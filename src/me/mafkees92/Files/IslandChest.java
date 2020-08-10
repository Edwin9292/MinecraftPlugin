package me.mafkees92.Files;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class IslandChest extends BaseFile{

	private ItemStack[] items;
	Inventory chestInventory;
	UUID islandOwner;
	int chestSize;
	
	public IslandChest(Main plugin, String fileName, UUID islandOwner) {
		super(plugin, fileName);
		this.islandOwner = islandOwner;
		
		//on creation of island chest instance
		
		loadIslandChest();
	}

	// load data from yml file
	@SuppressWarnings("unchecked")
	private void loadIslandChest() {
		this.chestSize = config.getInt("chestSize", 9);
		chestInventory = Bukkit.createInventory(null, chestSize, Utils.colorize("&6&lTOOT&e&lMC &f| &eIsland Chest"));

		Object obj = config.get("inventory");
		if(obj == null) return;
		this.items = ((List<ItemStack>)config.get("inventory")).toArray(new ItemStack[0]);
		
		chestInventory.setContents(items);
	}
	
	public void unload() {
		this.items = null;
		this.chestInventory = null;
		this.islandOwner = null;
	}
	
	
	// save data to yml file
	public void saveIslandChest() {
		config.set("inventory", chestInventory.getContents());
		config.set("chestSize", this.chestSize);
		save();
	}
	
	public Inventory getInventory() {
		return this.chestInventory;
	}
	
	public void setInventoryContents(ItemStack[] items) {
		this.chestInventory.setContents(items);;
		saveIslandChest();
	}
	
	public boolean setInventorySize(int size) {
		if(size % 9 != 0) {
			return false;
		}
		if(size > 54)
			return false;
		this.chestSize = size;
		Inventory newInventory = Bukkit.createInventory(null, chestSize, Utils.colorize("&6&lTOOT&e&lMC &f| &eIsland Chest"));
		newInventory.setContents(this.chestInventory.getContents());
		this.chestInventory = newInventory;
		saveIslandChest();
		return true;
	}
	
	
}
