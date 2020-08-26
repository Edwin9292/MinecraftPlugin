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


import me.mafkees92.Utils.Utils;

public class ChunkHopper {

	String locationSeparator = "|";
	
	private final Location location;
	private final Chunk chunk;
	private final Location hologramLocation;
	
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
	
	
	
	@Override
	public String toString() {

		return location.getWorld().getName() +
				locationSeparator +
				location.getBlockX() +
				locationSeparator +
				location.getBlockY() +
				locationSeparator +
				location.getBlockZ();
	}

	
}
