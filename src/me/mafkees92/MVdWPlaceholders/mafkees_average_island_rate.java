package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class mafkees_average_island_rate {

	public mafkees_average_island_rate(Main plugin) {
		
		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_average_island_rate", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				UUID islandLeader = Utils.getTeamOrIslandOwner(player.getUniqueId());
				if(islandLeader != null) {
					return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(islandLeader), "%islandrate_average_rating%").substring(0, 1);
				}
			}
			return null;
		});
	}
}
