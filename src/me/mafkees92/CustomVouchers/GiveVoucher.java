package me.mafkees92.CustomVouchers;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class GiveVoucher implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player commandExecutor = (Player) sender;

		String permission = "mafkeesplugin.givevoucher";
		if (commandExecutor.isOp() || commandExecutor.hasPermission(permission)) {
			if (args.length >= 2) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);

				if (targetPlayer != null && targetPlayer.isOnline()) {
					if (args[1].equalsIgnoreCase("permission")) {
						
						switch (args[2]) {
						case "essentials.fly":
							if(args.length == 5) {
								giveFlyVoucher(targetPlayer, args[2], args[3], args[4], commandExecutor);
							}
							else {
								commandExecutor.sendMessage(Messages.invalidFlyVoucherArguments);
							}
							break;
						default:
							break;
						}
						return true;
					}
					commandExecutor.sendMessage(Messages.invalidVoucherType);
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
	
	private void giveFlyVoucher(Player targetPlayer, String permission, String duration, String stackable, Player commandExector) {
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messages.flyVoucherName);

		List<String> lore = new ArrayList<>(Messages.flyVoucherLore);
        for (ListIterator<String> i = lore.listIterator(); i.hasNext(); )   {
        	String loreLine = i.next().replace("%duration%", Utils.luckPermDurationToFullDuration(duration));
        	loreLine = loreLine.replace("%rarity%", Utils.luckPermsDurationToRarityString(duration));
            i.set(Utils.colorize(loreLine));
        }
		
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.FROST_WALKER, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "customVoucher", "permission");
		item = Utils.setNBTTag(item, "permission", permission);
		item = Utils.setNBTTag(item, "duration", duration);
		item = Utils.setNBTTag(item, "stackable", stackable);
		
		if(targetPlayer.getInventory().firstEmpty() == -1) {
			//inventory is full
			commandExector.sendMessage(Messages.inventoryFull(targetPlayer));
		}
		else {
			targetPlayer.getInventory().addItem(item);
		}
	}
	
}
