package me.mafkees92.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class PlayerLoginListener implements Listener{

	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("group.rank4") && !player.hasPermission("group.mod")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Bukkit.broadcastMessage(Utils.colorize(player.getDisplayName() + " &7has just come online!"));
				}
			}.runTaskLater(Main.getInstance(), 20);
		}
	}
}
