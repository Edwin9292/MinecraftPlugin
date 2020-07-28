package me.mafkees92.MVdWPlaceholders;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.mafkees92.Main;

public class mafkees_player_health {

	public mafkees_player_health(Main plugin) {

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_player_health", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				if (player != null) {
					int health = (int) (Math.ceil(player.getHealth() * 5));
					return Integer.toString(health);
				}
				return null;
			}
		});
	}
}
