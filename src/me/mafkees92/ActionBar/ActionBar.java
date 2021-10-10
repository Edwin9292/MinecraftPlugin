package me.mafkees92.ActionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.api.event.XpGainEvent;
import com.archyx.aureliumskills.leveler.Leveler;
//import com.archyx.aureliumskills.api.event.XpGainEvent;
//import com.archyx.aureliumskills.leveler.Leveler;
//import com.archyx.aureliumskills.leveler.SkillLeveler;
import com.archyx.aureliumskills.skills.Skill;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import bin.com.wasteofplastic.askyblock.events.IslandEnterEvent;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements Listener {

	Plugin plugin;
	
	private HashMap<UUID, String> customizedMessages = new HashMap<UUID, String>();
	private HashMap<UUID, BukkitTask> customizedMessagesTimerTasks = new HashMap<UUID, BukkitTask>();
	
	private ASkyBlockAPI api = ASkyBlockAPI.getInstance();
	private ArrayList<String> disabledWorlds = new ArrayList<>(
		Arrays.asList("build", "spleef", "bedwars1", "event"));
	
	private boolean isPlaceholderAPIEnabled = false;

	public ActionBar(Main plugin) {
		this.plugin = plugin;
		runActionBarScheduler();
		isPlaceholderAPIEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	
	public void setCustomActionBar(UUID uuid, String message, int durationInTicks) {
		this.customizedMessages.put(uuid, message);
		
		if(customizedMessagesTimerTasks.containsKey(uuid)) {
			customizedMessagesTimerTasks.get(uuid).cancel();
		}
		
		BukkitTask removeMessageTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
			this.removeCustomActionBar(uuid);
			this.customizedMessagesTimerTasks.remove(uuid);
		}, durationInTicks);
		
		customizedMessagesTimerTasks.put(uuid, removeMessageTask);
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
									.replace("{island_level}", "" + api.getLongIslandLevel(islandLeaderUUID));
						}
						else {
							message = Messages.actionBarNoIsland;
						}
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message
								.replace("{max_hp}", "" + (int)Math.ceil((Math.round((player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 5) * 10d)/10d)))
								.replace("{hp}", "" + (int)(Math.ceil(Math.round((player.getHealth() * 5) * 10d)/10d)))
								.replace("{mana}", ""+ (int)AureliumAPI.getMana(player))
								.replace("{max_mana}", "" + (int)AureliumAPI.getMaxMana(player))
								));
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
			Main.getInstance().getActionBar().setCustomActionBar(event.getPlayer(), Messages.actionBarSpawnEnter, 80);
		}
		else {
			UUID teamLeader = Utils.getTeamOrIslandOwner(event.getPlayer());
			//the player has an island(or is in a team) and his island has the same owner as the event island
			if(teamLeader != null && teamLeader.equals(event.getIsland().getOwner())) {
				this.setCustomActionBar(event.getPlayer(), Messages.actionBarIslandEnterOwnIsland, 80);
			}
			else {
				String islandName = ASkyBlockAPI.getInstance().getIslandName(event.getIslandOwner());
				if(ChatColor.stripColor(islandName).equalsIgnoreCase(ChatColor.stripColor(Bukkit.getOfflinePlayer(event.getIslandOwner()).getName()))) {
					islandName += "&e's";
				}
				this.setCustomActionBar(event.getPlayer(), Messages.actionBarIslandEnter.
						replace("[islandname]", Utils.colorize("&e"+islandName)), 80);
			}
		}
	}
	
	
	//'&c {hp}/{max_hp}❤      +{exp} {skill} XP ({current_exp}/{exp_to_next_level})     &e {mana}/{max_mana}✤ Mana'
	@EventHandler
	public void skillExpGainEvent(XpGainEvent event) {
//	public void skillExpGainEvent(com.archyx.aureliumskills.api.XpGainEvent event) {
		DecimalFormat df = new DecimalFormat("#.##"); 
		double expGained = event.getAmount();
		
		Skill skill = event.getSkill();
		String skillName = skill.getDisplayName(new Locale(event.getPlayer().getLocale()));
//		String skillName = skill.getDisplayName();
		
		int level = AureliumAPI.getSkillLevel(event.getPlayer(), skill);
		double currentExp = AureliumAPI.getXp(event.getPlayer(), skill) + event.getAmount();
		double xpToNext;
//		Leveler leveler = new Leveler((AureliumSkills) Main.getInstance().getServer().getPluginManager().getPlugin("AureliumSkills"));
		AureliumSkills skills = ((AureliumSkills) Main.getInstance().getServer().getPluginManager().getPlugin("AureliumSkills"));
		Leveler leveler = skills.getLeveler();
		if (leveler.getLevelRequirements().size() > level - 1) {
			xpToNext = leveler.getLevelRequirements().get(level - 1);
			if(currentExp >= xpToNext){
				currentExp -= xpToNext;
				xpToNext = leveler.getLevelRequirements().get(level);
			}
		} else {
			xpToNext = 0;
		}

//		if (com.archyx.aureliumskills.skills.levelers.Leveler.levelReqs.size() > level - 1) {
//			xpToNext = com.archyx.aureliumskills.skills.levelers.Leveler.levelReqs.get(level - 1);
//		} else {
//			xpToNext = 0;
//		}
		
		String message = Messages.actionBarSkillExpGained
				.replace("{exp}", df.format(expGained))
				.replace("{skill}", skillName)
				.replace("{current_exp}", df.format(currentExp))
				.replace("{exp_to_next_level}", "" + (int)xpToNext);
		
		this.setCustomActionBar(event.getPlayer().getUniqueId(), message, 50);
	}
	
	
	
}
