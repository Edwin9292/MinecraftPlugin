package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class mafkees_has_island {

	public mafkees_has_island(Main plugin) {
		
		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_has_island", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				UUID islandLeader = Utils.getTeamOrIslandOwner(player.getUniqueId());
				if(islandLeader == null) {
					return "noisland";
				}
				return "hasisland";
			}
			return null;
		});
	}
}
