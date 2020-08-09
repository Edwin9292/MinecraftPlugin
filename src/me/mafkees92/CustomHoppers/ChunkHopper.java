package me.mafkees92.CustomHoppers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.util.Util;

import me.mafkees92.Utils.Utils;

public class ChunkHopper {

	String locationSeparator = "|";
	
	private Location location;
	private Chunk chunk;
	private Location hologramLocation;
	
	public ChunkHopper(Location location) {
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
	
	public Hopper getHopper() {
		return (Hopper) location.getBlock().getState();
	}
	
	public Inventory getInventory() {
		return this.getHopper().getInventory();
	}
	
	public String getChunkLocationString() {
		return location.getBlockX()/16 + ":" + location.getBlockZ()/16;
	}
	
	public void updateInventoryViewers() {
		for (HumanEntity entity : getInventory().getViewers()) {
			Player p = (Player)entity;
			p.updateInventory();
		}
	}
	
	public Location getHologramLocation() {
		return this.hologramLocation;
	}
	
	
	public static ItemStack CreateChunkHopperItem() {

		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colorize("&o&3&lChunkHopper"));
		List<String> lore = new ArrayList<String>();
		lore.add(Util.colorize("&7When placed, this hopper will suck"));
		lore.add(Util.colorize("&7up every item that drops inside"));
		lore.add(Util.colorize("&7its chunk."));
		lore.add(Util.colorize("&o"));
		lore.add(Util.colorize("&d&lEPIC"));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunkhopper", "hopper");
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
