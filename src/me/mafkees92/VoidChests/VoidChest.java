package me.mafkees92.VoidChests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.brcdev.shopgui.ShopGuiPlusApi;

public class VoidChest {
	
	private VoidChests voidChestsInstance;
	
	private final Location location;
	//private ShulkerBox chest;
	private OfflinePlayer inventoryOwner;
	private Double moneyToPayOut;
	private int chestGrade;
	private List<TextLine> hologramTextLines = new ArrayList<>();
	private boolean areTextLinesSet = false;
	private int sellTimeInterval;
	private BukkitTask sellIntervalTask;
	private final double sellBooster;

	private Vector hologramOffset;

	public VoidChest(String location, String data, VoidChests voidChestsInstance) {
		
		this.voidChestsInstance = voidChestsInstance;
		this.location = Utils.StringToLocation(location);
		
		if(data != null && !data.equals("")) {
			// 0 = inventoryowner; 1 = moneyToPayOut; 2 = chestgrade
			String[] split = data.split("[|]");
			this.inventoryOwner = Bukkit.getOfflinePlayer(UUID.fromString(split[0]));
			this.moneyToPayOut = Utils.tryParseDouble(split[1]);
			this.chestGrade = Utils.tryParseInt(split[2]);
		}

		if(this.chestGrade == 6) this.sellBooster = 1.05d;
		else if(this.chestGrade == 7) this.sellBooster = 1.1d;
		else this.sellBooster = 1.0d;
	}
	
	public VoidChest(Location location, Player inventoryOwner, int chestGrade) {
		this.location = location;
		this.inventoryOwner = inventoryOwner;
		this.chestGrade = chestGrade;
		this.moneyToPayOut = 0d;
		
		if(this.chestGrade == 6) this.sellBooster = 1.05d;
		else if(this.chestGrade == 7) this.sellBooster = 1.1d;
		else this.sellBooster = 1.0d;
	}
	
	public boolean initialize() {
		if (this.location != null) {

			setHologramOffset();
			
			if(!(this.location.getBlock().getState() instanceof ShulkerBox)) {
				Bukkit.getLogger().severe("ERROR: location X: " + this.location.getBlockX() +  
						" Y: " + this.location.getBlockY() + " Z: " + this.location.getBlockZ() + 
						" is not a shulkerbox, could not parse this location into a voidchest.");
				Bukkit.getLogger().severe("The voidchest stored at this location has been removed from the config.");
				this.voidChestsInstance.removeVoidChest(this);
				return false;
			}
			//this.chest = (ShulkerBox) this.location.getBlock().getState();
			this.sellTimeInterval = this.gradeToSellTime(this.chestGrade);
			
			if(this.sellTimeInterval > 0) {
				//this.sellIntervalTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> this.sellContents(), sellTimeInterval, sellTimeInterval);
				this.sellIntervalTask = new BukkitRunnable() {
					
					@Override
					public void run() {
						sellContents();
					}
				}.runTaskTimer(Main.getInstance(), sellTimeInterval, sellTimeInterval);
			}
		}
		if(this.inventoryOwner == null || this.moneyToPayOut == -1 || this.chestGrade == -1 || (ShulkerBox) this.location.getBlock().getState() == null) {
			Bukkit.getLogger().warning(Utils.colorize("FAILED PARSING DATA TO VOIDCHEST, DISABELING PLUGIN"));
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
		}
		return true;
	}
	
	
	public void destroy() {
		if(this.sellIntervalTask != null) {
			this.sellIntervalTask.cancel();
		}
	}
	
	public void setHologramTextLines(List<TextLine> textLines) {
		this.hologramTextLines = textLines;
		this.areTextLinesSet = true;
	}
	
	private void updateHologram() {
		if(this.areTextLinesSet) {
			TextLine value = this.hologramTextLines.get(1);
			value.setText(Utils.colorize(this.getMoneyInChestTextLine()));
		}
	}
	
	private void setHologramOffset() {
		if (this.chestGrade > 5) 
			this.hologramOffset = new Vector(0.5d, 2.6d, 0.5d);
		else 
			this.hologramOffset = new Vector(0.5d, 2.35d, 0.5d);
		
	}
	
