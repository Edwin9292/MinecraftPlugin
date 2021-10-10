package me.mafkees92.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class PlayerTeleportListener implements Listener{

	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(!event.isCancelled()) {
			event.getPlayer().setFallDistance(0);
			event.getPlayer().setVelocity(new Vector(0,0,0));
		}
	}
}
