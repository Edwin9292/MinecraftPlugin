package me.mafkees92.VoidChests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.brcdev.shopgui.ShopGuiPlusApi;

public class VoidChest {
	
	
	private final Location location;
	private ShulkerBox chest;
	private Inventory inventory;
	private OfflinePlayer inventoryOwner;
	private Double moneyToPayOut;
	private int chestGrade;
	private List<TextLine> hologramTextLines = new ArrayList<>();
	private boolean areTextLinesSet = false;
	private int sellTimeInterval;
	private BukkitTask sellIntervalTask;
	private double sellBooster = 1.0;

	private Vector hologramOffset;

	public VoidChest(String location, String data) {
		
		this.location = Utils.StringToLocation(location);
		
		if(data != null && !data.equals("")) {
			// 0 = inventoryowner; 1 = moneyToPayOut; 2 = chestgrade
			String[] split = data.split("[|]");
			this.inventoryOwner = Bukkit.getOfflinePlayer(UUID.fromString(split[0]));
			this.moneyToPayOut = Utils.tryParseDouble(split[1]);
			this.chestGrade = Utils.tryParseInt(split[2]);
		}
		initialize();
	}
	
	public VoidChest(Location location, Player inventoryOwner, int chestGrade) {
		this.location = location;
		this.inventoryOwner = inventoryOwner;
		this.chestGrade = chestGrade;
		this.moneyToPayOut = 0d;
		initialize() ;
	}
	
	private void initialize() {
		if (this.location != null) {
			this.chest = (ShulkerBox) this.location.getBlock().getState();
			this.inventory = this.chest.getInventory();
			this.sellTimeInterval = this.gradeToSellTime(this.chestGrade);
			
			if(this.sellTimeInterval > 0) {
				this.sellIntervalTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> this.sellContents(), sellTimeInterval, sellTimeInterval);
			}
		}
		if(this.inventoryOwner == null || this.moneyToPayOut == -1 || this.chestGrade == -1 || this.chest == null) {
			Bukkit.getLogger().warning(Utils.colorize("FAILED PARSING DATA TO VOIDCHEST, DISABELING PLUGIN"));
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
		}
		setSellBooster();
		setHologramOffset();
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
	
	private void setSellBooster() {
		if(this.chestGrade > 5) {
			if(this.chestGrade == 6) this.sellBooster = 1.05d;
			if(this.chestGrade == 7) this.sellBooster = 1.1d;
		}
		else {
			this.sellBooster = 1.0d;
		}
	}
	
	private void setHologramOffset() {
		if (this.chestGrade > 5) 
			this.hologramOffset = new Vector(0.5d, 2.6d, 0.5d);
		else 
			this.hologramOffset = new Vector(0.5d, 2.35d, 0.5d);
		
	}
	
	public void sellContents() {
		ItemStack[] itemsInInventory = this.inventory.getContents();
		double money = 0;
		
		for(ItemStack item : itemsInInventory) {
			if(item == null || item.getType().equals(Material.AIR)) continue;
			double sellprice = ShopGuiPlusApi.getItemStackPriceSell(item);
			if(sellprice > 0) {
				sellprice *= this.sellBooster;
				money += sellprice;
				this.inventory.remove(item);
			}
		}
		this.moneyToPayOut += money;
		updateHologram();
	}

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
		return this.inventory;
	}

	public OfflinePlayer getInventoryOwner() {
		return inventoryOwner;
	}
	
	public int getChestGrade() {
		return this.chestGrade;
	}

	public String getMoneyInChestTextLine() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyInChestTextLine = "&7Holding: &6{money}";
		return moneyInChestTextLine.replace("{money}", formatter.format(this.moneyToPayOut));
	}

	public double getSellBooster() {
		return sellBooster;
	}

	public Vector getHologramOffset() {
		return hologramOffset;
	}
	
}
