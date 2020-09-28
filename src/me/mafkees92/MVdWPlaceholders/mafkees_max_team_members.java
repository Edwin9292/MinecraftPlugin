package me.mafkees92.MVdWPlaceholders;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class mafkees_max_team_members {

	public mafkees_max_team_members(Main plugin) {
		
		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_max_team_members", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				UUID islandOwner = Utils.getTeamOrIslandOwner(player.getUniqueId());

				if(islandOwner == null)
					return Utils.colorize("&cNo Island Found");

				else {
					
					User user = plugin.getLuckperms().getUserManager().getUser(islandOwner);
					List<Node> nodes = user != null ? user.getNodes().stream()
							.filter(x -> x.getKey().contains("askyblock.team.maxsize")).collect(Collectors.toList()) : null;
							
					int maxTeamMembers = 6;
					for (Node node : nodes) {
						String perm = node.getKey();
						int temp = Utils.tryParseInt(perm.split("[.]")[3]);
						if (temp> maxTeamMembers)
							maxTeamMembers = temp;
					}
					return "" + maxTeamMembers;
				}
			}
			return null;
		});
	}

}
