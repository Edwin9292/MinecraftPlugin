package me.mafkees92.MVdWPlaceholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class mafkees_remaining_fly_time {

	public mafkees_remaining_fly_time(Main plugin) {

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_remaining_fly_time", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				if (player.hasPermission("essentials.fly")) {
					User user = plugin.getLuckperms().getUserManager().getUser(player.getUniqueId());
					List<Node> nodes = user != null ? user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
							.filter(x -> x.getKey().contentEquals("essentials.fly")).collect(Collectors.toList()) : null;
					if (nodes != null) {
						if (nodes.size() == 1) {
							if (nodes.get(0).hasExpiry()) {
								return secondsToTimeString(Objects.requireNonNull(nodes.get(0).getExpiryDuration()).getSeconds());
							} else {
								return "Permanent";
							}
						} else if (nodes.size() > 1) {
							Node permNode = nodes.stream().filter(x -> (!x.hasExpiry())).findFirst()
									.orElse(null);
							if (permNode != null) {
								return "Permanent";
							} else {
								Node tempNode = nodes.stream().filter(Node::hasExpiry).findFirst()
										.orElse(null);
								if (tempNode != null) {
									return secondsToTimeString(Objects.requireNonNull(tempNode.getExpiryDuration()).getSeconds());
								} else {
									return "Error";
								}
							}
						} else {
							return "OP-Fly";
						}
					}

				}else {
					return "None";
				}
			}
			return null;
		});
	}

	private String secondsToTimeString(long sec) {

		int seconds = (int) sec % 60;
		int minutes = (int) TimeUnit.SECONDS.toMinutes(sec) % 60;
		int hours = (int) TimeUnit.SECONDS.toHours(sec) % 24;
		int days = (int) TimeUnit.SECONDS.toDays(sec);

		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append(days).append("D ");
		if (sec > 3600)
			sb.append(hours).append("H ");
		if (sec > 60)
			sb.append(minutes).append("M ");
		sb.append(seconds).append("S");

		return sb.toString();
	}
}
