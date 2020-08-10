package me.mafkees92.CustomHoppers;

import java.util.List;

import org.bukkit.Location;

import me.mafkees92.Main;
import me.mafkees92.Files.HopperLocations;

public class CustomHoppers {
	
	Main plugin;
	HopperLocations hopperLocations;
	ChunkHopperHolograms hopperHolograms;
	
	public CustomHoppers(Main plugin) {
		this.plugin = plugin;
		this.hopperHolograms = new ChunkHopperHolograms(plugin);
		
		loadHoppers();
	}
	
	private void loadHoppers() {
		hopperLocations = new HopperLocations(plugin, "HopperData.yml");
		List<ChunkHopper> hoppers = hopperLocations.getAllChunkHoppers();
		for(ChunkHopper hopper : hoppers){
			hopperHolograms.LoadHopperHologram(hopper);
		}
	}
	
	public void addHopper(ChunkHopper hopper) {
		hopperLocations.addHopper(hopper);
		hopperHolograms.LoadHopperHologram(hopper);
	}
	
	
	public boolean removeHopper(ChunkHopper hopper) {
		if(hopperLocations.removeHopper(hopper) &&
				hopperHolograms.RemoveHopperHologram(hopper)) {
			return true;
		}
		return false;
	}
	
	public boolean isChunkHopper(Location location) {
		return hopperLocations.isCustomHopper(location);
	}
	
	public List<ChunkHopper> getHoppersInChunk(Location location){
		return hopperLocations.getHoppersInChunk(location);
	}
	
	public void onDisable() {
		hopperLocations.save();
		hopperHolograms.RemoveAllHopperHolograms();
	}
	

	
	/*
	private void SellItemsInHopper(CustomHopper hopper) {
		double moneyMade = 0.0d;
		
		ItemStack[] items = hopper.getInventory().getContents();
		if(!plugin.isShopGuiPlusEnabled()) return;
		for (ItemStack itemStack : items) {
			if(itemStack.hasItemMeta()) continue;
			double stackValue = ShopGuiPlusApi.getItemStackPriceSell(itemStack);
			if(stackValue > 0) {
				moneyMade += stackValue;
				hopper.getInventory().remove(itemStack);
			}
		}
		if(moneyMade > 0.0d) {
			Player mafkees = Bukkit.getPlayer("Mafkees92");
			Main.econ.depositPlayer(mafkees, moneyMade);
			mafkees.sendMessage("You have made " + moneyMade + "&");
		}
	}
	*/
}


