package me.mafkees92.MVdWPlaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import net.minelink.ctplus.CombatTagPlus;
import net.minelink.ctplus.Tag;

public class mafkees_combattag_time {
	CombatTagPlus ctp;
	
	public mafkees_combattag_time(Main plugin) {
		ctp = (CombatTagPlus) Bukkit.getPluginManager().getPlugin("CombatTagPlus");
		if(ctp!= null) {
			PlaceholderAPI.registerPlaceholder(plugin, "mafkees_combattag_time", event -> {
				Player player = event.getPlayer();
				if (player != null) {
					if(ctp.getTagManager().isTagged(player.getUniqueId())){
						Tag tag = ctp.getTagManager().getTag(player.getUniqueId());
						int duration = tag.getTagDuration();
						return "" + duration;
					}
					else {
						return "not tagged";
					}
				}
				return null;
			});
		}
	}
}
