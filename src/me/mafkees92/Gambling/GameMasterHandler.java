package me.mafkees92.Gambling;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.mafkees92.Main;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class GameMasterHandler implements CommandExecutor, Listener {

	Main plugin;
	HeadDatabaseAPI hdb;
	private RollTheDice RollTheDiceInstance;

	private Inventory gameMasterInventory;

	public GameMasterHandler(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this.RollTheDiceInstance = new RollTheDice(), Main.getInstance());

		this.initialize();

	}

	private void initialize() {
		hdb = new HeadDatabaseAPI();
		this.createGameMasterInventory();
	}

	private void createGameMasterInventory() {

		this.gameMasterInventory = Bukkit.createInventory(null, 27, Messages.gameMasterInventoryTitle);

		for (int i = 0; i < gameMasterInventory.getSize(); i++) {
			this.gameMasterInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "&7", ""));
		}
		
		
		ItemStack rollTheDice = Utils.createCustomItem(Messages.gameMasterRollTheDiceMaterial, Messages.gameMasterRollTheDiceDisplayName,
				Messages.gameMasterRollTheDiceLore);
		rollTheDice = Utils.setNBTTag(rollTheDice, "gamemaster", "rollthedice");

		this.gameMasterInventory.setItem(Messages.gameMasterRollTheDiceSlot, rollTheDice);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		player.openInventory(this.gameMasterInventory);

		return true;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().equals(this.gameMasterInventory)) {
			event.setCancelled(true);

			ItemStack clickedItem = event.getCurrentItem();
			String tag = Utils.getNBTTag(clickedItem, "gamemaster");

			if (tag != null && !tag.contentEquals("")) {

				if (tag.contentEquals("rollthedice")) {
					RollTheDiceInstance.StartGame((Player) event.getWhoClicked());
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryDragEvent e) {
		if (e.getInventory().equals(this.gameMasterInventory)) {
			e.setCancelled(true);
		}
	}

}
