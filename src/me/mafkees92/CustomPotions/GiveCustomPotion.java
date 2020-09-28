package me.mafkees92.CustomPotions;

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

import me.mafkees92.Files.Messages;
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
							meta.setDisplayName(Messages.freezePotionDisplayName);
							meta.setColor(Color.BLUE);
							meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							meta.addEnchant(Enchantment.FROST_WALKER, 1, false);

							meta.setLore(Messages.freezePotionLore);
							item.setItemMeta(meta);
							item = Utils.setNBTTag(item, "customPotions", "freezePotion");
							
							try {
								int amount = Integer.parseInt(args[2]);

								for (int i = 0; i < amount; i++) {
									
									if(targetPlayer.getInventory().firstEmpty() == -1) {
										targetPlayer.sendMessage(Messages.customPotionReceivedInventoryFull);
										player.sendMessage(Messages.customPotionSendTargetInventoryFull);
										
										item.setAmount(amount-i);
										targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), item);
									}
									else {
										targetPlayer.getInventory().addItem(item);
									}
								}
								return true;
							} catch (Exception e) {
								player.sendMessage(Messages.invalidAmount);
								return true;
							}
							
						}
						else {
							player.sendMessage(Messages.invalidPotionType);
							return true;
						}
					} else {
						player.sendMessage(Messages.invalidTargetPlayer);
						return true;
					}
				} else {
					player.sendMessage(Messages.invalidPotionArguments);
					return true;
				}
			} else {
				player.sendMessage(Messages.noPermission);
				return true;
			}
		}
		return false;
	}

}
