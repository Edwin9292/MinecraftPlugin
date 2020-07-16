package me.mafkees92.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.mafkees92.Main;

public class VoidDamage implements Listener {

	Plugin plugin;

	public VoidDamage(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onVoidDamage(EntityDamageEvent e) {
		if (e.getCause() != DamageCause.VOID)
			return;
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
			Player player = (Player) e.getEntity();
			player.teleport(ASkyBlockAPI.getInstance().getHomeLocation(player.getUniqueId()));

			player.setFallDistance(0);
			
		}
	}

}
