package me.mafkees92.MVdWPlaceholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import me.mafkees92.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class mafkees_island_chest_size {

	public mafkees_island_chest_size(Main plugin) {
		
	ASkyBlockAPI skyBlock = ASkyBlockAPI.getInstance();

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_average_island_rate", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				if(skyBlock.hasIsland(player.getUniqueId())) {
					return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, "%islandrate_average_rating%").substring(0, 1);
				}
				else if(skyBlock.inTeam(player.getUniqueId())) {
					UUID IslandLeader = skyBlock.getTeamLeader(player.getUniqueId());
					return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(IslandLeader), "%islandrate_average_rating%").substring(0, 1);
				}
			}
			return null;
		});
	}
}