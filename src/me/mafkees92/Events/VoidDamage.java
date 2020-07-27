package me.mafkees92.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.mafkees92.Main;

public class VoidDamage implements Listener {

	Plugin plugin;

	public VoidDamage(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onVoidDamage(EntityDamageEvent e) {
		if (e.getCause() != DamageCause.VOID)
			return;
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			String worldName = player.getWorld().getName();
			if (worldName.equalsIgnoreCase("staff") || worldName.equalsIgnoreCase("skyblock")
					|| worldName.equalsIgnoreCase("skyblock_nether")
					|| worldName.equalsIgnoreCase("skyblock_the_end")
					|| worldName.equalsIgnoreCase("testing")) {
				e.setCancelled(true);
				player.teleport(ASkyBlockAPI.getInstance().getHomeLocation(player.getUniqueId()));

				player.setFallDistance(0);

			}

		}
	}

	/*
	 * @EventHandler public void OnItemSpawn(ItemSpawnEvent e) {
	 * Bukkit.getServer().broadcastMessage("Item has spawned at " +
	 * Utils.LocationToString(e.getEntity().getLocation()) + " Block type = " +
	 * e.getEntity().getItemStack().getType()); Block b =
	 * e.getEntity().getWorld().getBlockAt(-1202, 76, -600); if(b.getType() ==
	 * Material.HOPPER) { Hopper hopper = (Hopper)b.getState(); //check if hopper
	 * contains the item, if yes check if stack is full, if no add to stack, else
	 * look for empty slot if (!(hopper.getInventory().firstEmpty() == -1)) {
	 * e.setCancelled(true);
	 * hopper.getInventory().addItem(e.getEntity().getItemStack()); } else {
	 * //hopper inventory is full
	 * 
	 * }
	 * 
	 * } }
	 */
}
