package me.mafkees92.CustomVouchers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class GiveVoucher implements CommandExecutor {

	private final String permission = "mafkeesplugin.givevoucher";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		if (player.isOp() || player.hasPermission(permission)) {
			if (args.length == 3) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);

				if (targetPlayer != null && targetPlayer.isOnline()) {
					if (args[1].equalsIgnoreCase("permission")) {
							
						ItemStack item = new ItemStack(Material.PAPER);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(Utils.colorize("&bPermission Voucher"));
						List<String> lore = new ArrayList<String>();
						lore.add(Utils.colorize("&3Right Click to use this voucher"));
						lore.add(Utils.colorize("&3This voucher will give you permission to use the /givecustompotion command"));
						meta.setLore(lore);
						item.setItemMeta(meta);
						item = Utils.setNBTTag(item, "customVoucher", "permission");
						item = Utils.setNBTTag(item, "permission", args[2]);
						
						if(targetPlayer.getInventory().firstEmpty() == -1) {
							//inventory is full
							player.sendMessage(Messages.CustomVoucherMessages.inventoryFull(targetPlayer));
						}
						else {
							targetPlayer.getInventory().addItem(item);
						}
						return true;			
					}
					player.sendMessage(Messages.CustomVoucherMessages.invalidType);
					return true;
				}
				player.sendMessage(Messages.GeneralMessages.invalidTargetPlayer);
				return true;
			}
			player.sendMessage(Messages.CustomVoucherMessages.invalidArguments);
			return true;
		}
		player.sendMessage(Utils.colorize(Messages.GeneralMessages.noPermission));
		return true;

	}
}
