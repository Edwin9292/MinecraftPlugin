package me.mafkees92.CustomHoppers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Holograms;
import me.mafkees92.Main;
import me.mafkees92.Files.HopperLocations;
import me.mafkees92.Utils.Utils;

public class CustomHoppers {
	
	Main plugin;
	HopperLocations hopperLocations;
	
	public CustomHoppers(Main plugin) {
		this.plugin = plugin;
		
		loadHoppers();
	}
	
	private void loadHoppers() {
		hopperLocations = new HopperLocations(plugin, "HopperData.yml");
		List<ChunkHopper> hoppers = hopperLocations.getAllChunkHoppers();
		for(ChunkHopper hopper : hoppers){
			//hopperHolograms.LoadHopperHologram(hopper);
			Holograms.AddHologram(hopper.getHologramLocation().clone().add(0,0.29d,0), new ItemStack(Material.HOPPER),  Utils.colorize("&b&lChunk Hopper"), "");
		}
	}

	public void onDisable() {
		hopperLocations.save();
		Holograms.RemoveAllHolograms();
	}
	
	public void addHopper(ChunkHopper hopper) {
		hopperLocations.addHopper(hopper);
		Holograms.AddHologram(hopper.getHologramLocation().clone().add(0,0.29d,0), new ItemStack(Material.HOPPER),  Utils.colorize("&b&lChunk Hopper"), "");
	}
	
	public boolean removeHopper(ChunkHopper hopper) {
		if(hopperLocations.removeHopper(hopper)) {
			Holograms.RemoveHologram(hopper.getHologramLocation().clone().add(0,0.29d,0));
			return true;
		}
		return false;
	}
	
	public boolean isChunkHopper(Location location) {
		return hopperLocations.isCustomHopper(location);
	}
	
	public List<ChunkHopper> getHoppersInChunk(Location location){
		return hopperLocations.getHoppersInChunk(location);
	}
	

	public static ItemStack getChunkHopperItemStack() {

		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colorize("&o&3&lChunkHopper"));
		List<String> lore = new ArrayList<>();
		lore.add(Utils.colorize("&7When placed, this hopper will suck"));
		lore.add(Utils.colorize("&7up every item that drops inside"));
		lore.add(Utils.colorize("&7its chunk."));
		lore.add(Utils.colorize("&o"));
		lore.add(Utils.colorize("&d&lEPIC"));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunkhopper", "hopper");
		return item;
	}
}


