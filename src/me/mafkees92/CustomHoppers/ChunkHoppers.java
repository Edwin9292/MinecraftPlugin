package me.mafkees92.CustomHoppers;

import me.mafkees92.Files.BaseFile;
import me.mafkees92.Files.Messages;
import me.mafkees92.Holograms;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ChunkHoppers extends BaseFile implements Listener, CommandExecutor{

	

	private final HashMap<String, List<ChunkHopper>> chunkHopperList = new HashMap<>();
	
	
	
	public ChunkHoppers(Main plugin, String fileName) {
		super(plugin, fileName);
		
		loadChunkHoppers();
	}

	public void onDisable() {
		// on disable
		this.save();
	}
	
	
	private void loadChunkHoppers() {
		if(config.getConfigurationSection("void chests") == null){
			config.createSection("void chests");
		}
		
		HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("void chests").getValues(false);
		try {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				ChunkHopper hopper = new ChunkHopper(entry.getKey(), (String)entry.getValue());
				AddChunkHopper(hopper);
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
		Holograms.AddHologram(hopper.getHologramLocation(), new ItemStack(Material.HOPPER),  Utils.colorize("&b&lChunk Hopper"), "");
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
	
	public ItemStack createChunkHopperItemStack() {

		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colorize("&o&3&lChunkHopper"));
		List<String> lore = new ArrayList<>();
		lore.add(Utils.colorize("&7When placed, this hopper will suck"));
		lore.add(Utils.colorize("&7up every item that drops inside"));
		lore.add(Utils.colorize("&7its chunk."));
		lore.add(Utils.colorize("&o"));
		lore.add(Utils.colorize("&d&lEPIC"));
		meta.setLore(lore);
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
	
	@EventHandler
	public void OnItemDrop(ItemSpawnEvent event) {
		List<ChunkHopper> hoppers = this.chunkHopperList.get(Utils.ChunkToString(event.getLocation().getChunk()));
		if(hoppers == null || hoppers.size() == 0) return;
		
		//item to add
		ItemStack item = event.getEntity().getItemStack();
		int maxStack = item.getMaxStackSize();
		int amountToAdd = item.getAmount();
		
		//loop trough all hoppers
		for (ChunkHopper customHopper : hoppers) {
			//get all item stacks in the hopper
			ItemStack[] itemsInHopper = customHopper.getInventory().getContents();

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
					customHopper.updateInventoryViewers();
					event.setCancelled(true);
					return;
				}
				
				//if amount to add is more than we can add to this stack
				else {
					itemInHopper.setAmount(maxStack);
					customHopper.updateInventoryViewers();
					amountToAdd -= possibleToAdd;
				}
			}
			//if we reach this part, all occupied slots are either a different item or full stacks, so we need to add to empty slots. 
			while(amountToAdd > 0) {
				//check if the hopper still has an empty slot, if so, break out of the loop and continue to the next hopper in this chunk.
				int firstEmpty = customHopper.getInventory().firstEmpty();
				if(firstEmpty == -1) break;
				
				//if the amount to add is the same as the max stack or more, add the max stack to the hopper, decrease amount to add and find another empty slot
				if(amountToAdd >= maxStack) {
					item.setAmount(maxStack);
					customHopper.getInventory().addItem(item);
					customHopper.updateInventoryViewers();
					amountToAdd -= maxStack;
				}
				// amount to add is less than a maxStack so we can add an item to the hopper with the amount to add. then we are done, cancel dropping the item and return. 
				else {
					item.setAmount(amountToAdd);
					customHopper.getInventory().addItem(item);
					customHopper.updateInventoryViewers();
					event.setCancelled(true);
					return;
				}
			}
		}
		//if we reach this part, it means all hoppers are full; Drop the items on the ground.
		event.getEntity().getItemStack().setAmount(amountToAdd);
	}
	
	@EventHandler
	public void HopperPlaceEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType().equals(Material.HOPPER)){
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			String tag = Utils.getNBTTag(item, "chunk hopper");
			if(tag == null) return;
			if(tag.equals("")) return;
			
			ChunkHopper hopper = new ChunkHopper(event.getBlock().getLocation());
			this.AddChunkHopper(hopper);
		}
	}
	
	@EventHandler
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
							x.isSimilar(this.createChunkHopperItemStack())).findFirst().orElse(null);
					//item is in inventory
					if(item != null) {
						//if item stack is not full, add item
						if(item.getAmount() < item.getMaxStackSize()) {
							player.getInventory().addItem(this.createChunkHopperItemStack());
							return;
						}
					}
					event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), this.createChunkHopperItemStack());
					
				}
				else {
					player.getInventory().addItem(CustomHoppers.getChunkHopperItemStack());
				}
				
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
					ItemStack customHopper = CustomHoppers.getChunkHopperItemStack();
					customHopper.setAmount(amount);
					targetPlayer.getInventory().addItem(customHopper);
					targetPlayer.sendMessage("You have received a custom hopper");
					player.sendMessage("Gave "+ targetPlayer.getName() + " " + amount + " custom hopper(s)");
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
