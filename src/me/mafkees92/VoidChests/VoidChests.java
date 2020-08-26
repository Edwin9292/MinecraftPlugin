package me.mafkees92.VoidChests;

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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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
	
	private final Vector hologramOffset = new Vector(0.5d, 2.05d, 0.5d);
	HeadDatabaseAPI hdb;
	
	public VoidChests(Main plugin, String fileName) {
		super(plugin, fileName);
		
		hdb = new HeadDatabaseAPI();
		
		loadVoidChests();

		Bukkit.getScheduler().runTaskTimer(plugin, this::payOutAllVoidChests, 100L, 600L);
	}
	
	//load void chests from config
	public void loadVoidChests() {
		if(config.getConfigurationSection("chunkloaders") == null){
			config.createSection("chunkloaders");
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
		
		if(hdb.getHeads(CategoryEnum.ALPHABET).size() > 0) {
			addHologram(chest);
		}
		else {
			hologramsToAdd.add(chest);
		}
		this.save();
	}
	
	public void removeVoidChest(VoidChest chest) {
		this.voidChestList.remove(chest);
		chest.destroy();
		this.config.set("voidchests." + chest.getLocationString(), null);
		Holograms.RemoveHologram(chest.getLocation().add(hologramOffset));
		this.save();
	}

	private void addHologram(VoidChest chest) {
		chest.setHologramTextLines(Holograms.AddHologram(chest.getLocation().clone().add(hologramOffset), 
				hdb.getItemHead("35106") , 
				Utils.colorize("&6&lVoid Chest &e[&6&l" + chest.getChestGrade() + "&e]"), 
				Utils.colorize(chest.getMoneyInChestTextLine())));
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
			Bukkit.getPlayer(pair.getKey()).sendMessage(Utils.colorize("You have been payed $"+ pair.getValue() + " from your voidchests."));
		}
	}

	//where voidchest contains location, chunk, owned player, hologram location or hologram instance, clean up method, sell method



	
	//events

	//void chest place event
	@EventHandler
	public void voidChestPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.CHEST)) {
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
	

	// 0,5 second sell timer boost per grade, max 3 second boost
	private ItemStack createVoidChestItemStack(int amount, int grade) {
		ItemStack chest = new ItemStack(Material.CHEST);
		
		ItemMeta meta = chest.getItemMeta();
		meta.setDisplayName(Utils.colorize("&o&3&lVoid Chest &2(Grade "+ grade +")"));
		
		List<String> lore = new ArrayList<>();
		lore.add(Utils.colorize("&7When placed, this chest will sell"));
		lore.add(Utils.colorize("&7all of it's contents "));
		lore.add(Utils.colorize("&7every " + ((5.5D - 0.5D * grade ) > 2D ? (5.5D - 0.5D * grade ) : 2D) + " seconds."));
		lore.add(Utils.colorize("&o"));
		lore.add(Utils.colorize("&d&lEPIC"));
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
	  
	//void chest break event
	@EventHandler
	public void voidChestBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.CHEST)){
			VoidChest chest = this.getVoidChestAt(event.getBlock().getLocation());
			if(chest != null) {
				Player player = event.getPlayer();
				event.setDropItems(false);    //disable dropping a normal chest
				//pay owner of the chest the remaining money
				double money = chest.payOut();
				Main.econ.depositPlayer(chest.getInventoryOwner(), money);
				if(chest.getInventoryOwner().isOnline()) {
					chest.getInventoryOwner().getPlayer().sendMessage(Utils.colorize("You have been payed $"+ money + " from a broken voidchest"));
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

