package me.mafkees92.Commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;

public class GetRemainingFlyTime implements CommandExecutor{

	Main main;

	public GetRemainingFlyTime(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;

		//List<Node> nodes = main.getLuckperms().getUserManager().getUser(player.getUniqueId()).getNodes().stream().filter(x -> x.getKey().equals("essentials.fly")).collect(Collectors.toList());
		List<Node> nodes = main.getLuckperms().getUserManager().getUser(player.getUniqueId()).resolveInheritedNodes(QueryOptions.nonContextual()).
				stream().filter(x -> x.getKey().contentEquals("essentials.fly")).collect(Collectors.toList());
		
		if(nodes.size() >= 1) {
			Node node = nodes.get(0);
			if(!node.hasExpiry()) {
				player.sendMessage(Utils.colorize("&7You have permanent fly. Your fly will not expire"));
			}
			else{
				long remainingTime = node.getExpiryDuration().getSeconds();
				player.sendMessage("You have " + remainingTime + " seconds flytime left");
			}
			player.sendMessage(nodes.toString());
		}
		
		return true;
	}
}
