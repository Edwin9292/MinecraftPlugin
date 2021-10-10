package me.mafkees92.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.mafkees92.Main;
import me.mafkees92.HologramParkour.Parkour;
import me.mafkees92.Utils.Utils;

public class BlockPlaceListener implements Listener{
	
	ArrayList<Player> levelBlockMessageCooldownPlayers = new ArrayList<Player>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		//if the blockplace is cancelled (most likely by worldguard), check if the player is in parkour.
		//if the player is in parkour, he might be trying to block glitch, so teleport him back to his old location.
		if(event.isCancelled()) {
			if(Parkour.parkourParticipants.containsKey(player)){
				player.teleport(player.getLocation());
				return;
			}
		}
		
		//check if it was a level block that has been placed.
		Block block = event.getBlock();
		if((block.getType().getId() >= 235 && 
				block.getType().getId() <= 250) || 
				block.getType() == Material.BEACON || 
				block.getType() == Material.MAGMA ||
				block.getType() == Material.EMERALD_BLOCK ||
				block.getType() == Material.DIAMOND_BLOCK ||
				block.getType() == Material.GOLD_BLOCK ||
				block.getType() == Material.IRON_BLOCK) {
			
			//if the player mined a levelblock, send him a message
			if(!levelBlockMessageCooldownPlayers.contains(player)) {
				player.sendMessage(Utils.colorize("&7You have placed a level block! "
						+ "In order to update your island level, type the command &e/is level&7."));
				
				//add the player to a cooldown list
				this.levelBlockMessageCooldownPlayers.add(player);
				
				//make sure the player gets removed from the cooldown after 30 seconds.
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
					this.levelBlockMessageCooldownPlayers.remove(player);
				}, 600);
			}
		}
	}
}
