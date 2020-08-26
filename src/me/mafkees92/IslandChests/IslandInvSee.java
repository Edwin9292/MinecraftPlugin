package me.mafkees92.IslandChests;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.mafkees92.Main;
import me.mafkees92.Files.IslandChest;
import me.mafkees92.Files.Messages;

public class IslandInvSee implements CommandExecutor{

	
	Main plugin;
	public IslandInvSee(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		//command is /islandinvsee <playername>
		if(!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		
		//check permissions
		if(!player.isOp() || player.hasPermission("mafkeesplugin.islandinvsee")){
			//check if there is only 1 argument(Playername)
			if(args.length == 1) {
				//Check if it's a valid playername
				if(Bukkit.getOfflinePlayer(args[0]) != null) {
					UUID target = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
					IslandChest chest;
					if(ASkyBlockAPI.getInstance().inTeam(target)) {
						UUID teamLeader = ASkyBlockAPI.getInstance().getTeamLeader(target);
						chest = plugin.getIslandChests().getIslandChest(teamLeader);
					}
					else if(ASkyBlockAPI.getInstance().hasIsland(target)){
						chest = plugin.getIslandChests().getIslandChest(target);
					}
					else {
						player.sendMessage("TARGET PLAYER HAS NO ISLAND");
						return true;
					}
					
					//player has an island
					if(chest != null) {
						player.openInventory(chest.getInventory());
						return true;
					}
					player.sendMessage("FAILED LOADING PLAYERS ISLANDCHEST");
					return true;
					
				}
				player.sendMessage("INVALID TARGET PLAYER");
				return true;
			}
			player.sendMessage("INVALID ARGUMENTS. VALID ARGUMENTS ARE /is invsee <PlayerName>");
			player.sendMessage("YOU HAVE SENT " + args.length + " arguments");
			return true;
		}
		player.sendMessage(Messages.noPermission);
		return true;
	}

	
	
	
	
	
	
}

