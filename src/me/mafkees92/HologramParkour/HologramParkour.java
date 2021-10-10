package me.mafkees92.HologramParkour;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.Main;
import me.mafkees92.Files.Messages;

public class HologramParkour {
	
	Main plugin;
	Queue<Location> locationsToSpawn;
	public Location previousLocation;
	public Location twoStepsBack = null;
	private Location locToTeleport;
	int counter = 0;
	int itemDisplayedCounter = 0;
	BukkitTask runningTask;
	int timer = 0;
	private long startTime = -1;
	private long endTime = -1;
	double moneyWon = 0;
	
	ParkourItem itemToDisplay;
	
	public HologramParkour(Parkour parkour, String parkourName, LinkedList<Location> waypoints, Player player, Main plugin) {
		
		this.locationsToSpawn = waypoints;
		this.plugin = plugin;
		Iterator<ParkourItem> it = Messages.parkourItems.iterator();
		itemToDisplay = it.next();
		timer = itemToDisplay.timeToPickup;
		
		//setup the hologram
		Parkour.parkourParticipants.put(player, this);
		Hologram hologram = HologramsAPI.createHologram(plugin, previousLocation = locationsToSpawn.poll());
		TextLine text = hologram.appendTextLine(itemToDisplay.hologramMessage);
		ItemLine itemline = hologram.appendItemLine(new ItemStack(itemToDisplay.material));

		//setup the hologram visibility
		hologram.getVisibilityManager().setVisibleByDefault(false);
		hologram.getVisibilityManager().showTo(player);
		
		//setup what happends if a hologram is picked up
		PickupHandler handler = player1 -> {
			if(startTime == -1)
				startTime = System.currentTimeMillis();
			if(runningTask != null) runningTask.cancel();
			counter ++;
			itemDisplayedCounter++;
			
			if(locToTeleport != null) {
				twoStepsBack = previousLocation;
				previousLocation = locToTeleport;
			}
			
			locToTeleport = locationsToSpawn.poll();

			//pay the reward if there is any
			if(itemToDisplay.pickUpReward > 0) {
				Main.econ.depositPlayer(player, itemToDisplay.pickUpReward);
				player.sendMessage(Messages.parkourWaypointRewardMessage.replace("%reward%", ""+ (int)itemToDisplay.pickUpReward));
				this.moneyWon += itemToDisplay.pickUpReward;
			}
			
			
			//set the item based on the amount of items pickedup
			if(itemDisplayedCounter >= itemToDisplay.timesToDisplay && it.hasNext()) {
				itemDisplayedCounter = 0;
				itemToDisplay = it.next();
				itemline.setItemStack(new ItemStack(itemToDisplay.material));
				text.setText(itemToDisplay.hologramMessage);
			}
			
			//if its the last waypoint, set it as a nether star
			if(locationsToSpawn.size() == 0) {
				itemToDisplay = Messages.finishItem;
				itemline.setItemStack(new ItemStack(Messages.finishItem.material));
				text.setText(Messages.finishItem.hologramMessage);
			}
			
			//if there is no more waypoint location left it means we have finished
			if(locToTeleport == null) {
				endParkour(player1, parkourName, hologram, true);
				parkour.setLastWon(parkourName, LocalDateTime.now(ZoneOffset.UTC));
				return;
			}
			
			timer = itemToDisplay.timeToPickup;
			hologram.teleport(locToTeleport);
			
			runningTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> update(player1, parkourName, hologram), 0L , 20L);
		};
		itemline.setPickupHandler(handler);
	}
	
	private void update(Player player, String parkourName, Hologram hologram) {
		if(!player.isOnline()) {
			if(runningTask!=null) runningTask.cancel();
			return;
		}
		if(timer > 0) {
			plugin.getActionBar().setCustomActionBar(player.getUniqueId(), 
					Messages.timeLeftTillWaypoint.replace("%timer%", "" + timer)
						.replace("%pickedup%", ""+counter)
						.replace("%totalwaypoints%", ""+(locationsToSpawn.size()+counter +1))
					,20);
			timer--;
		}
		else {
			endParkour(player, parkourName, hologram, false);
		}
	}
	
	private void endParkour(Player player, String parkourName, Hologram hologram, boolean finished) {
		this.endTime = System.currentTimeMillis();
		if(finished) {
			
			player.sendMessage(Messages.pickedUpLastWaypoint);
			player.sendMessage(Messages.parkourWonMessage.replace("%time%", ""+(endTime - startTime) / 1000.0));
			Bukkit.broadcastMessage(Messages.parkourWonBroadcastMessage
					.replace("%player%", player.getName())
					.replace("%parkourname%", parkourName));
		}
		else {
			player.sendMessage(Messages.parkourTimeRanOut);
			player.sendMessage(Messages.parkourLostMessage.replace("%score%", ""+counter)
					.replace("%time%", ""+(endTime - startTime) / 1000.0));
		}
		if(moneyWon > 0) {
			player.sendMessage(Messages.totalMoneyWonMessage.replace("%money%", ""+ (int)moneyWon));
		}
		
		if(runningTask != null) runningTask.cancel();
		hologram.delete();
		Parkour.parkourParticipants.remove(player, this);
	}

	
}
