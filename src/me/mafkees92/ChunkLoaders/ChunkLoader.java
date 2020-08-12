package me.mafkees92.ChunkLoaders;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.util.Util;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.mafkees92.Utils.Utils;

public class ChunkLoader {

	String locationSeparator = "|";
	
	private Location location;
	private Chunk chunk;
	private Location hologramLocation;
	
	public ChunkLoader(Location location) {
		this.location = location;
		this.chunk = location.getChunk();
		this.hologramLocation = location.clone().add(0.5D, 1.7D, 0.5D);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public Chunk getChunk() {
		return this.chunk;
	}
	
	
	public String getChunkLocationString() {
		return location.getBlockX()/16 + ":" + location.getBlockZ()/16;
	}
	
	
	public Location getHologramLocation() {
		return this.hologramLocation;
	}
	
	
	public static ItemStack CreateChunkLoaderItem() {

		ItemStack item = new HeadDatabaseAPI().getItemHead("25842");
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colorize("&o&3&lChunkLoader"));
		List<String> lore = new ArrayList<String>();
		lore.add(Util.colorize("&7When placed, this block will keep"));
		lore.add(Util.colorize("&7its chunk loaded if nobody"));
		lore.add(Util.colorize("&7is there to load it."));
		lore.add(Util.colorize("&o"));
		lore.add(Util.colorize("&d&lEPIC"));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunkloader", "loader");
		return item;
	}
	
	
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(location.getWorld().getName());
		sb.append(locationSeparator);
		sb.append(location.getBlockX());
		sb.append(locationSeparator);
		sb.append(location.getBlockY());
		sb.append(locationSeparator);
		sb.append(location.getBlockZ());
		
		return sb.toString();
	}

	
}
