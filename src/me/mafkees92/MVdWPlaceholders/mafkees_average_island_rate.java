package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.mafkees92.Main;

public class mafkees_average_island_rate {

	public mafkees_average_island_rate(Main plugin) {
		
	ASkyBlockAPI skyBlock = ASkyBlockAPI.getInstance();

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_average_island_rate", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
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
			}
		});
	}
}
