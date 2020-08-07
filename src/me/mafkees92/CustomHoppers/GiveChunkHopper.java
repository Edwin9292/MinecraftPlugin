package me.mafkees92.CustomHoppers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class GiveChunkHopper implements CommandExecutor {

	Main plugin;
	
	public GiveChunkHopper(Main plugin) {
		this.plugin = plugin;
	}
	
	//givechunkhopper <Player> <Amount>
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		if(!(player.isOp() || player.hasPermission("mafkeesplugin.givechunkhopper"))) {
			player.sendMessage(Messages.noPermission); 
			return true;	}
		if(args.length != 2) {
			player.sendMessage(Messages.invalidChunkHopperArguments); 
			return true;}
		
		Player targetPlayer = Bukkit.getPlayer(args[0]);
		
		if(targetPlayer != null && targetPlayer.isOnline()) {					
			int amount = Utils.tryParseInt(args[1]);
			if(amount != -1) {
				if(targetPlayer.getInventory().firstEmpty() != -1) {
					ItemStack customHopper = CustomHopper.CreateCustomHopper();
					customHopper.setAmount(amount);
					targetPlayer.getInventory().addItem(customHopper);
					targetPlayer.sendMessage("You have received a custom hopper");
					player.sendMessage("Gave "+ targetPlayer.getName() + " " + amount + " custom hopper(s)");
					return true;
					
				} else {	player.sendMessage(Messages.inventoryFull(targetPlayer)); return true;}
			} else {	player.sendMessage(Messages.invalidAmount);	return true;}
		} else {	player.sendMessage(Messages.invalidTargetPlayer); return true;}

	}

	
		
}
