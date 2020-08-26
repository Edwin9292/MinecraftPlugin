package me.mafkees92.VoidChests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.brcdev.shopgui.ShopGuiPlusApi;

public class VoidChest {
	
	
	private final Location location;
	private Chest chest;
	private Inventory inventory;
	private OfflinePlayer inventoryOwner;
	private Double moneyToPayOut;
	private int chestGrade;
	private List<TextLine> hologramTextLines = new ArrayList<>();
	private boolean areTextLinesSet = false;
	private int sellTimeInterval;
	private BukkitTask sellIntervalTask;

	public VoidChest(String location, String data) {
		
		this.location = Utils.StringToLocation(location);
		
		if(data != null && !data.equals("")) {
			
			//process data
			// 0 = inventoryowner; 1 = moneyToPayOut; 2 = chestgrade
			String[] split = data.split("[|]");
			this.inventoryOwner = Bukkit.getOfflinePlayer(UUID.fromString(split[0]));
			this.moneyToPayOut = Utils.tryParseDouble(split[1]);
			this.chestGrade = Utils.tryParseInt(split[2]);
			if(this.inventoryOwner == null || this.moneyToPayOut == -1 || this.chestGrade == -1) {
				Bukkit.getLogger().warning(Utils.colorize("FAILED PARSING DATA TO VOIDCHEST"));
			}
			this.sellTimeInterval = 110 - this.chestGrade * 10 > 40 ? 100 - this.chestGrade * 10 : 40;
			if(this.sellTimeInterval > 0) {
				this.sellIntervalTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> this.sellContents(), sellTimeInterval, sellTimeInterval);
			}
			
		}
		if (this.location != null) {
			this.chest = (Chest) this.location.getBlock().getState();
			this.inventory = this.chest.getInventory();
		}
		this.inventoryOwner.getPlayer().sendMessage("timer " + this.sellTimeInterval);
	}
	
	public VoidChest(Location location, Player inventoryOwner, int chestGrade) {
		this.location = location;
		this.chest = (Chest) this.location.getBlock().getState();
		this.inventory = this.chest.getInventory();
		this.inventoryOwner = inventoryOwner;
		this.moneyToPayOut = 0d;
		this.chestGrade = chestGrade;
		this.sellTimeInterval = 110 - this.chestGrade * 10 > 40 ? 100 - this.chestGrade * 10 : 40;
		if(this.sellTimeInterval > 0) {
			this.sellIntervalTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> this.sellContents(), sellTimeInterval, sellTimeInterval);
		}
		this.inventoryOwner.getPlayer().sendMessage("timer " + this.sellTimeInterval);
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
	
	public void sellContents() {
		ItemStack[] itemsInInventory = this.inventory.getContents();
		double money = 0;
		
		for(ItemStack item : itemsInInventory) {
			if(item == null || item.getType().equals(Material.AIR)) continue;
			double sellprice = ShopGuiPlusApi.getItemStackPriceSell(item);
			if(sellprice > 0) {
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
		String moneyInChestTextLine = "&eStored money: &2{money}";
		return moneyInChestTextLine.replace("{money}", formatter.format(this.moneyToPayOut));
	}
	
}
