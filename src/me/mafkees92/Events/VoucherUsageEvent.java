package me.mafkees92.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Utils.Utils;

public class VoucherUsageEvent implements Listener {

	@EventHandler
	public void onVoucherUsage(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			if (!(item.getType().equals(Material.PAPER)))
				return;

			String nbtTag = Utils.getNBTTag(item, "customVoucher");
			if (nbtTag.equals("permission")) {
				String permission = Utils.getNBTTag(item, "permissionVoucher");
				if (!permission.isEmpty()) {
					//if (!e.getPlayer().hasPermission(permission)) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + e.getPlayer().getName() + " permission settemp " + permission + " true 10m accumulate");
						
						if(item.getAmount() == 1)
							e.getPlayer().getInventory().removeItem(item);
						else {
							item.setAmount(item.getAmount()-1);
							e.getPlayer().getInventory().setItemInMainHand(item);
						}
					//}
					//else {
					//	e.getPlayer().sendMessage(Utils.colorize("&cYou already have the permission:" + permission));
					//}
				}
			}
		}
	}
}
