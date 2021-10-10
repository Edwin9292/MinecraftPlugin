package me.mafkees92.MVdWPlaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import net.minelink.ctplus.CombatTagPlus;

public class mafkees_is_combattagged {
	CombatTagPlus ctp;
	
	public mafkees_is_combattagged(Main plugin) {
		ctp = (CombatTagPlus) Bukkit.getPluginManager().getPlugin("CombatTagPlus");
		if(ctp!=null) {
			PlaceholderAPI.registerPlaceholder(plugin, "mafkees_is_combattagged", event -> {
				Player player = event.getPlayer();
				if (player != null) {
					if(ctp.getTagManager().isTagged(player.getUniqueId())){
						return "istagged";
					}
					else {
						return "false";
					}
				}
				return null;
			});
		}
	}
}
