package me.mafkees92.CustomVouchers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			if (!(item.getType().equals(Material.PAPER)))
				return;

			String nbtTag = Utils.getNBTTag(item, "customVoucher");
			if (nbtTag.equals("permission")) {
				String permission = Utils.getNBTTag(item, "permission");
				Player player = e.getPlayer();

				switch (permission) {
				case "essentials.fly":
					if(giveFlyPermission(player, 10, true)) {
						removeItemFromPlayerInventory(player, item);
					}
					break;

				default:
					break;
				}

			}
		}
	}

	private boolean giveFlyPermission(Player player, int durationInMinutes, boolean stackable) {
		
		//if player does not have the permission yet, give him the permission.
		if (!player.hasPermission("essentials.fly")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp essentials.fly %sm %s",
					player.getName(), durationInMinutes, stackable ? "accumulate" : ""));
			player.sendMessage(Messages.CustomVoucherMessages.firstVoucherUsed(durationInMinutes));
			player.setAllowFlight(true);
				return true;
				
		}
		//player already has the permission
		if(stackable) {
			Node node = main.getLuckperms().getUserManager().getUser(player.getUniqueId()).resolveInheritedNodes(QueryOptions.nonContextual()).
					stream().filter(x -> x.getKey().contentEquals("essentials.fly")).findFirst().orElse(null);
			if(node != null) {
				//if player does not have permanent fly
				if(node.hasExpiry()) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp essentials.fly %sm accumulate", player.getName(), durationInMinutes));
					player.sendMessage(Messages.CustomVoucherMessages.extentionVoucherUsed(durationInMinutes));
					player.setAllowFlight(true);
					return true;
				}else {
					player.sendMessage(Messages.CustomVoucherMessages.alreadyHasPermanentFly);
					return false;
				} 
			}else {
				player.sendMessage(Messages.CustomVoucherMessages.youAreOP);
				return false;
			}
		}
		player.sendMessage(Messages.CustomVoucherMessages.nonStackableFlightDuration);	
		return false;
	}
	
	/*
	// called when a permission voucher is used
	private boolean givePermission(Player player, String permission, int durationInSeconds, boolean stackable) {
		if (!player.hasPermission(permission)) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp %s %ss %s",
					player.getName(), permission, durationInSeconds, stackable ? "accumulate" : ""));
				return true;
				
		}
		if(stackable) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("lp user %s permission settemp %s %ss %s accumulate",
					player.getName(), permission, durationInSeconds));
			return true;
		}
		
		return false;
	}
    */
	private void removeItemFromPlayerInventory(Player player, ItemStack item) {
		if (item.getAmount() == 1)
			player.getInventory().removeItem(item);
		else {
			item.setAmount(item.getAmount() - 1);
			player.getInventory().setItemInMainHand(item);
		}
	}
}
