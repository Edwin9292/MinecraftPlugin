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
import me.mafkees92.Files.Messages;
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
				gameInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 5, Messages.wonFillItemDisplayName, Messages.wonFillItemLore));
			}
			else {
				gameInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 14, Messages.lostFillItemDisplayName, Messages.lostFillItemLore));
			}
		}
		if(hasWon) {
			player.getPlayer().sendMessage(Messages.rollTheDiceWinningMessage(NumberFormat.getCurrencyInstance().format(player.getBetValue() *6)));
			Main.econ.depositPlayer(player.getPlayer(), player.getBetValue() * 6);
			for (int i = 0; i < 8; i++) {
				player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 50, 50);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
					player.getPlayer().getWorld().playEffect(player.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 20, 20);
				}, i*2);
			}
		}
		else {
			player.getPlayer().sendMessage(Messages.rollTheDiceLostMessage(winningNumber +1));
			player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 5, 10);
			for (int i = 0; i < 10; i++) {
				player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 5, 10);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
					player.getPlayer().getWorld().spawnParticle(Particle.FLAME, player.getPlayer().getLocation(), 10);
				}, i*2);
			}
			player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 5, 10);
		}
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			if(player.getPlayer().getOpenInventory().getTopInventory().getName().contentEquals(Messages.rollTheDiceGameInventoryTitle)){
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
			this.pickANumberInventory = Bukkit.createInventory(null, 45, Messages.rollTheDicePickANumberInventoryTitle);
			
			//fill inventory with glass panes
			for (int i = 0; i < this.pickANumberInventory.getSize(); i++) {
				this.pickANumberInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "&7", ""));
			}
			
			//add the numbers
			this.pickANumberInventory.setItem(4, Utils.setNBTTag(Utils.createCustomHeadItem("9270", Utils.colorize("&6Number One"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "1"));
			this.pickANumberInventory.setItem(15, Utils.setNBTTag(Utils.createCustomHeadItem("9269", Utils.colorize("&6Number Two"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "2"));
			this.pickANumberInventory.setItem(33, Utils.setNBTTag(Utils.createCustomHeadItem("9268", Utils.colorize("&6Number Three"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "3"));
			this.pickANumberInventory.setItem(40, Utils.setNBTTag(Utils.createCustomHeadItem("9267", Utils.colorize("&6Number Four"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "4"));
			this.pickANumberInventory.setItem(29, Utils.setNBTTag(Utils.createCustomHeadItem("9266", Utils.colorize("&6Number Five"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "5"));
			this.pickANumberInventory.setItem(11, Utils.setNBTTag(Utils.createCustomHeadItem("9265", Utils.colorize("&6Number Six"), "&7",
					Utils.colorize("&eClick to select!")), "pickanumber", "6"));
			this.pickANumbeInventoryCreated = true;
			
			//add the info item
			this.pickANumberInventory.setItem(22, Utils.createCustomItem(Material.PAPER, "&6Select a number", "&7", "&7Choose any number on the dice!"));
		}
	}
	
	private void createPickABetInventory() {
		this.pickABetInventory = Bukkit.createInventory(null, 54, Messages.rollTheDicePickABetInventoryTitle);
		
		//fill everything with black glass panes
		for (int i = 0; i < this.pickABetInventory.getSize(); i++) {
			this.pickABetInventory.setItem(i, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "&7", ""));
		}
		
		//setup bets
		this.pickABetInventory.setItem(13, Utils.setNBTTag(Utils.createCustomItem(Material.EMERALD, "&6Place a bet of: ", "&2$&a500,000", "&7" , "&eClick to place bet!"), "bet", "500000"));
		this.pickABetInventory.setItem(21, Utils.setNBTTag(Utils.createCustomItem(Material.DIAMOND, "&6Place a bet of: ", "&2$&a100,000", "&7" , "&eClick to place bet!"), "bet", "100000"));
		this.pickABetInventory.setItem(23, Utils.setNBTTag(Utils.createCustomItem(Material.DIAMOND, "&6Place a bet of: ", "&2$&a200,000", "&7" , "&eClick to place bet!"), "bet", "200000"));
		this.pickABetInventory.setItem(29, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&6Place a bet of: ", "&2$&a10,000", "&7" , "&eClick to place bet!"), "bet", "10000"));
		this.pickABetInventory.setItem(31, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&6Place a bet of: ", "&2$&a25,000", "&7" , "&eClick to place bet!"), "bet", "25000"));
		this.pickABetInventory.setItem(33, Utils.setNBTTag(Utils.createCustomItem(Material.GOLD_INGOT, "&6Place a bet of: ", "&2$&a50,000", "&7" , "&eClick to place bet!"), "bet", "50000"));
		this.pickABetInventory.setItem(37, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&6Place a bet of: ", "&2$&a1,000", "&7" , "&eClick to place bet!"), "bet", "1000"));
		this.pickABetInventory.setItem(39, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&6Place a bet of: ", "&2$&a2,500", "&7" , "&eClick to place bet!"), "bet", "2500"));
		this.pickABetInventory.setItem(41, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&6Place a bet of: ", "&2$&a5,000", "&7" , "&eClick to place bet!"), "bet", "5000"));
		this.pickABetInventory.setItem(43, Utils.setNBTTag(Utils.createCustomItem(Material.IRON_INGOT, "&6Place a bet of: ", "&2$&a7,500", "&7" , "&eClick to place bet!"), "bet", "7500"));
		
	}
	
	private Inventory createGameInventory(BettingPlayer player) {
		return Bukkit.createInventory(player.getPlayer(), gameInventorySize, Messages.rollTheDiceGameInventoryTitle);
	}
	
	private void createGameInventoryContents() {
		this.bettingInventoryContents = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			List<ItemStack> invContents = Arrays.asList(new ItemStack[gameInventorySize]);
			for (int j = 0; j < 5; j++) {
				invContents.set(2 + j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "&6&l" + (i + 1), ""));
				invContents.set(3+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "&6&l" + (i + 1), ""));
				invContents.set(4+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "&6&l" + (i + 1), ""));
				invContents.set(5+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "&6&l" + (i + 1), ""));
				invContents.set(6+ j*9, Utils.createCustomItem(Material.STAINED_GLASS_PANE, i, "&6&l" + (i + 1), ""));
			}
			this.bettingInventoryContents.add(invContents);
		}
		this.bettingInventoryContents.get(0).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l1", ""));
		
		this.bettingInventoryContents.get(1).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l2", ""));
		this.bettingInventoryContents.get(1).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l2", ""));

		this.bettingInventoryContents.get(2).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l3", ""));
		this.bettingInventoryContents.get(2).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l3", ""));
		this.bettingInventoryContents.get(2).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l3", ""));

		this.bettingInventoryContents.get(3).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l4", ""));
		this.bettingInventoryContents.get(3).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l4", ""));
		this.bettingInventoryContents.get(3).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l4", ""));
		this.bettingInventoryContents.get(3).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l4", ""));

		this.bettingInventoryContents.get(4).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l5", ""));
		this.bettingInventoryContents.get(4).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l5", ""));
		this.bettingInventoryContents.get(4).set(22, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l5", ""));
		this.bettingInventoryContents.get(4).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l5", ""));
		this.bettingInventoryContents.get(4).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l5", ""));

		this.bettingInventoryContents.get(5).set(12, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l6", ""));
		this.bettingInventoryContents.get(5).set(14, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l6", ""));
		this.bettingInventoryContents.get(5).set(21, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "&6&l6", ""));
		this.bettingInventoryContents.get(5).set(23, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 7, "&6&l6", ""));
		this.bettingInventoryContents.get(5).set(30, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l6", ""));
		this.bettingInventoryContents.get(5).set(32, Utils.createCustomItem(Material.STAINED_GLASS_PANE, 15, "&6&l6", ""));
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
						player.getPlayer().sendMessage(Messages.rollTheDiceBetNotFinishedMessage);
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
				if(player != null) {
					//check if player has balance TODO
					if(Main.econ.getBalance(player.getPlayer()) >= betValue) {
						player.setBetValue(betValue);
						player.getPlayer().sendMessage(Messages.rollTheDiceBetPlacedMessage(player.getNumberPicked()));
						player.getPlayer().sendMessage(Messages.rollTheDiceGameStartedMessage(NumberFormat.getCurrencyInstance().format(betValue)));
						Main.econ.withdrawPlayer((Player)event.getWhoClicked(), betValue);
						this.RunGame(player);
					}
					else {
						player.getPlayer().sendMessage(Messages.gameMasterInsufficientBalance);
					}
				}
				
			}
		}
			
		
		
		
		
		if(event.getInventory().getTitle().contentEquals(Messages.rollTheDiceGameInventoryTitle)) {
			event.setCancelled(true);
		}
	}	
	
	

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		if(event.getInventory().equals(this.pickANumberInventory) ||
			event.getInventory().equals(this.pickABetInventory) ||
			event.getInventory().getTitle().contentEquals(Messages.rollTheDiceGameInventoryTitle)){
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
