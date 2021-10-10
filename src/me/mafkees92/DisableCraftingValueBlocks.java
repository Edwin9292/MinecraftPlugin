package me.mafkees92;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DisableCraftingValueBlocks implements Listener{

	Main plugin;
	
	public DisableCraftingValueBlocks(Main plugin) {
		this.plugin = plugin;
	}

    
	@EventHandler
	public void CraftValueBlock(CraftItemEvent event) {
		Material result = event.getRecipe().getResult().getType();
		if(result.equals(Material.BEACON) ||
				result.equals(Material.MAGMA)) {
			event.setCancelled(true);
		}
	}
	
    @EventHandler
    public void preventPuttingInFurnace(InventoryClickEvent event) {
    	if(event.getInventory().getType().equals(InventoryType.FURNACE)) {
    		ItemStack item = event.getCurrentItem();
    		if(item != null && item.getType().equals(Material.STAINED_CLAY)) {
    			int data = item.getDurability();
    			if(data == 3 || data == 4 || data == 5 || data == 11) {
    				event.setCancelled(true);
    			}
    		}
    	}
    }
    
    @EventHandler
    public void preventHopperToFurnace(InventoryMoveItemEvent event) {
    	if(event.getDestination().getType().equals(InventoryType.FURNACE)){
    		if(event.getSource().getType().equals(InventoryType.HOPPER)) {
    			ItemStack itemToMove = event.getItem();
    			if(itemToMove.getType().equals(Material.STAINED_CLAY)) {
    				int data = itemToMove.getDurability();
    				if(data == 3 || data == 4 || data == 5 || data == 11) {
    					event.setCancelled(true);
    				}
    			}
    		}
    	}
    }
    
    @EventHandler
    public void smeltLevelBlock(FurnaceSmeltEvent event) {
    	if(event.getSource().getType() == Material.STAINED_CLAY) {
    		event.setCancelled(true);
    	}
    }

}
