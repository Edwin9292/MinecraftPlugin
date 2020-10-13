package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class mafkees_island_locked {

	public mafkees_island_locked(Main plugin) {
		
		PlaceholderAPI.registerPlaceholder(plugin, "mafkees_island_locked", event -> {
			Player player = event.getPlayer();
			if (player != null) {
				UUID islandOwner = Utils.getTeamOrIslandOwner(player.getUniqueId());

				if(islandOwner == null)
					return "false";

				else {
					return Boolean.toString(ASkyBlockAPI.getInstance().getIslandOwnedBy(islandOwner).isLocked());
				}
			}
			return "false";
		});
	}
}
