package me.mafkees92.Events;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import me.mafkees92.Main;

public class CustomSplashPotions implements Listener{
	
	private final Plugin plugin;
	public CustomSplashPotions(Main main) {
	     this.plugin = main;
	}

	final String potionName = "Freeze";
	final String loreLineOne = "Freeze your enemies";
	
	@EventHandler
	public void onSplashPotion(PotionSplashEvent e) {
		
		ThrownPotion potion = e.getPotion();
		ItemStack item = potion.getItem();
		
		if(item.hasItemMeta()) {
			if(item.getItemMeta().getDisplayName().equals(potionName)) {
				if(item.getItemMeta().getLore().contains(loreLineOne)){
					e.setCancelled(true);
					PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 70, 10);
					for (LivingEntity entity : e.getAffectedEntities()) {
						entity.addPotionEffect(effect);
					}
					setBlocks(e.getEntity().getLocation());
				}
			}
		}
	}
	
	private void setBlocks(Location location) {
	
		ArrayList<Block> changedBlocks = new ArrayList<Block>();
		int radius = 4 ;
		Block block = location.getBlock();
		for (int x = -(radius); x <= radius; x ++) {
			for (int z = -(radius); z <= radius; z ++) {
				Block relativeBlock = block.getRelative(x,0,z);
				if(relativeBlock.getType().equals(Material.AIR)) {
					Random random = new Random();
					int randInt = random.nextInt(100);
					if(randInt < 20) {
						relativeBlock.setType(Material.PACKED_ICE);
						changedBlocks.add(relativeBlock);
					}
					else if(randInt < 50) {
						relativeBlock.setType(Material.ICE);
						changedBlocks.add(relativeBlock);
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(plugin, () -> removeBlocks(changedBlocks), 70);
	}
	
	
	private void removeBlocks(ArrayList<Block> blocksToRemove) {
	
		for (Block block : blocksToRemove) {
			block.setType(Material.AIR);
		}
	}

}
