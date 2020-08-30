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
				.append(Utils.colorize("&6&m                          &7[&6&lTOOT&e&lMC &7: &eHelp&7]&6&m                          "))
				.append("\n")
				.append(Utils.colorize("&6&nClick an option to quickly access it"))
				.append("\n\n")
				
				.append(createTextComponentCommand(
						Utils.colorize(" &7- &6Tutorial: &eVisit our ingame tutorial at /warps tutorial"),
						Utils.colorize("&6&lClick to go to /warp tutorial"), 
						"/warp tutorial"))
				.append("\n")
				.append(createTextComponentCommand(
						Utils.colorize(" &7- &6Rules: &eFind our rules with /rules"),
						Utils.colorize("&6&lClick to go to see the rules"), 
						"/rules"))
				.append("\n")
				.append(createTextComponentCommand(
						Utils.colorize(" &7- &6Vote: &eYou can find the vote links with /vote"),
						Utils.colorize("&6&lClick to go to see the voting links"), 
						"/vote"))
				.append("\n")
				.append(createTextComponentLink(
						Utils.colorize(" &7- &6Issues/Bugs: &ePlease report them at our discord page"),
						Utils.colorize("&6&lClick to visit our discord"), 
						"https://discord.gg/JCHUR7"))
				.append("\n")
				.append(createTextComponentCommand(
						Utils.colorize(" &7- &6Rule breakers: &eRule breakers can be reported with /report"),
						Utils.colorize("&6&lClick to report a rulebreaker"), 
						"/report"))
				.append("\n\n")
				.append(Utils.colorize("&6&m                                                                         ")).create();
		
		
		player.spigot().sendMessage(messages);
		//player.spigot().sendMessage(messages.toArray(new TextComponent[messages.size()]));;
		return true;
	}

	private BaseComponent[] createTextComponentCommand(String text, String hoverText, String clickCommand) {
		ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand);
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create());
		return new ComponentBuilder(text).event(clickEvent).event(hoverEvent).create();
	}
	
	private BaseComponent[] createTextComponentLink(String text, String hoverText, String link) {
		ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, link);
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create());
		return new ComponentBuilder(text).event(clickEvent).event(hoverEvent).create();
	}
	
	
	
	
	
	
	
	
	
}
