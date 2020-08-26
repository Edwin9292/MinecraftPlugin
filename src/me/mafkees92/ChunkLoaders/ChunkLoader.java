package me.mafkees92.ChunkLoaders;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.mafkees92.Utils.Utils;

public class ChunkLoader {

	String locationSeparator = "|";
	
	private final Location location;
	private final Chunk chunk;
	private final Location hologramLocation;
	
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
		List<String> lore = new ArrayList<>();
		lore.add(Utils.colorize("&7When placed, this block will keep"));
		lore.add(Utils.colorize("&7its chunk loaded if nobody"));
		lore.add(Utils.colorize("&7is there to load it."));
		lore.add(Utils.colorize("&o"));
		lore.add(Utils.colorize("&d&lEPIC"));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunkloader", "loader");
		return item;
	}
	
	
	
	@Override
	public String toString() {

		String sb = location.getWorld().getName() +
				locationSeparator +
				location.getBlockX() +
				locationSeparator +
				location.getBlockY() +
				locationSeparator +
				location.getBlockZ();
		return sb;
	}

	
}
