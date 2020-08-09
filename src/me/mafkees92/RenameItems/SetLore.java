package me.mafkees92.RenameItems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class SetLore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) 
			return false;
			
		Player player = (Player) sender;
		if(player.isOp() || player.hasPermission("mafkeesplugin.setlore")) {
			if(args.length != 0){
				String loreText = String.join(" ", args);
				String[] loreLines = loreText.split("[/]");
				if(loreLines.length > 0){
					for (int i = 0; i < loreLines.length; i++) {
						loreLines[i] = Utils.colorize("&f" + loreLines[i]);
					}
				}
								
				ItemStack item = player.getInventory().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				List<String> lore = Arrays.asList(loreLines);
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				player.sendMessage(Messages.succeededSettingLore);
				
				return true;
			}
			player.sendMessage(Messages.noSetLoreArguments);
			return true;
		}
		player.sendMessage(Messages.noPermission);
		return true;
	}
}
