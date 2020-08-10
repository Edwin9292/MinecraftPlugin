package me.mafkees92.IslandChests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;

import me.mafkees92.Main;
import me.mafkees92.Files.IslandChest;
import me.mafkees92.Utils.Utils;

public class IslandChests implements Listener {

	private HashMap<String, IslandChest> loadedIslandChests = new HashMap<String, IslandChest>();
	
	Main plugin;
	ASkyBlockAPI skyBlockApi;
	
	public IslandChests(Main plugin) {
		this.plugin = plugin;
		skyBlockApi = ASkyBlockAPI.getInstance();
		
	}
	
	public IslandChest loadIslandChest(UUID islandOwner) {
		if(skyBlockApi.hasIsland(islandOwner)) {
			IslandChest isChest = new IslandChest(plugin, "islandChests/" + islandOwner + ".yml", islandOwner);
			loadedIslandChests.put(islandOwner.toString(), isChest);
			return isChest;
		}
		return null;
	}
	
	
	public void unloadIslandChest(UUID islandOwner) {
		if(loadedIslandChests.containsKey(islandOwner.toString())){
			loadedIslandChests.get(islandOwner.toString()).saveIslandChest();
			loadedIslandChests.get(islandOwner.toString()).unload();
			loadedIslandChests.remove(islandOwner.toString());
			Bukkit.getLogger().info("Unloaded islandchest");
		}
	}
	
	public IslandChest getIslandChest(UUID islandOwner) {
		if(loadedIslandChests.containsKey(islandOwner.toString())){
			return loadedIslandChests.get(islandOwner.toString());
		}
		else if(loadIslandChest(islandOwner) != null) {
			return loadedIslandChests.get(islandOwner.toString());
		}
		return null;
	}
	
	public void saveChests() {
		for(IslandChest chest : loadedIslandChests.values()) 
		    chest.saveIslandChest();
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event) {
		if(event.getInventory().getType().equals(InventoryType.CHEST)){
			if(event.getInventory().getName().contentEquals(Utils.colorize("&6&lTOOT&e&lMC &f| &eIsland Chest"))){
				if(skyBlockApi.inTeam(event.getPlayer().getUniqueId())){
					UUID islandOwner = skyBlockApi.getTeamLeader(event.getPlayer().getUniqueId());
					if(loadedIslandChests.containsKey(islandOwner.toString())) {
						loadedIslandChests.get(islandOwner.toString()).saveIslandChest();
					}
				}
				else {
					if(skyBlockApi.hasIsland(event.getPlayer().getUniqueId())){
						if(loadedIslandChests.containsKey(event.getPlayer().getUniqueId().toString())){
							loadedIslandChests.get(event.getPlayer().getUniqueId().toString()).saveIslandChest();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void changeOwnershipEvent(IslandChangeOwnerEvent event) {
		UUID oldOwner = event.getOldOwner();
		UUID newOwner = event.getNewOwner();
		IslandChest chest = getIslandChest(oldOwner);
		if(chest != null) {
			IslandChest newOwnerChest = getIslandChest(newOwner);
			if(newOwnerChest != null) {
				newOwnerChest.setInventorySize(chest.getInventory().getSize());
				newOwnerChest.setInventoryContents(chest.getInventory().getContents());
				chest.getInventory().clear();
				chest.setInventorySize(9);
				chest.saveIslandChest();
				unloadIslandChest(oldOwner);
			}
			
		}
	}
	
	@EventHandler
	public void playerLogOff(PlayerQuitEvent event) {
		UUID player = event.getPlayer().getUniqueId();
		Iterator<UUID> it = ASkyBlockAPI.getInstance().getTeamMembers(player).iterator();
		boolean found = false;
		//check if this player has a team and if one of the team members is still online
		while(it.hasNext()) {
			UUID teamMember = it.next();
			if(teamMember.equals(event.getPlayer().getUniqueId()))
				continue;
			if(Bukkit.getOfflinePlayer(teamMember).isOnline()) 
				continue;
			found = true;
		}
		if(found) {
			if(ASkyBlockAPI.getInstance().inTeam(player)){
				this.unloadIslandChest(ASkyBlockAPI.getInstance().getTeamLeader(player));
				return;
			}
			this.unloadIslandChest(event.getPlayer().getUniqueId());
			return;
		}
	}

	@EventHandler
	public void overrideIsChestCommand(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().startsWith("/is chest") || event.getMessage().startsWith("/island chest")) {
			event.setMessage("/islandchest");
		}
	}

	@EventHandler
	public void overrideIsInvseeCommand(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().startsWith("/is invsee")) {
			event.setMessage(event.getMessage().replace("/is invsee", "/islandinvsee"));
		}
		if(event.getMessage().startsWith("/island invsee")) {
			event.setMessage(event.getMessage().replace("/island invsee", "/islandinvsee"));
		}
	}
}
