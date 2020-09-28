package me.mafkees92.ActionBar;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.wasteofplastic.askyblock.Island;

import bin.com.wasteofplastic.askyblock.events.IslandEnterEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements Listener {

	Plugin plugin;
	private List<UUID> toSend = new ArrayList<>();
	private HashMap<UUID, String> customizedMessages = new HashMap<UUID, String>();

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
	
	public void setCustomActionBar(UUID uuid, String message) {
		this.customizedMessages.put(uuid, message);
	}
	public void removeCustomActionBar(UUID uuid) {
		this.customizedMessages.remove(uuid);
	}
	
	

	private void runActionBarScheduler() {
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")
						&& Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					try {
						String message;
						if(customizedMessages.containsKey(player.getUniqueId())) {
							message = customizedMessages.get(player.getUniqueId());
						}
						else if(ASkyBlockAPI.getInstance().hasIsland(player.getUniqueId()) ||
								ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
							message = PlaceholderAPI.setPlaceholders(player, Messages.actionBar);
						}
						else {
							message = PlaceholderAPI.setPlaceholders(player, Messages.actionBarNoIsland);
						}
						
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 10L, 10L); // will run 2x a second. Idk if it's needed to run it this often.

	}

	@EventHandler
	public void enterIslandEvent(IslandEnterEvent event) {
		//on entering island
		Island island = event.getIsland();
		if(island.isSpawn()) {
			Main.getInstance().getActionBar().setCustomActionBar(event.getPlayer(), Messages.actionBarSpawnEnter);
		}
		else {
			Main.getInstance().getActionBar().setCustomActionBar(event.getPlayer(), Messages.actionBarIslandEnter.
					replace("[islandname]", ASkyBlockAPI.getInstance().getIslandName(event.getIslandOwner())));
		}
		
		//remove island visit message after 4 seconds
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			Main.getInstance().getActionBar().removeCustomActionBar(event.getPlayer());
		}, 80);
	}
}
