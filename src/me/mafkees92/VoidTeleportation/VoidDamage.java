package me.mafkees92.VoidTeleportation;

import org.bukkit.Bukkit;
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
			Player player = (Player) e.getEntity();
			String worldName = player.getWorld().getName();
			if (worldName.equalsIgnoreCase("staff") || worldName.equalsIgnoreCase("skyblock")
					|| worldName.equalsIgnoreCase("skyblock_nether")
					|| worldName.equalsIgnoreCase("skyblock_the_end")
					|| worldName.equalsIgnoreCase("testing")) {
				e.setCancelled(true);
				if(ASkyBlockAPI.getInstance().hasIsland(player.getUniqueId()) || ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
					player.teleport(ASkyBlockAPI.getInstance().getHomeLocation(player.getUniqueId()));
				}
				else{
					Bukkit.dispatchCommand(player, "spawn");
				}
				player.setFallDistance(0);
			}
		}
	}
}
