package me.mafkees92.CustomVouchers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class GiveVoucher implements CommandExecutor {

	///givevoucher <playername> <vouchername>'
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length >= 2) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (targetPlayer != null && targetPlayer.isOnline()) {
					Voucher voucher = getCustomVoucherFromConfig(args[1]);
					if(voucher != null) {
						if(args.length == 3) {
							int amount = Utils.tryParseInt(args[2]);
							if(amount != -1) {
								giveVoucher(targetPlayer, voucher, amount, null);
								return true;
							}
							else {
								Bukkit.getLogger().warning("Invalid arguments for giving a voucher");
								return true;
							}
						}
						else {
							giveVoucher(targetPlayer, voucher, 1, null);
							return true;
						}
					}
					Bukkit.getLogger().warning("No voucher with the name: " + args[1] + " exists.");
					return false;
				}
				Bukkit.getLogger().warning("Cannot give a voucher: Target player is not online");
				return false;
			}
			Bukkit.getLogger().warning("Invalid arguments for giving a voucher");
			return false;
		}
		
		Player commandExecutor = (Player) sender;
		String permission = "mafkeesplugin.givevoucher";
		if (commandExecutor.isOp() || commandExecutor.hasPermission(permission)) {
			if (args.length >= 2) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (targetPlayer != null && targetPlayer.isOnline()) {
					Voucher voucher = getCustomVoucherFromConfig(args[1]);
					if(voucher != null) {

						if(args.length == 3) {
							int amount = Utils.tryParseInt(args[2]);
							if(amount != -1) {
								giveVoucher(targetPlayer, voucher, amount, commandExecutor);
								return true;
							}
							else {
								commandExecutor.sendMessage("Invalid arguments for giving a voucher");
								return true;
							}
						}
						giveVoucher(targetPlayer, voucher, 1, commandExecutor);
						return true;
					}
						
					String validPermissions = "";
					for (Voucher tempVoucher : CustomVouchers.vouchersList) {
						validPermissions += tempVoucher.voucherName + ", ";
					}
					if(validPermissions.length() > 2) {
						validPermissions = validPermissions.substring(0, validPermissions.length() - 2);
					}
					commandExecutor.sendMessage(Messages.invalidVoucherType.replace("%names%", validPermissions));
					return true;
				}
				commandExecutor.sendMessage(Messages.invalidTargetPlayer);
				return true;
			}
			commandExecutor.sendMessage(Messages.invalidVoucherArguments);
			return true;
		}
		commandExecutor.sendMessage(Messages.noPermission);
		return true;
	}
	
	
	private Voucher getCustomVoucherFromConfig(String voucherName) {
		return CustomVouchers.vouchersList.stream().filter(x -> x.voucherName.equalsIgnoreCase(voucherName)).findFirst().orElse(null);
	}
	
	
	private void giveVoucher(Player targetPlayer, Voucher voucher, int amount, Player commandExector) {
		ItemStack item = voucher.getItemStack();
		item.setAmount(amount);
		
		if(targetPlayer.getInventory().firstEmpty() == -1 && commandExector != null) {
			commandExector.sendMessage(Messages.inventoryFull(targetPlayer));
		}
		else {
			if(commandExector != null) {
				commandExector.sendMessage(Utils.colorize("The permission voucher has been given to " + targetPlayer.getDisplayName()));
			}
			targetPlayer.getInventory().addItem(item);
			targetPlayer.sendMessage(Utils.colorize("&7You have been given &6" + item.getAmount() + "x &7" + item.getItemMeta().getDisplayName() +"&7!"));
		}
	}
	
}
