package me.mafkees92.ChunkLoaders;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;

public class GiveChunkLoader implements CommandExecutor{

	
	Main plugin;
	public GiveChunkLoader(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player target = Bukkit.getPlayer(args[0]);
		
		target.getInventory().addItem(ChunkLoader.CreateChunkLoaderItem());
		
		return false;
	}

}
