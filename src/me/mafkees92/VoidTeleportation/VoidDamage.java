package me.mafkees92.VoidTeleportation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import me.mafkees92.Main;
import me.mafkees92.HologramParkour.HologramParkour;
import me.mafkees92.HologramParkour.Parkour;

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
			if (worldName.equalsIgnoreCase("staff") 
					|| worldName.equalsIgnoreCase("skyblock")
					|| worldName.equalsIgnoreCase("skyblock_nether")
					|| worldName.equalsIgnoreCase("skyblock_the_end")
					|| worldName.equalsIgnoreCase("testing")) {
				e.setCancelled(true);
				if(Parkour.parkourParticipants.containsKey(player)) {
					Bukkit.getScheduler().runTask(Main.getInstance(), ()->{
						HologramParkour parkour = Parkour.parkourParticipants.get(player);
						Location loc;
						if(parkour.twoStepsBack != null) {
							loc = parkour.twoStepsBack;
						}
						else {
							loc = parkour.previousLocation;
						}
						loc.setPitch(player.getLocation().getPitch());
						loc.setYaw(player.getLocation().getYaw());
						player.setFallDistance(0);
						player.setVelocity(new Vector(0, 0, 0));
						player.teleport(loc);
					});
					return;
				}
				
				Island is = ASkyBlockAPI.getInstance().getIslandAt(player.getLocation());
				if(is != null) {
					if(is.isSpawn()) {
						Bukkit.getScheduler().runTask(Main.getInstance(), ()-> {
						player.setFallDistance(0);
						player.setVelocity(new Vector(0, 0, 0));
						player.teleport(is.getSpawnPoint().clone().add(0.5, 0, 0.5));
						});
					}
					else {
						Bukkit.getScheduler().runTask(Main.getInstance(), ()->{
							if(is.getOwner() != null) {
								Location loc = ASkyBlockAPI.getInstance().getHomeLocation(is.getOwner());
								player.setFallDistance(0);
								player.setVelocity(new Vector(0, 0, 0));
								if(loc !=null) {
									player.teleport(loc.clone().add(0.5,0,0.5));
								}
								else {
									player.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
								}
							}
							else {
								player.setFallDistance(0);
								player.setVelocity(new Vector(0, 0, 0));
								player.teleport(is.getSpawnPoint());
							}
						});
					}
				}
				else {
					Bukkit.getScheduler().runTask(Main.getInstance(), ()->{
					Bukkit.dispatchCommand(player, "spawn");
					});
				}

				player.setFallDistance(0);
			}
		}
	}
}
