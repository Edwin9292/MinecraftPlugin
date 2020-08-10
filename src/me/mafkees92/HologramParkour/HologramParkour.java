package me.mafkees92.HologramParkour;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class HologramParkour {
	
	Main plugin;
	Queue<Location> locationsToSpawn;
	int counter = 0;
	BukkitTask runningTask;
	int timer = 7;
	
	public HologramParkour(Player player, Main plugin) {
		this.plugin = plugin;
		
		locationsToSpawn = new LinkedList<Location>();

		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -598.5D, 73.9D, -2.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -591.5D, 70.9D, -13.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -581.5D, 70.9D, -25.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -562.5D, 70.9D, -32.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -558.5D, 71.9D, -44.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -554.5D, 71.9D, -54.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -551.5D, 69.9D, -70.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -543.5D, 68.9D, -103.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -537.5D, 73.9D, -107.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -537.5D, 74.9D, -87.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -529.5D, 73.9D, -84.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -526.5D, 73.9D, -96.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -526.5D, 85.9D, -96.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -526.5D, 93.9D, -96.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -527.5D, 100.9D, -95.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), -527.5D, 100.9D, -84.5D));
		
		//setup the hologram
		Hologram hologram = HologramsAPI.createHologram(plugin, locationsToSpawn.poll());
		hologram.appendTextLine(Utils.colorize("&3&lPICK THIS DIAMOND!!"));
		ItemLine itemline = hologram.appendItemLine(new ItemStack(Material.YELLOW_GLAZED_TERRACOTTA));

		//setup the hologram visibility
		hologram.getVisibilityManager().setVisibleByDefault(false);
		hologram.getVisibilityManager().showTo(player);
		
		//setup what happends if a hologram is picked up
		PickupHandler handler = new PickupHandler() {
			
			@Override
			public void onPickup(Player player) {
				if(runningTask != null) runningTask.cancel();
				timer = 7;
				counter ++;
				Location locToTeleport = locationsToSpawn.poll();
				
				//if its the last waypoint, set it as a nether star
				if(locationsToSpawn.size() == 0) {
					itemline.setItemStack(new ItemStack(Material.NETHER_STAR));
				}
				//if there is no more waypoint location left it means we have finished
				if(locToTeleport == null) {
					endParkour(player, hologram, true);
					return;
				}
			
				hologram.teleport(locToTeleport);
				runningTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> update(player, hologram), 0L , 20L);
			}
		};
		itemline.setPickupHandler(handler);
	}
	
	private void update(Player player, Hologram hologram) {
		if(!player.isOnline()) {
			if(runningTask!=null) runningTask.cancel();
			return;
		}
		if(timer > 0) {
			plugin.getActionBar().setCustomActionBar(player.getUniqueId(), 
					Utils.colorize("&e&lYou have &c&l" + timer + 
							"&e&l seconds left to find the next waypoint ("+ counter +"/" + (locationsToSpawn.size()+counter +1) + ")"));
			timer--;
		}
		else {
			endParkour(player, hologram, false);
			return;
		}
	}
	
	private void endParkour(Player player, Hologram hologram, boolean finished) {
		if(finished) {
			player.sendMessage("You got them all!");
		}
		else {
			player.sendMessage("You were to late. The parkour has ended");
			player.sendMessage("Your final score is: "+ counter);
		}
		plugin.getActionBar().removeCustomActionBar(player.getUniqueId());
		if(runningTask != null) runningTask.cancel();
		hologram.delete();
	}
	
	

}
