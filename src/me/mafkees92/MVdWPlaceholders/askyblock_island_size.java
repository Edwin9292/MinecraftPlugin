package me.mafkees92.MVdWPlaceholders;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.mafkees92.Main;

public class askyblock_island_size {
	
public askyblock_island_size(Main plugin) {
	
		PlaceholderAPI.registerPlaceholder(plugin, "askyblock_island_size", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
				Player player = event.getPlayer();
				if(player != null) {
					if(ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
						UUID leader = ASkyBlockAPI.getInstance().getTeamLeader(player.getUniqueId());
						int size = ASkyBlockAPI.getInstance().getIslandOwnedBy(leader).getProtectionSize();
						return size + "x" + size;
					}
					else {
						Island is = ASkyBlockAPI.getInstance().getIslandOwnedBy(player.getUniqueId());
						if(is != null) {
							return is.getProtectionSize() + "x" + is.getProtectionSize();
						}
					}
				}
				return null;
			}
		});
	}

}
