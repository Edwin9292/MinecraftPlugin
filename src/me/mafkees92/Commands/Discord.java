package me.mafkees92.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Files.Messages;

public class Discord implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		//display a link to the discord page
		player.spigot().sendMessage(Messages.discordMessage);
		
		return true;
	}

}
