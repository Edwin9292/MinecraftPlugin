package me.mafkees92.Listeners;


import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;


public class BlockDispenseListener implements Listener{

	
	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent e) {
		ItemStack item = e.getItem();
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if(meta instanceof BlockStateMeta) {
				BlockStateMeta bsMeta = (BlockStateMeta) meta;
				if(bsMeta.getBlockState() instanceof ShulkerBox) {
					e.setCancelled(true);
				}
			}
		}
	}
}
