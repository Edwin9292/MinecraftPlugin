package me.mafkees92.Commands;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class RepairHand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if(!player.isOp()) {
			player.sendMessage(Utils.colorize("&cYou don't have permission to use this command!"));
			return true;
		}
		if(Main.econ.getBalance(player) < 10000) {
			player.sendMessage(Utils.colorize("&cYou don't have enough money to fix this item!"));
			return true;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() == Material.AIR || item.getDurability() == 0) {
			player.sendMessage(Utils.colorize("&cError: &4This item cannot be repaired."));
			return true;
		}
		
		else {
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName()) {
				player.sendMessage(Utils.colorize("&7You have succesfully repaired your &e" + meta.getDisplayName() + "&7."));
			}
			else {
				player.sendMessage(Utils.colorize("&7You have succesfully repaired your &e" + 
						WordUtils.capitalizeFully(item.getType().name().replace("_", " ")) + "&7."));
			}
			item.setDurability((short) 0);
			Main.econ.withdrawPlayer(player, 10000);
		}
		
		return true;
	}
}
