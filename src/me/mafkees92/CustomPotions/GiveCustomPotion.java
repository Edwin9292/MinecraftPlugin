package me.mafkees92.CustomPotions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;


import me.mafkees92.Utils.Utils;

public class GiveCustomPotion implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp() || player.hasPermission("mafkeesplugin.givepotion")) {
				if (args.length == 3) {
					if (Bukkit.getPlayer(args[0]).isOnline()) {
						Player targetPlayer = Bukkit.getPlayer(args[0]);
						if (args[1].equalsIgnoreCase("freeze")) {
							ItemStack item = new ItemStack(Material.SPLASH_POTION);
							PotionMeta meta = (PotionMeta)item.getItemMeta();
							meta.setDisplayName(Utils.colorize("&bFreeze Potion"));
							meta.setColor(Color.BLUE);
							meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							meta.addEnchant(Enchantment.FROST_WALKER, 1, false);

							List<String> lore = new ArrayList<>();
							lore.add(Utils.colorize("&9Freeze your opponents in a 4 block Radius!"));
							lore.add(Utils.colorize("&9Duration: 4 seconds "));
							meta.setLore(lore);
							item.setItemMeta(meta);
							item = Utils.setNBTTag(item, "customPotions", "freezePotion");
							
							try {
								int amount = Integer.parseInt(args[2]);

								for (int i = 0; i < amount; i++) {
									
									if(targetPlayer.getInventory().firstEmpty() == -1) {
										targetPlayer.sendMessage(Utils.colorize("&cYou have received a custom freeze potion. Your inventory is full, so it has been dropped on the floor"));
										player.sendMessage(Utils.colorize("&cTarget players inventory was full. The item has been dropped on the ground at his location"));
										
										item.setAmount(amount-i);
										targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), item);
									}
									else {
										targetPlayer.getInventory().addItem(item);
									}
								}
								return true;
							} catch (Exception e) {
								player.sendMessage(Utils.colorize("&cInvalid amount entered, please try again."));
								return true;
							}
							
						}
						else {
							player.sendMessage(Utils.colorize("&cInvalid Potion type, Valid potion types are {freeze}"));
							return true;
						}
					} else {
						player.sendMessage(Utils.colorize("&cTarget player is either invalid or not online"));
						return true;
					}
				} else {
					player.sendMessage(Utils.colorize("&cInvalid Arguments. Try /givecustompotion <playername> <potiontype> <amount>"));
					return true;
				}
			} else {
				player.sendMessage(Utils.colorize("&cYou dont have permission to use this command"));
				return true;
			}
		}
		return false;
	}

}
