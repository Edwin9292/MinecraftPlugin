package me.mafkees92;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class OverrideHelpCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
				
		BaseComponent[] messages = new ComponentBuilder("")
				//.append(Utils.colorize("&6&m                         &7[&6&lTOOT&e&lMC &7: &eHelp&7]&6&m                        "))
				.append(Utils.colorize("&6&m                         &7[&6&lTOOT&e&lMC &7: &eHelp&7]&6&m                        "))
				.append("\n\n")
				.append(Utils.colorize("&6&nClick an option to quickly access it"))
				.append("\n\n")
				
				.append(Utils.createTextComponentCommand(
						Utils.colorize(" &7- &6Tutorial: &eVisit our ingame tutorial at /warps tutorial"),
						Utils.colorize("&6&lClick to go to /warp tutorial"), 
						"/warp tutorial"))
				.append("\n")
				.append(Utils.createTextComponentCommand(
						Utils.colorize(" &7- &6Rules: &eFind our rules with /rules"),
						Utils.colorize("&6&lClick to go to see the rules"), 
						"/rules"))
				.append("\n")
				.append(Utils.createTextComponentCommand(
						Utils.colorize(" &7- &6Vote: &eYou can find the vote links with /vote"),
						Utils.colorize("&6&lClick to go to see the voting links"), 
						"/vote"))
				.append("\n")
				.append(Utils.createTextComponentLink(
						Utils.colorize(" &7- &6Issues/Bugs: &ePlease report them at our discord page"),
						Utils.colorize("&6&lClick to visit our discord"), 
						"https://discord.gg/JCHUR7"))
				.append("\n")
				.append(Utils.createTextComponentCommand(
						Utils.colorize(" &7- &6Rule breakers: &eReport rule breakers with /report"),
						Utils.colorize("&6&lClick to report a rulebreaker"), 
						"/report"))
				.append("\n\n")
				.event((HoverEvent)null).event((ClickEvent) null)
				.append(Utils.colorize("&6&m                                                                     ")).create();
		
		
		player.spigot().sendMessage(messages);
		//player.spigot().sendMessage(messages.toArray(new TextComponent[messages.size()]));;
		return true;
	}


	
	
	
	
	
}
