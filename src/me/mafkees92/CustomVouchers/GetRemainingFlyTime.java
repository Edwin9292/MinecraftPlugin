package me.mafkees92.CustomVouchers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
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
		Node node = Objects.requireNonNull(main.getLuckperms().getUserManager().getUser(player.getUniqueId())).resolveInheritedNodes(QueryOptions.nonContextual()).
				stream().filter(x -> x.getKey().contentEquals("essentials.fly")).findFirst().orElse(null);
		
		if(node != null) {
			if(!node.hasExpiry()) {
				player.sendMessage(Messages.flightWontExpire);
			}
			else{
				long remainingTime = Objects.requireNonNull(node.getExpiryDuration()).getSeconds();
				player.sendMessage(Messages.flightExpirationTime(secondsToTimeString(remainingTime)));
			}
		}
		else {
			player.sendMessage(Messages.noFlightTimeLeft);
		}
		return true;
	}
	

	private String secondsToTimeString(long sec) {

		int seconds = (int) sec%60;
		int minutes = (int) TimeUnit.SECONDS.toMinutes(sec)%60;
		int hours = (int) TimeUnit.SECONDS.toHours(sec)%24;
		int days = (int) TimeUnit.SECONDS.toDays(sec);
		
		StringBuilder sb = new StringBuilder();
		if(days > 0)
			sb.append(days).append(" Days, ");
		if(sec > 3600)
			sb.append(hours).append(" Hours, ");
		if(sec > 60)
			sb.append(minutes).append(" Minutes &aand&e ");
		sb.append(seconds).append(" Seconds&a.");
		
		return sb.toString();
	}
}
