package me.mafkees92.RenameItems;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Utils.Utils;

public class RenameItem implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		if(args.length > 1) {
			player.sendMessage(Utils.colorize("&cInvalid arguments. Try /rename newName"));
			return false;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item == null || item.getType() == Material.AIR) {
			player.sendMessage(Utils.colorize("&cYou must hold an item in your hand when using this command"));
			return false;
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colorize("&f" + args[0]).replace("_", " "));
		item.setItemMeta(meta);
		
		
		
		return true;
	}
}
