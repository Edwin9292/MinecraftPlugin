package me.mafkees92.ActionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import bin.com.wasteofplastic.askyblock.events.IslandEnterEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements Listener {

	Plugin plugin;
	private HashMap<UUID, String> customizedMessages = new HashMap<UUID, String>();
	private ASkyBlockAPI api = ASkyBlockAPI.getInstance();
	private ArrayList<String> disabledWorlds = new ArrayList<>(
		Arrays.asList("build", "spleef", "bedwars", "event"));
	private boolean isPlaceholderAPIEnabled = false;

	public ActionBar(Main plugin) {
		this.plugin = plugin;
		runActionBarScheduler();
		isPlaceholderAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	
	public void setCustomActionBar(UUID uuid, String message) {
		this.customizedMessages.put(uuid, message);
	}
	public void removeCustomActionBar(UUID uuid) {
		this.customizedMessages.remove(uuid);
	}
	
	
	private void runActionBarScheduler() {
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			String message;
			for (Player player : Bukkit.getOnlinePlayers()) {
				//disable the action bar for certain worlds
				String worldName = player.getWorld().getName().toLowerCase();
				if(!this.disabledWorlds.contains(worldName)) {
					if (isPlaceholderAPIEnabled) {
						UUID playerUUID = player.getUniqueId();
						if(customizedMessages.containsKey(playerUUID)) {
							message = customizedMessages.get(playerUUID);
						}
						else if(api.hasIsland(playerUUID) || api.inTeam(playerUUID)) {
							UUID islandLeaderUUID = Utils.getTeamOrIslandOwner(playerUUID);
							message = Messages.actionBar
									.replace("{island_level}", "" + api.getLongIslandLevel(islandLeaderUUID))
									.replace("{mana}", "" + AureliumAPI.getMana(player))
									.replace("{max_mana}", "" + AureliumAPI.getMaxMana(player))
									.replace("{max_hp}", "" + Math.round((player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 5)))
									.replace("{hp}", "" + (int)(Math.ceil(player.getHealth() * 5)));
						}
						else {
							message = Messages.actionBarNoIsland
									.replace("{max_hp}", "" + Math.round((player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 5)))
									.replace("{hp}", "" + (int)(Math.ceil(player.getHealth() * 5)))
									.replace("{mana}", "" + AureliumAPI.getMana(player))
									.replace("{max_mana}", "" + AureliumAPI.getMaxMana(player));
						}
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
					}
				}
			}
		}, 10L, 5L); // will run 2x a second.

	}

	@EventHandler
	public void enterIslandEvent(IslandEnterEvent event) {
		//on entering island
		Island island = event.getIsland();
		if(island.isSpawn()) {
			Main.getInstance().getActionBar().setCustomActionBar(event.getPlayer(), Messages.actionBarSpawnEnter);
		}
		else {
			UUID teamLeader = Utils.getTeamOrIslandOwner(event.getPlayer());
			//the player has an island(or is in a team) and his island has the same owner as the event island
			if(teamLeader != null && teamLeader.equals(event.getIsland().getOwner())) {
				this.setCustomActionBar(event.getPlayer(), Messages.actionBarIslandEnterOwnIsland);
			}
			else {
				this.setCustomActionBar(event.getPlayer(), Messages.actionBarIslandEnter.
						replace("[islandname]", ASkyBlockAPI.getInstance().getIslandName(event.getIslandOwner())));
			}
			
		}
		
		//remove island visit message after 4 seconds
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			Main.getInstance().getActionBar().removeCustomActionBar(event.getPlayer());
		}, 80);
	}
}
