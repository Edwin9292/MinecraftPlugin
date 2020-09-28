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
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class HologramParkour {
	
	Main plugin;
	Queue<Location> locationsToSpawn;
	int counter = 0;
	BukkitTask runningTask;
	int timer = 7;
	private long startTime = -1;
	private long endTime = -1;
	
	public HologramParkour(LinkedList<Location> waypoints, Player player, Main plugin) {
		
		this.locationsToSpawn = waypoints;
		this.plugin = plugin;
		Parkour.parkourParticipants.add(player);
		
		//setup the hologram
		Hologram hologram = HologramsAPI.createHologram(plugin, locationsToSpawn.poll());
		TextLine text = hologram.appendTextLine(Utils.colorize("&3&lPICK THIS STONE!!"));
		ItemLine itemline = hologram.appendItemLine(new ItemStack(Material.STONE));

		//setup the hologram visibility
		hologram.getVisibilityManager().setVisibleByDefault(false);
		hologram.getVisibilityManager().showTo(player);
		
		//setup what happends if a hologram is picked up
		PickupHandler handler = player1 -> {
			if(startTime == -1)
				startTime = System.currentTimeMillis();
			if(runningTask != null) runningTask.cancel();
			timer = 7;
			counter ++;
			Location locToTeleport = locationsToSpawn.poll();

			//set the item based on the amount of items pickedup
			if(counter >= 3  && counter < 6) {
				itemline.setItemStack(new ItemStack(Material.GOLD_INGOT));
				text.setText(Utils.colorize("&3&lPICK THIS GOLD_INGOT!!"));
			}
			else if(counter >= 6 && counter < 10) {
				itemline.setItemStack(new ItemStack(Material.DIAMOND));
				text.setText(Utils.colorize("&3&lPICK THIS DIAMOND!!"));
			}
			else if(counter >= 10 && counter < 15) {
				itemline.setItemStack(new ItemStack(Material.EMERALD));
				text.setText(Utils.colorize("&3&lPICK THIS EMERALD!!"));
			}
			else if(counter >= 15 && counter < 25) {
				itemline.setItemStack(new ItemStack(Material.YELLOW_GLAZED_TERRACOTTA));
				text.setText(Utils.colorize("&3&lPICK THIS VALUE BLOCK!!"));
			}
			else if(counter >= 25) {
				itemline.setItemStack(new ItemStack(Material.BEACON));
				text.setText(Utils.colorize("&3&lPICK THIS VALUE BLOCK!!"));
			}
			
			
			//if its the last waypoint, set it as a nether star
			if(locationsToSpawn.size() == 0) {
				itemline.setItemStack(new ItemStack(Material.NETHER_STAR));
				text.setText(Utils.colorize("&3&lFINISH!!"));
			}
			//if there is no more waypoint location left it means we have finished
			if(locToTeleport == null) {
				endParkour(player1, hologram, true);
				return;
			}

			hologram.teleport(locToTeleport);
			
			runningTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> update(player1, hologram), 0L , 20L);
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
		this.endTime = System.currentTimeMillis();
		player.sendMessage("You finished the parkour in " + ((endTime - startTime) / 1000.0) + " seconds.");
		plugin.getActionBar().removeCustomActionBar(player.getUniqueId());
		if(runningTask != null) runningTask.cancel();
		hologram.delete();
		Parkour.parkourParticipants.remove(player);
	}

	
}
