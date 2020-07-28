package me.mafkees92.MVdWPlaceholders;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.mafkees92.Main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;

public class mafkees_remaining_fly_time {

	public mafkees_remaining_fly_time(Main plugin) {

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_remaining_fly_time", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				if (player != null) {
					
					User user = plugin.getLuckperms().getUserManager().getUser(player.getUniqueId());
					List<Node> nodes = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream().filter(x -> x.getKey().contentEquals("essentials.fly")).collect(Collectors.toList());
					if(nodes.size() == 1) {
						if(nodes.get(0).hasExpiry()) {
							return secondsToTimeString(nodes.get(0).getExpiryDuration().getSeconds());
						}
						else {
							return "Permanent";
						}
					}
					else if(nodes.size() > 1){
						Node permNode = nodes.stream().filter(x -> (x.hasExpiry() == false)).findFirst().orElse(null);
						if(permNode != null) {
							return "Permanent";
						}
						else {
							Node tempNode = nodes.stream().filter(x -> x.hasExpiry() == true).findFirst().orElse(null);
							if(tempNode != null) {
								return secondsToTimeString(tempNode.getExpiryDuration().getSeconds());
							}else {
								return "Error";
							}
						}
					}
					else {
						return "None";
					}
					
				}
				return null;
			}
		});
	}
	
	private String secondsToTimeString(long sec) {

		int seconds = (int) sec%60;
		int minutes = (int) TimeUnit.SECONDS.toMinutes(sec)%60;
		int hours = (int) TimeUnit.SECONDS.toHours(sec)%24;
		int days = (int) TimeUnit.SECONDS.toDays(sec);
		
		StringBuilder sb = new StringBuilder();
		if(days > 0)
			sb.append(days + "D ");
		if(sec > 3600)
			sb.append(hours + "H ");
		if(sec > 60)
			sb.append(minutes + "M ");
		sb.append(seconds + "S");
		
		return sb.toString();
	}
}
