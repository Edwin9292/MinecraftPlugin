package me.mafkees92.IslandChests;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.mafkees92.Main;
import me.mafkees92.Files.IslandChest;

public class OpenIslandChest implements CommandExecutor{

	Main plugin;
	
	public OpenIslandChest(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		IslandChest chest;
		
		if(ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
			UUID teamLeader = ASkyBlockAPI.getInstance().getTeamLeader(player.getUniqueId());
			chest = plugin.getIslandChests().getIslandChest(teamLeader);
			Inventory chestInv = chest.getInventory();
			player.openInventory(chestInv);
		}
		else if(ASkyBlockAPI.getInstance().hasIsland(player.getUniqueId())) {
			chest = plugin.getIslandChests().getIslandChest(player.getUniqueId());
			Inventory chestInv = chest.getInventory();
			player.openInventory(chestInv);
		}
		
		
		else {
			player.sendMessage("You need an island to use this command");
			return true;
		}
		
		
		return true;
	}

}
