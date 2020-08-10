package me.mafkees92.HologramParkour;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;

public class StartParkour implements CommandExecutor {

	Main plugin;
	
	public StartParkour(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player))
			return false;
		
		new HologramParkour((Player)sender, plugin);
		
		
		return true;
	}

}
