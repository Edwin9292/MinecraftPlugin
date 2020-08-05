package me.mafkees92.CustomHoppers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class HopperEvents implements Listener{
	
	private Main plugin;
	public HopperEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler
	public void HopperPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)){
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			String tag = Utils.getNBTTag(item, "chunkhopper");
			if(tag != null || tag != "") {
				CustomHopper hopper = new CustomHopper(event.getBlock().getLocation());
				plugin.getHopperData().AddHopperData(hopper);
				event.getPlayer().sendMessage("added custom hopper");
			}
		}
	}
	
	@EventHandler
	public void HopperBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)) {
			if(plugin.getHopperData().isChunkHopper(event.getBlock().getLocation())) {
				if(plugin.getHopperData().removeHopper(event.getBlock().getLocation())) {
					event.getPlayer().sendMessage("Removed custom hopper");
				}
			}
		}
	}
}
