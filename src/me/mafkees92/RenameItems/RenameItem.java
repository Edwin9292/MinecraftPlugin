package me.mafkees92.RenameItems;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class RenameItem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;		
		if (player.isOp() || player.hasPermission("mafkeesplugin.rename")) {
			if (args.length != 0) {
				ItemStack item = player.getInventory().getItemInMainHand();
				
				if (item != null && item.getType() != Material.AIR) {
					String displayName = String.join(" ", args);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(Utils.colorize("&f" + displayName));
					item.setItemMeta(meta);
		
					return true;
				}
				player.sendMessage(Utils.colorize("&cYou must hold an item in your hand when using this command"));
				return true;
			} 
			player.sendMessage(Utils.colorize("&cInvalid arguments. Try /rename newName"));
			return true;
		} 
		player.sendMessage(Messages.noPermission);
		return true;
	}
}
