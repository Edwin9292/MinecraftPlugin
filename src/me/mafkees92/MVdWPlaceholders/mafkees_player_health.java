package me.mafkees92.MVdWPlaceholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import org.bukkit.entity.Player;

public class mafkees_player_health {

	public mafkees_player_health(Main plugin) {

		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_player_health", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				int health = (int) (Math.ceil(player.getHealth() * 5));
				return Integer.toString(health);
			}
			return null;
		});
	}
}
