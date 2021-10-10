package me.mafkees92.CustomPotions;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class CustomSplashPotions implements Listener {

	private final Plugin plugin;

	public CustomSplashPotions(Main main) {
		this.plugin = main;
	}

	final String CustomPotionNBTKey = "customPotions";
	final String freezePotionNBTTag = "freezePotion";
	ArrayList<Block> changedBlocks = new ArrayList<>();

	@EventHandler
	public void onFreezePotionThrow(PlayerInteractEvent e) {

		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = e.getPlayer();
			if(player.getInventory().getItemInMainHand() != null) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if(item.getType() == Material.SPLASH_POTION) {
					String nbt = Utils.getNBTTag(item, CustomPotionNBTKey);
					if(nbt != null && nbt != "" && nbt.equals(freezePotionNBTTag)) {
						if(ASkyBlockAPI.getInstance().getIslandAt(player.getLocation()).isSpawn()) {   //disable it at the spawn island
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onSplashPotion(PotionSplashEvent e) {

		ThrownPotion potion = e.getPotion();
		ItemStack item = potion.getItem();

		String nbtTag = Utils.getNBTTag(item, CustomPotionNBTKey);
		
		//freeze potions
		if (nbtTag != null && nbtTag.equals(freezePotionNBTTag)) {
			e.setCancelled(true);
			PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 80, 10);
			for (LivingEntity entity : e.getAffectedEntities()) {
				entity.addPotionEffect(effect);
			}
			setBlocks(e.getEntity().getLocation());
		}
	}

	@EventHandler
	public void onIceMelting(BlockFadeEvent e) {
		if (changedBlocks.contains(e.getBlock())) {
			e.setCancelled(true);
		}
	}

	private void setBlocks(Location location) {
		ArrayList<Block> changed = new ArrayList<>();
		int radius = 4;
		Block block = location.getBlock();
		for (int x = -(radius); x <= radius; x++) {
			for (int z = -(radius); z <= radius; z++) {
				Block relativeBlock = block.getRelative(x, 0, z);
				if (relativeBlock.getType().equals(Material.AIR)) {
					Random random = new Random();
					int randInt = random.nextInt(100);
					if (randInt < 20) {
						relativeBlock.setType(Material.PACKED_ICE);
						changed.add(relativeBlock);
						this.changedBlocks.add(relativeBlock);
					} else if (randInt < 50) {
						relativeBlock.setType(Material.ICE);
						changed.add(relativeBlock);
						this.changedBlocks.add(relativeBlock);
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(plugin, () -> removeBlocks(changed), 80);
	}

	private void removeBlocks(ArrayList<Block> blocksToRemove) {

		for (Block block : blocksToRemove) {
			if (block.getType().equals(Material.PACKED_ICE))
				block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 4, 50);
			block.setType(Material.AIR);
			this.changedBlocks.remove(block);
		}
	}

}
