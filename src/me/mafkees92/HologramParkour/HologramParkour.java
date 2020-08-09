package me.mafkees92.HologramParkour;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	
	public HologramParkour(Main plugin) {
		this.plugin = plugin;
		
		locationsToSpawn = new LinkedList<Location>();

		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 3.5D, 76.9D, 1802.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 10.5D, 76.9D, 1802.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 19.5D, 76.9D, 1802.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 19.5D, 76.9D, 1808.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 19.5D, 76.9D, 1815.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 12.5D, 76.9D, 1815.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 3.5D, 76.9D, 1815.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 3.5D, 76.9D, 1810.5D));
		locationsToSpawn.add(new Location(Bukkit.getWorld("SkyBlock"), 3.5D, 76.9D, 1802.5D));
		
		Hologram hologram = HologramsAPI.createHologram(plugin, locationsToSpawn.poll());
		hologram.appendTextLine(Utils.colorize("&3&lPICK THIS DIAMOND!!"));
		ItemLine itemline = hologram.appendItemLine(new ItemStack(Material.DIAMOND));
		
		hologram.getVisibilityManager().setVisibleByDefault(false);
		
		PickupHandler handler = new PickupHandler() {
			
			@Override
			public void onPickup(Player player) {
				player.sendMessage("You have picked up hologram number: " + counter);
				counter ++;
				Location locToTeleport = locationsToSpawn.poll();
				if(locToTeleport == null) {
					player.sendMessage("You got them all!");
					hologram.delete();
					return;
				}
				hologram.teleport(locToTeleport);
			}
		};
		itemline.setPickupHandler(handler);
	}
	
	
	

}
