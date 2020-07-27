package me.mafkees92.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements Listener {

	Plugin plugin;
	private List<UUID> toSend = new ArrayList<>();

	public ActionBar(Main plugin) {
		this.plugin = plugin;
		runActionBarScheduler();
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		toSend.add(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		toSend.remove(event.getPlayer().getUniqueId());
	}

	private void runActionBarScheduler() {
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
						&& Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					try {
						int healthInt = ((int) Math.ceil(p.getHealth()))*5;

						String rating = PlaceholderAPI.setPlaceholders(p, "%islandrate_average_rating%").substring(0, 1);
						String islandLevel = PlaceholderAPI.setPlaceholders(p, "%askyblock_level%");
						
						int ratingint = Integer.parseInt(rating);

						String message = "&c " + healthInt + "/100❤&r     &a" + islandLevel + "〣 Level     &e" + ratingint + "/5✰ Rating ";
						

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								TextComponent.fromLegacyText(Utils.colorize(message)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 5L, 5L); // will run 4x a second. Idk if it's needed to run it this often.

	}

}
