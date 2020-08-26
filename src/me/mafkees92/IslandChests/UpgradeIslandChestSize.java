package me.mafkees92.IslandChests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Files.IslandChest;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class UpgradeIslandChestSize implements CommandExecutor
{

	Main plugin; 
	
	public UpgradeIslandChestSize(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			sender.sendMessage(Messages.noPermission);
			return true;
		}
		
		if(args.length == 1) {
			if(Bukkit.getOfflinePlayer(args[0]) != null) {
				UUID islandOwner = Utils.getTeamOrIslandOwner(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
				IslandChest chest = null;
				if (islandOwner != null) {
					chest = plugin.getIslandChests().getIslandChest(islandOwner);
				}
				if (chest != null) {
					if(chest.setInventorySize(chest.getInventory().getSize() + 9)) {
						sender.sendMessage("Succesfully increased the size of the islandChest to " + chest.getInventory().getSize());
					}
					else {
						sender.sendMessage("Players island chest is already on max size");
					}
				}
			}
		}
		return false;
	}

}