	public void sellContents() {
		
		BlockState[] blockStates = this.getLocation().getChunk().getTileEntities();
		ShulkerBox shulker = null;
		for (int i = 0; i < blockStates.length; i++) {
			if(blockStates[i] instanceof ShulkerBox) {
				if(blockStates[i].getLocation().equals(this.getLocation())) {
					//we got this voidchest
					shulker = (ShulkerBox) blockStates[i];
					Inventory inv = shulker.getInventory();
					if(inv == null) {
						System.out.println("WARNING: VoidChest has no inventory at " + this.getLocationString());
						return;
					}
					
					ItemStack[] itemsInInventory = inv.getContents();
					
					Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ()->{
						double money = 0;
						for(ItemStack item : itemsInInventory) {
							if(item == null || item.getType().equals(Material.AIR)) continue;
							if(item.hasItemMeta()) {
								if(item.getItemMeta().hasDisplayName()) {
									continue;
								}
							}
							double sellprice = ShopGuiPlusApi.getItemStackPriceSell(item);
							if(sellprice > 0) {
								sellprice *= this.sellBooster;
								money += sellprice;
								inv.remove(item);
							}
							else if(item.getType() == Material.BOW) {
								if(item.hasItemMeta()) {
									ItemMeta meta = item.getItemMeta();
									Map<Enchantment, Integer> enchantments = meta.getEnchants();
									money += (enchantments.values().size() * 2.0);
								}
								money+= 5;
								inv.remove(item);
							}
						}
						final double moneyToAdd = money;
						Bukkit.getScheduler().runTask(Main.getInstance(), ()->{
							this.moneyToPayOut += moneyToAdd;
							updateHologram();
						});
					});
				}
			}
		}
		//looped trough all shulkers but none are this voidchest, so we should remove it
		if(shulker == null) {
			voidChestsInstance.removeVoidChest(this);
		}
	}
//		
//		
//		if(!(block.getState() instanceof ShulkerBox)) {
//			voidChestsInstance.removeVoidChest(this);
//			return;
//		}
//		ShulkerBox chest = (ShulkerBox) this.location.getBlock().getState();
//		Inventory inv = chest.getInventory();
//		if(inv == null) {
//			return;
//		}
//		
//		ItemStack[] itemsInInventory = inv.getContents();
//		double money = 0;
//		
//		for(ItemStack item : itemsInInventory) {
//			if(item == null || item.getType().equals(Material.AIR)) continue;
//			if(item.hasItemMeta()) {
//				if(item.getItemMeta().hasDisplayName()) {
//					continue;
//				}
//			}
//			double sellprice = ShopGuiPlusApi.getItemStackPriceSell(item);
//			if(sellprice > 0) {
//				sellprice *= this.sellBooster;
//				money += sellprice;
//				inv.remove(item);
//			}
//			else if(item.getType() == Material.BOW) {
//				if(item.hasItemMeta()) {
//					ItemMeta meta = item.getItemMeta();
//					Map<Enchantment, Integer> enchantments = meta.getEnchants();
//					for (Integer level : enchantments.values()) {
//						money += 2;
//					}
//				}
//				money+= 5;
//				inv.remove(item);
//			}
//		}
//		this.moneyToPayOut += money;
//		updateHologram();

	public double payOut() {
		double money = this.moneyToPayOut;
		this.moneyToPayOut = 0d;
		updateHologram();
		return money;
	}
		
	private int gradeToSellTime(int grade) {
		switch (grade) {
		case 1:
			return 30 * 20;
		case 2:
			return 20 * 20;
		case 3:
			return 10 * 20;
		case 4:
			return 6 * 20;
		case 5:
			return 3 * 20;
		case 6:
			return 3 * 20;
		case 7:
			return 3 * 20;

		default:
			return -1;
		}
	}
	
	public String getDataString() {

		return this.inventoryOwner.getUniqueId().toString() +
				"|" +
				this.moneyToPayOut.toString() +
				"|" +
				this.chestGrade;
	}
	
	public String getLocationString() {
		return Utils.LocationToString(this.location);
	}
	
	public Location getLocation() {
		return this.location;
	}


	public Inventory getInventory() {
		return ((ShulkerBox) this.location.getBlock().getState()).getInventory();
	}

	public OfflinePlayer getInventoryOwner() {
		return inventoryOwner;
	}
	
	public int getChestGrade() {
		return this.chestGrade;
	}

	public String getMoneyInChestTextLine() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyInChestTextLine = "&7Holding: &2$&a{money}";
		return moneyInChestTextLine.replace("{money}", formatter.format(this.moneyToPayOut).replace("$", ""));
	}

	public double getSellBooster() {
		return sellBooster;
	}

	public Vector getHologramOffset() {
		return hologramOffset;
	}
	
}
