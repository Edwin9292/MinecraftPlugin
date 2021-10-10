package me.mafkees92.CustomHoppers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

import me.mafkees92.Holograms;
import me.mafkees92.Main;
import me.mafkees92.Files.BaseFile;
import me.mafkees92.Files.Messages;
import me.mafkees92.Utils.Utils;

public class ChunkHoppers extends BaseFile implements Listener, CommandExecutor{

	

	private final HashMap<String, List<ChunkHopper>> chunkHopperList = new HashMap<>();	
//	private final HashMap<String, ArrayList<ItemStack>> itemsToAdd = new HashMap<>();
	
	
	public ChunkHoppers(Main plugin, String fileName) {
		super(plugin, fileName);
		
		loadChunkHoppers();
		
//		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::AddItemsToHoppers, 20L, 10L);
	}

	public void onDisable() {
		// on disable
		this.save();
	}
	
	
	private void loadChunkHoppers() {
		if(config.getConfigurationSection("hoppers") == null){
			config.createSection("hoppers");
		}
		
		HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("hoppers").getValues(false);
		try {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				ChunkHopper hopper = new ChunkHopper(entry.getKey(), (String)entry.getValue());
				if(hopper.isChunkHopper()) {
					AddChunkHopper(hopper);
				}
				else {
					RemoveChunkHopper(hopper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void AddChunkHopper(ChunkHopper hopper) {
		if(this.chunkHopperList.containsKey(hopper.getChunkLocationString())) {
			this.chunkHopperList.get(hopper.getChunkLocationString()).add(hopper);
		}
		else {
			this.chunkHopperList.put(hopper.getChunkLocationString(), new ArrayList<>(Collections.singletonList(hopper)));
		}
		
		this.addHologram(hopper);
		this.config.set("hoppers." + hopper.getLocationString(), hopper.getDataString());
		this.save();
	}
	
	private void RemoveChunkHopper(ChunkHopper hopper) {
		if(this.chunkHopperList.containsKey(hopper.getChunkLocationString())) {
			this.chunkHopperList.get(hopper.getChunkLocationString()).remove(hopper);
		}
		this.deleteHologram(hopper);
		this.config.set("hoppers." + hopper.getLocationString(), null);
		this.save();
	}
	
	
	private void addHologram(ChunkHopper hopper) {
		Holograms.AddHologram(hopper.getHologramLocation(), new ItemStack(Messages.hopperHologramMaterial),
				Messages.hopperHologramText(hopper.getHopperGrade(), hopper.getHopperOwner()));
	}
	
	private void deleteHologram(ChunkHopper hopper) {
		Holograms.RemoveHologram(hopper.getHologramLocation());
	}
	
	
	private ChunkHopper getChunkHopperAt(Location location) {
		if(this.chunkHopperList.containsKey(Utils.ChunkToString(location.getChunk()))) {
			return this.chunkHopperList.get(Utils.ChunkToString(location.getChunk())).stream()
			 	.filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
		}
		return null;
	}
	
	public ItemStack createChunkHopperItemStack(int grade) {

		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messages.customHopperDisplayName(grade));
		meta.setLore(Messages.customHopperLore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "chunk hopper", "hopper");
		return item;
	}
	
	//
	//
	// **** EVENTS ****
	//
	//

/*	@EventHandler
	public void OnItemDrop(ItemSpawnEvent event) {
		//convert the chunk to a string
		String chunkString = Utils.ChunkToString(event.getLocation().getChunk());
		
		//check if at least one chunkhopper is in this chunk
		final List<ChunkHopper> hoppers = this.chunkHopperList.get(chunkString);
		if(hoppers == null || hoppers.size() == 0) return;
		
		//item to add
		ItemStack item = event.getEntity().getItemStack();
		int maxStack = item.getMaxStackSize();
		int amountToAdd = item.getAmount();
		//check if it's not more than the max stack size (can happen by wildstacker)
		int amount = WildStackerAPI.getItemAmount(event.getEntity());
		if(amount > amountToAdd) {
			amountToAdd = amount;
		}
		
		int amountLeft = this.addItemToList(chunkString, item, amountToAdd);
		if(amountLeft > 0) {
			event.getEntity().getItemStack().setAmount(amountLeft);
		}
		else {
			event.setCancelled(true);
		}
	}	
	
	private int addItemToList(String chunkString, ItemStack item, int amount) {
		int amountToAdd = amount;
		//check if the list contains this chunk
		if(this.itemsToAdd.containsKey(chunkString)) {
			//get the items that are already in this list
			ArrayList<ItemStack> currentItemsInList = this.itemsToAdd.get(chunkString);
			
			//iterate over the items in the list to see if we can add to them.
			Iterator<ItemStack> it = currentItemsInList.iterator();
			while(it.hasNext()) {
				ItemStack itemStack = it.next();
				//if the item to add is not the same as in the list, continue
				if(!itemStack.isSimilar(item)) {
					continue;
				}
				//if the item already has maxStack, continue
				if(itemStack.getAmount() == itemStack.getMaxStackSize()) {
					continue;
				}
				int amountInStack = itemStack.getAmount();
				int maxAmount = itemStack.getMaxStackSize();
				//if we need to add more than we can add to this stack, set the stack to max amount and continue
				if(amountToAdd + amountInStack > maxAmount) {
					itemStack.setAmount(maxAmount);
					amountToAdd -= (maxAmount - amountInStack);
				}
				else {
					itemStack.setAmount(amountInStack + amountToAdd);
					return 0;
				}
			}
			//if we get here, it means we can't add more to the current items in the list.
			//so we need to check if there are enough chunkhoppers in this chunk so we can add more itemstacks.
			int chunkHopperSlotAmount = this.chunkHopperList.get(chunkString).size() * 5;
			while(currentItemsInList.size() < chunkHopperSlotAmount) {
				//if the amount to add is less than the max stack size, add it and return 0.
				if(amountToAdd <= item.getMaxStackSize()) {
					item.setAmount(amountToAdd);
					this.itemsToAdd.get(chunkString).add(item);
					return 0;
				}
				//else add a full stack and decrease the amount to add
				else {
					item.setAmount(item.getMaxStackSize());
					this.itemsToAdd.get(chunkString).add(item);
					amountToAdd -= item.getMaxStackSize();
				}
			}
			//if we get here, it means we cant add any more stacks to the list
			//because there are not enough chunkhoppers in it. So we return the amount that we cant add.
			return amountToAdd;
		}
		else {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			items.add(item);
			this.itemsToAdd.put(chunkString, items);
			return 0;
		}
	}
	
	private void AddItemsToHoppers() {
		if(this.itemsToAdd.isEmpty()) return;
		Iterator<String> keyIterator = this.itemsToAdd.keySet().iterator();
		HashMap<String, ArrayList<ItemStack>> remainingItems = new HashMap<>();
		
		while(keyIterator.hasNext()) {
			String chunkString = keyIterator.next();
			//check if at least one chunkhopper is in this chunk -- it might have been removed in the meanwhile
			final List<ChunkHopper> hoppers = this.chunkHopperList.get(chunkString);
			if(hoppers == null || hoppers.size() == 0) {
				keyIterator.remove();
				return;
			}
			
			ArrayList<ItemStack> itemsToAdd = this.itemsToAdd.get(chunkString);
			BlockState[] chunkBlockStates = hoppers.get(0).getChunk().getTileEntities();
			
			Iterator<ChunkHopper> it = hoppers.iterator();
			while(it.hasNext()) {
				ChunkHopper chunkHopper = it.next();
				for (BlockState bs : chunkBlockStates) {
					if(bs instanceof Hopper) {
						Hopper hopper = (Hopper) bs;
						//here we found a chunkhopper.
						if(bs.getLocation().equals(chunkHopper.getLocation())) {
							if(itemsToAdd.size() == 0) {
								keyIterator.remove();
								return;
							}

							ItemStack item = itemsToAdd.remove(itemsToAdd.size()-1);
							int maxStack = item.getMaxStackSize();
							int amountToAdd = item.getAmount();
							
							//get all item stacks in the hopper
							ItemStack[] itemsInHopper = hopper.getInventory().getContents();
				
							//loop trough all items in the hopper
							for (ItemStack itemInHopper : itemsInHopper) {			
								if(itemInHopper == null) continue;  // null check
								if(!itemInHopper.isSimilar(item)) continue;  //if the item in the hopper isn't similar to the dropped item, continue to next item;
								if(itemInHopper.getAmount() == itemInHopper.getMaxStackSize()) continue; //if the current item stack is full, continue to next item
								
								//if we reach this the items are similar and the stack isn't full
								int possibleToAdd = maxStack - itemInHopper.getAmount();
								
								//if the amount to add is equal or less than we can add to this stack, add it to the stack and return.
								if(amountToAdd <= possibleToAdd) {
									itemInHopper.setAmount(itemInHopper.getAmount() + amountToAdd);
									chunkHopper.updateInventoryViewers(hopper);
									amountToAdd = 0;
								}
								//if amount to add is more than we can add to this stack
								else {
									itemInHopper.setAmount(maxStack);
									chunkHopper.updateInventoryViewers(hopper);
									amountToAdd -= possibleToAdd;
								}
							}
							while(amountToAdd > 0 && hopper.getInventory().firstEmpty() != -1) {
								//if the amount to add is the same as the max stack or more, add the max stack to the hopper, decrease amount to add and find another empty slot
								if(amountToAdd >= maxStack) {
									item.setAmount(maxStack);
									hopper.getInventory().addItem(item);
									chunkHopper.updateInventoryViewers(hopper);
									amountToAdd -= maxStack;
								}
								// amount to add is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
								else {
									item.setAmount(amountToAdd);
									hopper.getInventory().addItem(item);
									chunkHopper.updateInventoryViewers(hopper);
									keyIterator.remove();
									return;
								}
							}
						}
					}
				}
			}
			//if we reach this part, it means all hoppers are full; put the items back into the list
			remainingItems.put(chunkString, itemsToAdd);
		}
		this.itemsToAdd.clear();
		this.itemsToAdd.putAll(remainingItems);
	}
*/	
	
	@EventHandler
	public void OnItemDrop(ItemSpawnEvent event) {
		final List<ChunkHopper> hoppers = this.chunkHopperList.get(Utils.ChunkToString(event.getLocation().getChunk()));
		if(hoppers == null || hoppers.size() == 0) return;
		
		//item to add
		ItemStack item = event.getEntity().getItemStack();
		int maxStack = item.getMaxStackSize();
		int amountToAdd = item.getAmount();
		//check if it's not more than the max stack size (can happen by wildstacker)
		int amount = WildStackerAPI.getItemAmount(event.getEntity());
		if(amount > amountToAdd) {
			amountToAdd = amount;
		}
		
		BlockState[] bs = event.getLocation().getChunk().getTileEntities();
		
		//loop trough all hoppers
		for (ChunkHopper customHopper : hoppers) {
			//get all item stacks in the hopper
			BlockState hopperState = null;
			for(BlockState bs1 : bs) {
				if(bs1 instanceof Hopper) {
					if(bs1.getLocation().equals(customHopper.getLocation())) {
						hopperState = bs1;
					}
				}
			}
			if(hopperState == null) continue;
			Hopper hopper = (Hopper) hopperState;
			Inventory inv = hopper.getInventory();
			if(inv == null) { 
				System.out.println("Inventory is null of hopper at " + Utils.LocationToString(hopper.getLocation()));
				continue;
			}
			
			//loop trough all inventory slots
			for (int i = 0; i < inv.getSize(); i++) {
				ItemStack itemInHopper = inv.getItem(i);			
				//if the slot is empty, add the item to it.
				if(itemInHopper == null || itemInHopper.getType() == Material.AIR) {
					//if the amount to add is the same as the max stack or more, add the max stack to the hopper, decrease amount to add and find another empty slot
					if(amountToAdd >= maxStack) {
						item.setAmount(maxStack);
						inv.setItem(i, item);
						customHopper.updateInventoryViewers(hopper);
						amountToAdd -= maxStack;
						continue;
					}
					// amount to add is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
					else {
						item.setAmount(amountToAdd);
						inv.setItem(i, item);
						customHopper.updateInventoryViewers(hopper);
						event.setCancelled(true);
						return;
					}
				}
				if(!itemInHopper.isSimilar(item)) continue;  //if the item in the hopper isn't similar to the dropped item, continue to next item;
				if(itemInHopper.getAmount() == itemInHopper.getMaxStackSize()) continue; //if the current item stack is full, continue to next item
				
				//if we reach here it's not an empty slot, and it's the same item.
				int possibleToAdd = maxStack - itemInHopper.getAmount();
				
				//if the amount to add is equal or less than we can add to this stack, add it to the stack and return.
				if(amountToAdd <= possibleToAdd) {
					itemInHopper.setAmount(itemInHopper.getAmount() + amountToAdd);
					customHopper.updateInventoryViewers(hopper);
					event.setCancelled(true);
					return;
				}
				//if amount to add is more than we can add to this stack
				else {
					itemInHopper.setAmount(maxStack);
					customHopper.updateInventoryViewers(hopper);
					amountToAdd -= possibleToAdd;
				}
				
			}
		}
		//if we reach this part, it means all hoppers are full; Drop the items on the ground.
		event.getEntity().getItemStack().setAmount(amountToAdd);
	}

//			ItemStack[] itemsInHopper = inv.getContents();
//			//loop trough all items in the hopper
//			for (ItemStack itemInHopper : itemsInHopper) {			
//				if(itemInHopper == null) continue;  // null check
//				if(!itemInHopper.isSimilar(item)) continue;  //if the item in the hopper isn't similar to the dropped item, continue to next item;
//				if(itemInHopper.getAmount() == itemInHopper.getMaxStackSize()) continue; //if the current item stack is full, continue to next item
//				
//				//if we reach this the items are similar and the stack isn't full
//				int possibleToAdd = maxStack - itemInHopper.getAmount();
//				
//				//if the amount to add is equal or less than we can add to this stack, add it to the stack and return.
//				if(amountToAdd <= possibleToAdd) {
//					itemInHopper.setAmount(itemInHopper.getAmount() + amountToAdd);
//					customHopper.updateInventoryViewers(hopper);
//					event.setCancelled(true);
//					return;
//				}
//				
//				//if amount to add is more than we can add to this stack
//				else {
//					itemInHopper.setAmount(maxStack);
//					customHopper.updateInventoryViewers(hopper);
//					amountToAdd -= possibleToAdd;
//				}
//			}
//			//if we reach this part, all occupied slots are either a different item or full stacks, so we need to add to empty slots. 
//			while(amountToAdd > 0) {
//				//check if the hopper still has an empty slot, if so, break out of the loop and continue to the next hopper in this chunk.
//				int firstEmpty = inv.firstEmpty();
//				if(firstEmpty == -1) break;
//				
//				//if the amount to add is the same as the max stack or more, add the max stack to the hopper, decrease amount to add and find another empty slot
//				if(amountToAdd >= maxStack) {
//					item.setAmount(maxStack);
//					inv.addItem(item);
//					customHopper.updateInventoryViewers(hopper);
//					amountToAdd -= maxStack;
//				}
//				// amount to add is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
//				else {
//					item.setAmount(amountToAdd);
//					inv.addItem(item);
//					customHopper.updateInventoryViewers(hopper);
//					event.setCancelled(true);
//					return;
//				}
//			}
	
//	@EventHandler
//	public void OnItemDrop(ItemSpawnEvent event) {
//		final List<ChunkHopper> hoppers = this.chunkHopperList.get(Utils.ChunkToString(event.getLocation().getChunk()));
//		if(hoppers == null || hoppers.size() == 0) return;
//		
//		//item to add
//		ItemStack item = event.getEntity().getItemStack();
//		int maxStack = item.getMaxStackSize();
//		int amountToAdd = item.getAmount();
//		//check if it's not more than the max stack size (can happen by wildstacker)
//		int amount = WildStackerAPI.getItemAmount(event.getEntity());
//		if(amount > amountToAdd) {
//			amountToAdd = amount;
//		}
//		
//		
//		//loop trough all hoppers
//		for (ChunkHopper customHopper : hoppers) {
//			//get all item stacks in the hopper
//			ItemStack[] itemsInHopper = customHopper.getInventory().getContents();
//
//			//loop trough all items in the hopper
//			for (ItemStack itemInHopper : itemsInHopper) {			
//				if(itemInHopper == null) continue;  // null check
//				if(!itemInHopper.isSimilar(item)) continue;  //if the item in the hopper isn't similar to the dropped item, continue to next item;
//				if(itemInHopper.getAmount() == itemInHopper.getMaxStackSize()) continue; //if the current item stack is full, continue to next item
//				
//				//if we reach this the items are similar and the stack isn't full
//				int possibleToAdd = maxStack - itemInHopper.getAmount();
//				
//				//if the amount to add is equal or less than we can add to this stack, add it to the stack and return.
//				if(amountToAdd <= possibleToAdd) {
//					itemInHopper.setAmount(itemInHopper.getAmount() + amountToAdd);
//					customHopper.updateInventoryViewers();
//					event.setCancelled(true);
//					return;
//				}
//				
//				//if amount to add is more than we can add to this stack
//				else {
//					itemInHopper.setAmount(maxStack);
//					customHopper.updateInventoryViewers();
//					amountToAdd -= possibleToAdd;
//				}
//			}
//			//if we reach this part, all occupied slots are either a different item or full stacks, so we need to add to empty slots. 
//			while(amountToAdd > 0) {
//				//check if the hopper still has an empty slot, if so, break out of the loop and continue to the next hopper in this chunk.
//				int firstEmpty = customHopper.getInventory().firstEmpty();
//				if(firstEmpty == -1) break;
//				
//				//if the amount to add is the same as the max stack or more, add the max stack to the hopper, decrease amount to add and find another empty slot
//				if(amountToAdd >= maxStack) {
//					item.setAmount(maxStack);
//					customHopper.getInventory().addItem(item);
//					customHopper.updateInventoryViewers();
//					amountToAdd -= maxStack;
//				}
//				// amount to add is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
//				else {
//					item.setAmount(amountToAdd);
//					customHopper.getInventory().addItem(item);
//					customHopper.updateInventoryViewers();
//					event.setCancelled(true);
//					return;
//				}
//			}
//		}
//		//if we reach this part, it means all hoppers are full; Drop the items on the ground.
//		event.getEntity().getItemStack().setAmount(amountToAdd);
//	}
	
	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void HopperPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)){
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			String tag = Utils.getNBTTag(item, "chunk hopper");
			if(tag == null) return;
			if(tag.equals("")) return;
			
			ChunkHopper hopper = new ChunkHopper(event.getBlock().getLocation(), event.getPlayer());
			this.AddChunkHopper(hopper);
		}
		//check if there is a block placed above a void chest, and if so remove the hologram.
		else {
			ChunkHopper hopper = getChunkHopperAt(event.getBlock().getLocation().clone().add(0d,-1d,0d));
			if(hopper != null) {
				Holograms.RemoveHologram(hopper.getHologramLocation());
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void HopperBreakEvent(BlockBreakEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)) {
			ChunkHopper hopper = getChunkHopperAt(event.getBlock().getLocation());
			if(hopper != null) {
				event.setDropItems(false);
				
				this.RemoveChunkHopper(hopper);
				
				Player player = event.getPlayer();
				if(player.getInventory().firstEmpty() == -1) {
					//check if player already has the item
					List<ItemStack> items = Arrays.asList(player.getInventory().getContents());
					ItemStack item = items.stream().filter(x -> x != null && x.getType() != Material.AIR &&
							x.isSimilar(this.createChunkHopperItemStack(1))).findFirst().orElse(null);
					//item is in inventory
					if(item != null) {
						//if item stack is not full, add item
						if(item.getAmount() < item.getMaxStackSize()) {
							player.getInventory().addItem(this.createChunkHopperItemStack(1));
							return;
						}
					}
					event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), this.createChunkHopperItemStack(1));
					
				}
				else {
					player.getInventory().addItem(this.createChunkHopperItemStack(1));
				}
				
			}
		}
		else {
			ChunkHopper hopper = getChunkHopperAt(event.getBlock().getLocation().clone().add(0d,-1d,0d));
			if(hopper != null) {
				this.addHologram(hopper);
			}
		}
	}
	
	
	
	
	//
	//
	// **** COMMANDS ****
	//	
	//
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			if(args.length != 2) {
				Bukkit.getLogger().warning(Messages.invalidChunkHopperArguments); 
				return true;}
			
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			
			if(targetPlayer != null && targetPlayer.isOnline()) {					
				int amount = Utils.tryParseInt(args[1]);
				if(amount != -1) {
					ItemStack customHopper = this.createChunkHopperItemStack(1);
					customHopper.setAmount(amount);
					if(targetPlayer.getInventory().firstEmpty() != -1) {
						targetPlayer.getInventory().addItem(customHopper);
						targetPlayer.sendMessage(Messages.youReceivedCustomHopperMessage);
						Bukkit.getLogger().warning(Messages.gaveHopperToPlayerMessage(targetPlayer, amount));
						return true;
						
					} 
					targetPlayer.getLocation().getWorld().dropItemNaturally(targetPlayer.getLocation(), customHopper);
					targetPlayer.sendMessage(Utils.colorize("&cYour inventory was full. Your ChunkHopper(s) has/have been dropped on the floor."));
					return true;
				} 	
				Bukkit.getLogger().warning(Messages.invalidAmount);	
				return true;
			} 
			Bukkit.getLogger().warning(Messages.invalidTargetPlayer); 
			return true;
		}
		else {
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
						ItemStack customHopper = this.createChunkHopperItemStack(1);
						customHopper.setAmount(amount);
						targetPlayer.getInventory().addItem(customHopper);
						targetPlayer.sendMessage(Messages.youReceivedCustomHopperMessage);
						player.sendMessage(Messages.gaveHopperToPlayerMessage(targetPlayer, amount));
						return true;
						
					} 
					player.sendMessage(Messages.inventoryFull(targetPlayer)); 
					return true;
				} 	
				player.sendMessage(Messages.invalidAmount);	
				return true;
			} 
			player.sendMessage(Messages.invalidTargetPlayer); 
			return true;
		}

		
	}
	
}
