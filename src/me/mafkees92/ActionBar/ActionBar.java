package me.mafkees92.ActionBar;

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

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
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
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
						&& Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					try {
						String message;
						if(ASkyBlockAPI.getInstance().hasIsland(player.getUniqueId()) ||
								ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
							message = PlaceholderAPI.setPlaceholders(player, Messages.ActionBarMessages.message);
						}
						else {
							message = PlaceholderAPI.setPlaceholders(player, Messages.ActionBarMessages.messageNoIsland);
						}

						message = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, message);
						
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 5L, 5L); // will run 4x a second. Idk if it's needed to run it this often.

	}

}
