package me.mafkees92.ChunkLoaders;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class ChunkLoaderEvents implements Listener{

	Main plugin;
	HeadDatabaseAPI api;
	
	public ChunkLoaderEvents(Main plugin) {
		this.plugin = plugin;
		this.api = new HeadDatabaseAPI();
		
	}
	
	@EventHandler
	public void chunkUnloadEvent(ChunkUnloadEvent event) {
		if(plugin.getChunkLoadersInstance().containsChunkLoader(event.getChunk())) {
			Bukkit.broadcastMessage("Keeping a chunk loaded at X:" + event.getChunk().getX() + " Z:" + event.getChunk().getZ());
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void chunkLoaderPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.SKULL)){
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			String skullId = api.getItemID(item);
			if(skullId.contentEquals("25842")) {
				Bukkit.getPlayer("Mafkees92").sendMessage("this is the right skull");
				String nbt = Utils.getNBTTag(item, "chunkloader");
				if(nbt == "loader") {
					plugin.getChunkLoadersInstance().addChunkLoader(new ChunkLoader(event.getBlock().getLocation()));
					Bukkit.getPlayer("Mafkees92").sendMessage("added chunkloader");
					return;
				}
				Bukkit.getPlayer("Mafkees92").sendMessage("no nbt tag");
			}
		}
	}
	
	
	@EventHandler
	public void chunkLoaderBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.SKULL)) {
			if(plugin.getChunkLoadersInstance().isChunkLoader(event.getBlock().getLocation())){
				plugin.getChunkLoadersInstance().removeChunkLoader(new ChunkLoader(event.getBlock().getLocation()));		
				event.setDropItems(false);
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), ChunkLoader.CreateChunkLoaderItem());
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
