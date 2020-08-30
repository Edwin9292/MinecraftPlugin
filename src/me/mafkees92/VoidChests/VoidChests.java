package me.mafkees92.VoidChests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import me.mafkees92.Holograms;
import me.mafkees92.Main;
import me.mafkees92.Files.BaseFile;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class VoidChests extends BaseFile implements Listener, CommandExecutor{

	List<VoidChest> voidChestList = new ArrayList<>();
	List<VoidChest> hologramsToAdd = new ArrayList<>();

	HeadDatabaseAPI hdb;
	
	public VoidChests(Main plugin, String fileName) {
		super(plugin, fileName);
		
		hdb = new HeadDatabaseAPI();
		
		loadVoidChests();

		Bukkit.getScheduler().runTaskTimer(plugin, this::payOutAllVoidChests, 100L, 600L);
	}
	
	//load void chests from config
	public void loadVoidChests() {
		if(config.getConfigurationSection("voidchests") == null){
			config.createSection("voidchests");
		}
		
		HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("voidchests").getValues(false);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			VoidChest chest = new VoidChest(entry.getKey(), (String)entry.getValue());
			this.addVoidChest(chest);
		}
	}

	public void onDisable() {
		for (VoidChest voidChest : voidChestList) {
			config.set("voidchests." + voidChest.getLocationString(), voidChest.getDataString());
		}
		this.save();
	}
	
	
	public void addVoidChest(VoidChest chest) {
		this.voidChestList.add(chest);
		this.config.set("voidchests." + chest.getLocationString(), chest.getDataString());
		
		if(chest.getLocation().clone().add(0,1,0).getBlock().getType() == Material.AIR) {
			if(hdb.getHeads(CategoryEnum.ALPHABET).size() > 0) {
				addHologram(chest);
			}
			else {
				hologramsToAdd.add(chest);
			}
		}
		
		this.save();
	}
	
	public void removeVoidChest(VoidChest chest) {
		this.voidChestList.remove(chest);
		chest.destroy();
		this.config.set("voidchests." + chest.getLocationString(), null);
		Holograms.RemoveHologram(chest.getLocation().add(chest.getHologramOffset()));
		this.save();
	}

	private void addHologram(VoidChest chest) {
		int chestGrade = chest.getChestGrade();
		if(chestGrade <= 5) {
			chest.setHologramTextLines(Holograms.AddHologram(chest.getLocation().clone().add(chest.getHologramOffset()), 
					hdb.getItemHead("35106") , 
					Utils.colorize("&e&lVoid Chest &7["+ gradeToColorCode(chestGrade) + chestGrade + "〣&7]"), 
					Utils.colorize(chest.getMoneyInChestTextLine()),
					Utils.colorize("&7Placed by: &b" + chest.getInventoryOwner().getName())));
		}
		else {
			chest.setHologramTextLines(Holograms.AddHologram(chest.getLocation().clone().add(chest.getHologramOffset()), 
					hdb.getItemHead("35106") , 
					Utils.colorize("&e&lVoid Chest &7["+ gradeToColorCode(chestGrade) + chestGrade + "〣&7]"), 
					Utils.colorize(chest.getMoneyInChestTextLine()),
					Utils.colorize("&7Booster: &d" + (chestGrade == 6 ? "5%" : "10%")),
					Utils.colorize("&7Placed by: &b" + chest.getInventoryOwner().getName())));
		}
	}
	
	public VoidChest getVoidChestAt(Location location) {
		return voidChestList.stream().filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
	}
	
	public void sellAllVoidChestsContents() {
		for (VoidChest voidChest : voidChestList) {
			voidChest.sellContents();
		}
	}
	
	public void payOutAllVoidChests() {
		HashMap<UUID, Double> playersToPayout = new HashMap<>();
		for (VoidChest voidChest : voidChestList) {
			if(voidChest.getInventoryOwner().isOnline()) {
				if(playersToPayout.containsKey(voidChest.getInventoryOwner().getUniqueId())) {
					playersToPayout.put(voidChest.getInventoryOwner().getUniqueId(), 
						playersToPayout.get(voidChest.getInventoryOwner().getUniqueId()) + voidChest.payOut());
				}
				else {
					playersToPayout.put(voidChest.getInventoryOwner().getUniqueId(), voidChest.payOut());
				}
			}
		}
		for (Map.Entry<UUID, Double> pair : playersToPayout.entrySet()) {
			if(pair.getValue() <= 0d) continue;
			Main.econ.depositPlayer(Bukkit.getPlayer(pair.getKey()), pair.getValue());
			Bukkit.getPlayer(pair.getKey()).sendMessage(Utils.colorize("You have been payed "+ NumberFormat.getCurrencyInstance().format(pair.getValue()) + " from your voidchests."));
		}
	}

	private String gradeToColorCode(int grade) {
		switch (grade) {
		
		case 1:
			return "&a";
		case 2:
			return "&2";
		case 3:
			return "&b";
		case 4:
			return "&1";
		case 5:
			return "&e";
		case 6:
			return "&6";
		case 7:
			return "&c";

		default:
			return "";
		}
	}
	

	
	
	private boolean isPlacedNextToVoidChest(VoidChest chest) {

		Location locNorth = chest.getLocation().clone().add(0,0,-1);
		Location locEast = chest.getLocation().clone().add(1,0,0);
		Location locSouth = chest.getLocation().clone().add(0,0,1);
		Location locWest = chest.getLocation().clone().add(-1,0,0);
		return this.getVoidChestAt(locNorth) != null ||
				this.getVoidChestAt(locEast) != null ||
				this.getVoidChestAt(locSouth) != null ||
				this.getVoidChestAt(locWest) != null;
	}
	
	// grade 1: 30s, grade 2: 20s, grade 3: 10s, grade 4: 6s, grade 5 6 7: 3s  6 5% boost 7 10% boost
			private ItemStack createVoidChestItemStack(int amount, int grade) {
				//ItemStack chest = new ItemStack(Material.LIME_SHULKER_BOX);
				ItemStack chest;
				int timePerSell; 
				String rarity;
				
				switch(grade) {
					case 1: 
						chest = new ItemStack(Material.LIME_SHULKER_BOX);
						timePerSell = 30;
						rarity = "&9&lRARE";
						break;
					case 2:
						chest = new ItemStack(Material.GREEN_SHULKER_BOX);
						timePerSell = 20;
						rarity = "&9&lRARE";
						break;
					case 3:
						chest = new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX);
						timePerSell = 10;
						rarity = "&d&lEPIC";
						break;
					case 4:
						chest = new ItemStack(Material.BLUE_SHULKER_BOX);
						timePerSell = 6;
						rarity = "&d&lEPIC";
						break;
					case 5:
						chest = new ItemStack(Material.YELLOW_SHULKER_BOX);
						timePerSell = 3;
						rarity = "&6&lLEGENDARY";
						break;
					case 6:
						chest = new ItemStack(Material.ORANGE_SHULKER_BOX);
						timePerSell = 3;
						rarity = "&6&lLEGENDARY";
						break;
					case 7:
						chest = new ItemStack(Material.RED_SHULKER_BOX);
						timePerSell = 3;
						rarity = "&6&lLEGENDARY";
						break;
					default:
						chest = new ItemStack(Material.GRAY_SHULKER_BOX);
						timePerSell = 60;
						rarity = "&7&lDAMN YOU HACKER";
						break;
				}
				
				
				ItemMeta meta = chest.getItemMeta();
				meta.setDisplayName(Utils.colorize("&e&lVoid Chest &7["+ this.gradeToColorCode(grade) + grade + "〣&7]"));
				
				List<String> lore = new ArrayList<>();
				lore.add(Utils.colorize("&7When placed, this chest will "));
				lore.add(Utils.colorize("&7sell all of it's contents "));
				lore.add(Utils.colorize("&7every &e" + timePerSell + " seconds&7."));
				if(grade > 5) {
					String sellbooster = grade == 6 ? "5%" : "10%";
					lore.add(Utils.colorize("&o"));
					lore.add(Utils.colorize("&7The contents will be sold "));
					lore.add(Utils.colorize("&7with a &d" + sellbooster + " sell booster&7."));
				}
				else {
				}
				lore.add(Utils.colorize("&o"));
				lore.add(Utils.colorize(rarity));
				meta.setLore(lore);
				
				chest.setItemMeta(meta);
				chest.setAmount(amount);
				chest = Utils.setNBTTag(chest, "voidchest", Integer.toString(grade));

				return chest;
			}
	


	//
	//
	//  **** EVENTS ****
	//
	//
	
	
	
	
	@EventHandler
	public void onDatabaseLoad(DatabaseLoadEvent e){
		try{
		   if(hologramsToAdd.size() > 0) {
			   Iterator<VoidChest> it = hologramsToAdd.iterator();
			   while(it.hasNext()) {
				   addHologram(it.next());
				   it.remove();
				   }
			   }
		   }
		catch(NullPointerException nullpointer){
			Bukkit.getLogger().info( "could not find the head you were looking for" );
		}
	}
	  
	//void chest place event
		@EventHandler
		public void voidChestPlaceEvent(BlockPlaceEvent event) {
			if(event.getBlock().getState() instanceof ShulkerBox) {
				ItemStack placedChest = event.getPlayer().getInventory().getItemInMainHand();
				String nbt = Utils.getNBTTag(placedChest, "voidchest");
				if(nbt != null && !nbt.equals("")) {
					
					VoidChest chest = new VoidChest(event.getBlock().getLocation(), event.getPlayer(), Utils.tryParseInt(nbt));
					if(isPlacedNextToVoidChest(chest)) {
						event.getPlayer().sendMessage(Utils.colorize("&cYou can't place two voidchests next to eachother."));
						event.setCancelled(true);
						return;
					}
					this.addVoidChest(chest);
				}
			}
			
			//check if there is a block placed above a void chest, and if so remove the hologram.
			else {
				VoidChest chest = getVoidChestAt(event.getBlock().getLocation().clone().add(0d,-1d,0d));
				if(chest != null) {
					Holograms.RemoveHologram(chest.getLocation().clone().add(chest.getHologramOffset()));
				}
			}
		}
	
	
	//void chest break event
	@EventHandler
	public void voidChestBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getState() instanceof ShulkerBox){
			VoidChest chest = this.getVoidChestAt(event.getBlock().getLocation());
			if(chest != null) {
				Player player = event.getPlayer();
				event.setDropItems(false);    //disable dropping a normal chest
				//pay owner of the chest the remaining money
				double money = chest.payOut();
				Main.econ.depositPlayer(chest.getInventoryOwner(), money);
				if(chest.getInventoryOwner().isOnline()) {
					chest.getInventoryOwner().getPlayer().sendMessage(Utils.colorize("You have been payed "+ NumberFormat.getCurrencyInstance().format(money) + " from a broken voidchest"));
				}
				
				this.removeVoidChest(chest);   //remove voidchest
				
				
				
				if(player.getInventory().firstEmpty() == -1) {  //check if player has a free slot in his inventory
					//check if player already has the item
					List<ItemStack> items = Arrays.asList(player.getInventory().getContents());
					ItemStack item = items.stream().filter(x -> x != null && x.getType() != Material.AIR && 
							x.isSimilar(createVoidChestItemStack(1, chest.getChestGrade()))).findFirst().orElse(null);
					//item is in inventory
					if(item != null) {
						//if itemstack is not full, add item
						if(item.getAmount() < item.getMaxStackSize()) {
							player.getInventory().addItem(createVoidChestItemStack(1, chest.getChestGrade()));
							return;
						}
					}
					event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), createVoidChestItemStack(1, 2));
				}
				else {
					player.getInventory().addItem(this.createVoidChestItemStack(1, chest.getChestGrade()));
				}
			}
		}
		else {
			VoidChest chest = getVoidChestAt(event.getBlock().getLocation().clone().add(0d,-1d,0d));
			if(chest != null) {
				this.addHologram(chest);
			}
		}
	}
	
	
	//
	//
	//  **** COMMANDS ****
	//
	//
	
	
	//give voidchest   
	// /givevoidchest <player> <amount>
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		if(player.isOp() || player.hasPermission("mafkeesplugin.voidchest.give")) {
			if(args.length == 3) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if(targetPlayer != null) {
					int amount = Utils.tryParseInt(args[2]);
					if(amount != -1) {
						int grade = Utils.tryParseInt(args[1]);
						if(grade != -1) {
							if(targetPlayer.getInventory().firstEmpty() != -1) {
								targetPlayer.getInventory().addItem(this.createVoidChestItemStack(amount, grade));  //give the targetplayer the itemstack
								return true;
							}
							player.sendMessage(Messages.inventoryFull(targetPlayer));
							return true;
						}
						player.sendMessage(Utils.colorize("&cINVALID ITEM GRADE, try /givevoidchest <player> <grade> <amount>"));
					}
					player.sendMessage(Messages.invalidAmount);
					return true;
				}
				player.sendMessage(Messages.invalidTargetPlayer);
				return true;
			}
			player.sendMessage(Utils.colorize("&cINVALID ARGUMENTS, TRY /givevoidchest <player> <grade> <amount>"));
			return true;
		}
		player.sendMessage(Messages.noPermission);
		return true;
	}
	
	
}

