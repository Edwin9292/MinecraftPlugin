package me.mafkees92.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import me.mafkees92.Utils.Utils;

public class PlayerPortalEventListener implements Listener{
	
	ASkyBlockAPI aSkyBlock = ASkyBlockAPI.getInstance();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerPortalTeleport(PlayerTeleportEvent event) {
		if(event.getCause() == TeleportCause.PLUGIN){
			if(event.getTo().getWorld().getName().contentEquals("SkyBlock_nether")) {
				Island island = aSkyBlock.getIslandAt(event.getFrom());
				if(island.isSpawn()) {
					return;
				}
				int islandLevel = aSkyBlock.getIslandLevel(island.getOwner());
				
				if(islandLevel < 100) {
					//if the player is the owner or a member of the island, send this message
					if(island.getOwner() == event.getPlayer().getUniqueId() || island.getMembers().contains(event.getPlayer().getUniqueId())) {
						event.getPlayer().sendMessage(Utils.colorize("&cYour island has to be level 100 or higher to visit the nether!"));
						event.setCancelled(true);
					}
					else{
						event.getPlayer().sendMessage(Utils.colorize("&cThis islands level is not high enough to have access to the nether yet!"));
						event.setCancelled(true);
					}
					event.getPlayer().teleport(event.getPlayer().getLocation().add(0, 1, 0));
				}
			}
		}
	}
}
