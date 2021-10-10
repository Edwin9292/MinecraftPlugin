package me.mafkees92.CustomVouchers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;

public class VoucherUsageEvent implements Listener {
	
	Main main;
	public VoucherUsageEvent(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onVoucherUsage(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getHand() == EquipmentSlot.OFF_HAND) return;
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			
			String nbtPermissionTag = Utils.getNBTTag(item, "customVoucher");
			if (nbtPermissionTag != null) {

				Player player = e.getPlayer();
				List<Voucher> vouchers = getCustomVouchersFromConfig(nbtPermissionTag);
				if(vouchers.size() == 1) {
					if (givePermission(player, vouchers.get(0))) {
						removeItemFromPlayerInventory(player, item);
					}
				}
				else if(vouchers.size() > 1){
					Voucher voucher = vouchers.stream().filter(x -> x.duration.equalsIgnoreCase(Utils.getNBTTag(item, "duration"))).findFirst().orElse(null);
					if(voucher !=null) {
						if (givePermission(player, voucher)) {
							removeItemFromPlayerInventory(player, item);
						}
					}
				}
			}
		}
	}
	
	private List<Voucher> getCustomVouchersFromConfig(String permission) {
		return CustomVouchers.vouchersList.stream().filter(x -> x.permission.equalsIgnoreCase(permission)).collect(Collectors.toList()) ;
	}

	private boolean givePermission(Player player, Voucher voucher) {
		
		boolean isPermanent = voucher.duration.equalsIgnoreCase("-1");
		
		String voucherPermission = voucher.permission;
		String[] splitPermission = voucherPermission.split("[.]");
		if(StringUtils.isNumeric(splitPermission[splitPermission.length -1])) {
			String permToCheck = "";
			for (int i = 0; i < splitPermission.length -1; i++) {
				permToCheck += splitPermission[i];
				if(!(i == splitPermission.length -1)) {
					permToCheck += ".";
				}
			}
			for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
				if(permission.getPermission().startsWith(permToCheck) && permission.getValue()) {
					String[] split = permission.getPermission().split("[.]");
					if(StringUtils.isNumeric(split[split.length -1])) {
						int amount = Integer.parseInt(split[split.length -1]);
						if(amount >= Integer.parseInt(splitPermission[splitPermission.length -1])) {
							player.sendMessage(voucher.alreadyHasBetterPermissionMessage.replace("%amount%", ""+amount));
							return false;
						}
					}
				}
			}
		}
		
		//if player does not have the permission yet, give him the permission.
		if (!player.hasPermission(voucher.permission)) {
			if(isPermanent) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission set " + voucher.permission + " true",
						player.getName()));
			}
			else {
//				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp "+ voucher.permission + "true %s %s",
//						player.getName(), voucher.duration, voucher.isStackable ? "accumulate" : ""));
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp "+ voucher.permission + " true %s accumulate",
						player.getName(), voucher.duration));
			}
			player.sendMessage(voucher.firstVoucherUsedMessage);
			if(voucher.permission.equalsIgnoreCase("essentials.fly")) {
				player.setAllowFlight(true);
			}
			return true;
		}
		
				
		//player already has the permission
//		if(voucher.isStackable) {
			Node node = Objects.requireNonNull(main.getLuckperms().getUserManager().getUser(player.getUniqueId())).resolveInheritedNodes(QueryOptions.nonContextual()).
					stream().filter(x -> x.getKey().contentEquals(voucher.permission)).findFirst().orElse(null);
			if(node != null) {
				//if player does not have permanent fly
				if(node.hasExpiry()) {
					if(isPermanent) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission set " + voucher.permission + " true",
								player.getName()));
					}
					else {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp "+ voucher.permission + " true %s accumulate",
								player.getName(), voucher.duration));
					}
					player.sendMessage(voucher.extentionVoucherUsedMessage);
					player.setAllowFlight(true);
					return true;
				}else {
					player.sendMessage(voucher.alreadyHasPermanentMessage);
					return false;
				} 
			}else {
				player.sendMessage(Messages.OPAlreadyHasPermissions);
				return false;
			}
//		}
//		player.sendMessage(voucher.nonStackableUsedMessage);	
//		return false;
	}
	
	
	private void removeItemFromPlayerInventory(Player player, ItemStack item) {
		if (item.getAmount() == 1)
			player.getInventory().removeItem(item);
		else {
			item.setAmount(item.getAmount() - 1);
			player.getInventory().setItemInMainHand(item);
		}
	}
}
