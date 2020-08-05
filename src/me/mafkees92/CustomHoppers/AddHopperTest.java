package me.mafkees92.CustomHoppers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class AddHopperTest implements CommandExecutor {

	Main plugin;
	
	public AddHopperTest(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		
		Player player = (Player) sender;
		Location loc = player.getLocation();
		CustomHopper hopper = new CustomHopper(loc);
		
		plugin.getHopperData().AddHopperData(hopper);
		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("ChunkHopper");
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunkhopper", "hopper");
		player.getInventory().addItem(item);
		player.sendMessage("Added hopper" + loc.getWorld().getName());
		
		return true;
	}

}
