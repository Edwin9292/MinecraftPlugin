package me.mafkees92.MVdWPlaceholders;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.mafkees92.Main;

public class mafkees_average_island_rate {

	public mafkees_average_island_rate(Main plugin) {

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_average_island_rate", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				if (player != null) {
					return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, "%islandrate_average_rating%")
							.substring(0, 1);
				}
				return null;
			}
		});
	}
}
