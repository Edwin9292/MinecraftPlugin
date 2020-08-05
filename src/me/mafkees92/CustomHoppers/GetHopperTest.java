package me.mafkees92.CustomHoppers;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;

public class GetHopperTest implements CommandExecutor {

	Main plugin;
	public GetHopperTest(Main plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		
		
		Player player = (Player) sender;
		Location location = player.getLocation();
		Chunk chunk = location.getChunk();
		
		// get hopperdata instance and see if it contains a hopper in this chunk	
		
		if(plugin.getHopperData().containsHopperInChunk(chunk, player)) {
			player.sendMessage("True " + chunk.toString());
		}
		else
			player.sendMessage("False " + chunk.toString());
		
		return true;
	}

}
