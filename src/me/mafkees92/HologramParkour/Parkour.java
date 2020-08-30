package me.mafkees92.HologramParkour;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class Parkour implements CommandExecutor{

	private final String createParkourPermission = "mafkees.createparkour";
	
	private final Main plugin;
	
	public Parkour(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length > 0) {
			
			switch (args[0]) {
			
			case "create":
				
				break;

				
			case "start":
				
				break;
				
				
			default:
				this.sendInformationMessage(player);
				break;
			}
		}
		this.sendInformationMessage(player);
		
		
		return true;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void sendInformationMessage(Player player) {

		ComponentBuilder builder = new ComponentBuilder("")
		.append(Utils.colorize("&6&m              &7[&6&lTOOT&e&lMC &7: &eParkour Information&7]&6&m              "))
		.append("\n\n");
		
		if(player.isOp() || player.hasPermission(this.createParkourPermission)) {
			builder.append(Utils.createTextComponentCommand(
					Utils.colorize(" &7- &6List: &eShow all parkours currently created"),
					Utils.colorize("&6&lClick to display all parkour names"), 
					"/parkour list"));
			builder.append("\n");
			builder.event((HoverEvent)null).event((ClickEvent) null);
			
			
			builder.append(Utils.colorize(" &7- &6Create <parkourName>: &eCreate a new Parkour"));
			builder.append("\n");
		}
		
		builder.append(Utils.colorize(" &7- &6Start <parkourName>: &eStart a specific parkour"));
		
		builder.append("\n\n");
		builder.append(Utils.colorize("&6&m                                                                     "));

		BaseComponent[] message = builder.create();
		player.spigot().sendMessage(message);
	}
}
