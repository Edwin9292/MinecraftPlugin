package me.mafkees92.Listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Utils.Utils;

public class BlockBreakListeners implements Listener{

	
	@EventHandler
	private void onInventoryFull(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(player.getInventory().firstEmpty() != -1) 
			return;

		Collection<Material> materials = new ArrayList<Material>();
		event.getBlock().getDrops(player.getInventory().getItemInMainHand()).forEach(item -> materials.add(item.getType()));;
		for(ItemStack item: player.getInventory().getStorageContents()) {
			if(!materials.contains(item.getType()))
				continue;
			if(item.getAmount() == item.getMaxStackSize())
				continue;
			if(item.hasItemMeta())
				continue;
			
			//if the player has a slot that isnt full and  can be stacked to, we dont have to send an inventory full title
			return;
		}
		player.sendTitle(Utils.colorize("&cInventory Full!"), Utils.colorize("&eTry selling or storing some items."), 1, 30, 3);
	}
	
}
