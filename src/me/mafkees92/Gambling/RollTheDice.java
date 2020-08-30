package me.mafkees92.Gambling;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class RollTheDice implements Listener{

	private Inventory pickANumberInventory;
	private Inventory pickABetInventory;
	private HeadDatabaseAPI hdb;
	private boolean pickANumbeInventoryCreated = false;
	
	private int gameInventorySize = 45;
	
	private List<List<ItemStack>> bettingInventoryContents;
	private List<BettingPlayer> bettingPlayers = new ArrayList<>();
	
	public RollTheDice() {
		this.hdb = new HeadDatabaseAPI();
		this.CreateInventories();
	}

	
	public void StartGame(Player player) {
		player.openInventory(this.pickANumberInventory);
	}
	
	
	private void RunGame(BettingPlayer bettingPlayer) {
		bettingPlayer.setBetting(true);
		Inventory playerInv = this.createGameInventory(bettingPlayer);
		Player player = bettingPlayer.getPlayer();
		player.openInventory(playerInv);

		Random random = new Random(System.currentTimeMillis());
		int numberToWin = random.nextInt(6);		
		runTaskAfterTicks(bettingPlayer, playerInv, -1, 2.0, numberToWin);

	}
	
	private void runTaskAfterTicks(BettingPlayer player, Inventory playerInv, int previousNumber, double ticks, int numberToWin) {
		new BukkitRunnable() {
			int number = -1;
			
			@Override
			public void run() {
				do {
					number = new Random().nextInt(6);
				}
				while(number == previousNumber);
				
				if(ticks < 10.9) {
					playerInv.setContents(bettingInventoryContents.get(number).toArray(new ItemStack[bettingInventoryContents.get(number).size()]));
					runTaskAfterTicks(player, playerInv, number, ticks * 1.055, numberToWin);
					this.cancel();
				}
				else {
					ItemStack[] contents = bettingInventoryContents.get(numberToWin).
							toArray(new ItemStack[bettingInventoryContents.get(numberToWin).size()]).clone();
					playerInv.setContents(contents);
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()-> DisplayResult(player, playerInv, numberToWin +1 == player.getNumberPicked(), numberToWin), 30);
				}
			}			
		}.runTaskLater(Main.getInstance(), (long) ticks);
	}

	private void DisplayResult(BettingPlayer player, Inventory gameInventory, boolean hasWon, int winningNumber) {
		for (int i = 0; i < gameInventory.getContents().length; i++) {
			if(hasWon) {
				gameInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 5, Utils.colorize("&2WON"), ""));
			}
			else {
				gameInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 14, Utils.colorize("&4LOST"), ""));
			}
		}
		if(hasWon) {
			player.getPlayer().sendMessage(Utils.colorize("&2You won!! You have received &6"
					+ NumberFormat.getCurrencyInstance().format(player.getBetValue() *6) + "&2."));
			Main.econ.depositPlayer(player.getPlayer(), player.getBetValue() * 6);
			for (int i = 0; i < 8; i++) {
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 50, 50);
				player.getPlayer().getWorld().playEffect(player.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 20, 20);
			}
		}
		else {
			player.getPlayer().sendMessage(Utils.colorize("&cYou have lost. The winning number was: &2" + (winningNumber +1) + "&c."));
			for (int i = 0; i < 8; i++) {
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 5, 10);
				player.getPlayer().getWorld().spawnParticle(Particle.FLAME, player.getPlayer().getLocation(), 50);
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			if(player.getPlayer().getOpenInventory().getTopInventory().getName().contentEquals(Utils.colorize("&6&lTOOT&e&lMC &7: &eRoll The Dice"))){
				player.getPlayer().closeInventory();
			}
			player.setBetting(false);
		}, 40l);
	}
	
	
	private void CreateInventories() {
		this.createPickANumberInventory();
		this.createPickABetInventory();
		this.createGameInventoryContents();
	}
	
	
	
	private void createPickANumberInventory() {
		if(hdb.getHeads(CategoryEnum.ALPHABET).size() > 0) {
			this.pickANumberInventory = Bukkit.createInventory(null, 45, Utils.colorize("&6&lTOOT&e&lMC &7: &eSelect A Number"));
			this.pickANumberInventory.setItem(4, Utils.setNBTTag(Utils.createCustomHeadItem("9270", Utils.colorize("&6&lNumber 1"),
					Utils.colorize("&eClick to pick number 1")), "pickanumber", "1"));
			this.pickANumberInventory.setItem(15, Utils.setNBTTag(Utils.createCustomHeadItem("9269", Utils.colorize("&6&lNumber 2"),
					Utils.colorize("&eClick to pick number 2")), "pickanumber", "2"));
			this.pickANumberInventory.setItem(33, Utils.setNBTTag(Utils.createCustomHeadItem("9268", Utils.colorize("&6&lNumber 3"),
					Utils.colorize("&eClick to pick number 3")), "pickanumber", "3"));
			this.pickANumberInventory.setItem(40, Utils.setNBTTag(Utils.createCustomHeadItem("9267", Utils.colorize("&6&lNumber 4"),
					Utils.colorize("&eClick to pick number 4")), "pickanumber", "4"));
			this.pickANumberInventory.setItem(29, Utils.setNBTTag(Utils.createCustomHeadItem("9266", Utils.colorize("&6&lNumber 5"),
					Utils.colorize("&eClick to pick number 5")), "pickanumber", "5"));
			this.pickANumberInventory.setItem(11, Utils.setNBTTag(Utils.createCustomHeadItem("9265", Utils.colorize("&6&lNumber 6"),
					Utils.colorize("&eClick to pick number 6")), "pickanumber", "6"));
			this.pickANumbeInventoryCreated = true;
		}
	}
	
	private void createPickABetInventory() {
		this.pickABetInventory = Bukkit.createInventory(null, 54, Utils.colorize("&6&lTOOT&e&lMC &7: &eSelect Your Bet"));
		

		this.pickABetInventory.setItem(10, Utils.createCustomItem(Material.PAPER, 
				"&6Pick Your Bet", 
				"&o",
				"&7Choose your bet by clicking",
				"&7one of the diamonds below."));
		
		this.pickABetInventory.setItem(13, Utils.setNBTTag(Utils.createCustomItem(Material.EMERALD, "&eBet &6$500,000", "", ""), "bet", "500000"));
		this.pickABetInventory.setItem(21, Utils.setNBTTag(Utils.createCustomItem(Material.DIAMOND, "&eBet &6$100,000", "", ""), "bet", "100000"));
		this.pickABetInventory.setItem(23, Utils.setNBTTag(Utils.createCustomItem(Material.DIAMOND, "&eBet &6$200,000", "", ""), "bet", "200000"));
		this.pickABetInventory.setItem(29, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&eBet &6$10,000", "", ""), "bet", "10000"));
		this.pickABetInventory.setItem(31, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&eBet &6$25,000", "", ""), "bet", "25000"));
		this.pickABetInventory.setItem(33, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&eBet &6$50,000", "", ""), "bet", "50000"));
		this.pickABetInventory.setItem(37, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&eBet &6$1,000", "", ""), "bet", "1000"));
		this.pickABetInventory.setItem(39, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&eBet &6$2,500", "", ""), "bet", "2500"));
		this.pickABetInventory.setItem(41, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&eBet &6$5,000", "", ""), "bet", "5000"));
		this.pickABetInventory.setItem(43, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&eBet &6$7,500", "", ""), "bet", "7500"));
		
	}
	
	private Inventory createGameInventory(BettingPlayer player) {
		return Bukkit.createInventory(player.getPlayer(), gameInventorySize, Utils.colorize("&6&lTOOT&e&lMC &7: &eRoll The Dice"));
	}
	
	private void createGameInventoryContents() {
		this.bettingInventoryContents = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			List<ItemStack> invContents = Arrays.asList(new ItemStack[gameInventorySize]);
			for (int j = 0; j < 5; j++) {
				invContents.set(2 + j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "", ""));
				invContents.set(3+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "", ""));
				invContents.set(4+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "", ""));
				invContents.set(5+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "", ""));
				invContents.set(6+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "", ""));
			}
			this.bettingInventoryContents.add(invContents);
		}
		this.bettingInventoryContents.get(0).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		
		this.bettingInventoryContents.get(1).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(1).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));

		this.bettingInventoryContents.get(2).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(2).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(2).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));

		this.bettingInventoryContents.get(3).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(3).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(3).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(3).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));

		this.bettingInventoryContents.get(4).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(4).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(4).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(4).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(4).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));

		this.bettingInventoryContents.get(5).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(5).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(5).set(21, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "", ""));
		this.bettingInventoryContents.get(5).set(23, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "", ""));
		this.bettingInventoryContents.get(5).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
		this.bettingInventoryContents.get(5).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "", ""));
	}
	
	
	
	
	
	
	
	
	
	//events inventoryclick inventorydrag
	@EventHandler
	public void onPickANumberClick(InventoryClickEvent event) {
		if(event.getInventory().equals(this.pickANumberInventory)) {
			event.setCancelled(true);
			String tag = Utils.getNBTTag(event.getCurrentItem(), "pickanumber");
			int numberClicked = Utils.tryParseInt(tag);
			
			if(numberClicked != -1) {
				BettingPlayer player = bettingPlayers.stream().filter(x -> x.getPlayer().equals((Player)event.getWhoClicked())).findFirst().orElse(null);
				if(player == null) {
					player = new BettingPlayer((Player)event.getWhoClicked());
					player.setNumberPicked(numberClicked);
					this.bettingPlayers.add(player);
				}
				else {
					if(player.isBetting()) {
						player.getPlayer().sendMessage(Utils.colorize("&cPlease wait till your previous bet is completed"));
						player.getPlayer().closeInventory();
						return;
					}else {
						player.setNumberPicked(numberClicked);
					}
				}
				player.getPlayer().openInventory(this.pickABetInventory);
			}
		}
			
		
		
		if(event.getInventory().equals(this.pickABetInventory)) {
			event.setCancelled(true);
			String tag = Utils.getNBTTag(event.getCurrentItem(), "bet");
			int betValue = Utils.tryParseInt(tag);
			if(betValue != -1) {
				BettingPlayer player = bettingPlayers.stream().filter(x -> x.getPlayer().equals((Player)event.getWhoClicked())).findFirst().orElse(null);
				if(player == null) {
					event.getWhoClicked().sendMessage("ERROR: You havent picked a number yet");
				}
				else {
					//check if player has balance TODO
					if(Main.econ.getBalance(player.getPlayer()) >= betValue) {
						player.setBetValue(betValue);
						player.getPlayer().sendMessage(Utils.colorize("&eYou have placed your bet on number &6" + player.getNumberPicked() + "&e." ));
						player.getPlayer().sendMessage(Utils.colorize("&eStarting your game of Roll The Dice with a bet of &6"
						+ NumberFormat.getCurrencyInstance().format(betValue) + "&e."	));
						Main.econ.withdrawPlayer((Player)event.getWhoClicked(), betValue);
						this.RunGame(player);
					}
					else {
						player.getPlayer().sendMessage(Utils.colorize("&cYou have insufficient balance to do this bet."));
					}
				}
				
			}
		}
			
		
		
		
		
		if(event.getInventory().getTitle().contentEquals(Utils.colorize("&6&lTOOT&e&lMC &7: &eRoll The Dice"))) {
			event.setCancelled(true);
		}
	}	
	
	

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		if(event.getInventory().equals(this.pickANumberInventory) ||
			event.getInventory().equals(this.pickABetInventory) ||
			event.getInventory().getTitle().contentEquals(Utils.colorize("&6&lTOOT&e&lMC &7: &eRoll The Dice"))){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHeadDatabaseLoaded(DatabaseLoadEvent event) {
		if(!this.pickANumbeInventoryCreated) {
			this.createPickANumberInventory();
		}
	}
	
	
}
