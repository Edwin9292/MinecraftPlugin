package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Files.IslandChest;
import me.mafkees92.Utils.Utils;

public class mafkees_average_island_rate {

	public mafkees_average_island_rate(Main plugin) {
		
		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_island_chest_size", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				UUID islandOwner = Utils.getTeamOrIslandOwner(player.getUniqueId());

				if(islandOwner == null)
					return Utils.colorize("&cNo Island Found");

				else {
					IslandChest chest = plugin.getIslandChests().getIslandChest(islandOwner);
					if(chest != null) {
						return "" + chest.getInventory().getSize();
					}
					return "Error";
				}
			}
			return null;
		});
	}
}
