package me.mafkees92.CustomHoppers;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class HopperEvents implements Listener{
	
	private Main plugin;
	public HopperEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnItemDrop(ItemSpawnEvent event) {
		List<CustomHopper> hoppers = plugin.getHopperData().getHoppersInChunk(event.getLocation());
		if(hoppers == null || hoppers.size() == 0) return;
		
		//item to add
		ItemStack item = event.getEntity().getItemStack();
		int maxStack = item.getMaxStackSize();
		int amountToAdd = item.getAmount();
		
		//loop trough all hoppers
		for (CustomHopper customHopper : hoppers) {
			//get all itemstacks in the hopper
			List<ItemStack> itemsInHopper = Arrays.asList(customHopper.getInventory().getContents());

			//loop trough all items in the hopper
			for (ItemStack itemInHopper : itemsInHopper) {			
				if(itemInHopper == null) continue;  // null check
				if(!itemInHopper.isSimilar(item)) continue;  //if the item in the hopper isn't similar to the dropped item, continue to next item;
				if(itemInHopper.getAmount() == itemInHopper.getMaxStackSize()) continue; //if the current itemstack is full, continue to next item
				
				//if we reach this the items are similar and the stack isn't full
				int possibleToAdd = maxStack - itemInHopper.getAmount();
				
				//if the amount to add is equal or less than we can add to this stack, add it to the stack and return.
				if(amountToAdd <= possibleToAdd) {
					itemInHopper.setAmount(itemInHopper.getAmount() + amountToAdd);
					customHopper.updateInventoryViewers();
					event.setCancelled(true);
					return;
				}
				
				//if amount to add is more than we can add to this stack
				else {
					itemInHopper.setAmount(maxStack);
					customHopper.updateInventoryViewers();
					amountToAdd -= possibleToAdd;
					continue;
				}
			}
			//if we reach this part, all occupied slots are either a different item or full stacks, so we need to add to empty slots. 
			while(amountToAdd > 0) {
				//check if the hopper still has an empty slot, if so, break out of the loop and continue to the next hopper in this chunk.
				int firstEmpty = customHopper.getInventory().firstEmpty();
				if(firstEmpty == -1) break;
				
				//if the amount to add is the same as the maxstack or more, add the maxstack to the hopper, decrease amounttoadd and find another empty slot
				if(amountToAdd >= maxStack) {
					item.setAmount(maxStack);
					customHopper.getInventory().addItem(item);
					customHopper.updateInventoryViewers();
					amountToAdd -= maxStack;
					continue;
				}
				
				// amounttoadd is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
				else {
					item.setAmount(amountToAdd);
					customHopper.getInventory().addItem(item);
					customHopper.updateInventoryViewers();
					event.setCancelled(true);
					return;
				}
			}
		}
		//if we reach this part, it means all hoppers are full; Drop the items on the ground.
		event.getEntity().getItemStack().setAmount(amountToAdd);
		return;
		
	}
	
	@EventHandler
	public void HopperPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)){
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			String tag = Utils.getNBTTag(item, "chunkhopper");
			if(tag == null) return;
			if(tag == "") return;
			
			CustomHopper hopper = new CustomHopper(event.getBlock().getLocation());
			plugin.getHopperData().AddHopperData(hopper);
			event.getPlayer().sendMessage("added custom hopper");
			
		}
	}
	
	@EventHandler
	public void HopperBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)) {
			if(plugin.getHopperData().isChunkHopper(event.getBlock().getLocation())) {
				if(plugin.getHopperData().removeHopper(new CustomHopper(event.getBlock().getLocation()))) {
					event.getPlayer().sendMessage("Removed custom hopper");
					event.setDropItems(false);
					if(event.getPlayer().getInventory().firstEmpty() == -1)
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), CustomHopper.CreateCustomHopper());
					else {
						event.getPlayer().getInventory().addItem(CustomHopper.CreateCustomHopper());
					}
				}
			}
		}
	}
}
