package me.mafkees92.CustomHoppers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.mafkees92.Main;
import me.mafkees92.Files.BaseFile;
import me.mafkees92.Utils.Utils;

public class HopperData extends BaseFile {
	

	public HashMap<String, List<CustomHopper>> customHoppers;
	

	public HopperData(Main plugin, String fileName) {
		super(plugin, fileName);
		LoadHopperData();
	}
	
	public void AddHopperData(CustomHopper hopper) {

		if(customHoppers != null) {
			if(customHoppers.containsKey(hopper.getChunkLocationString())) {
				customHoppers.get(hopper.getChunkLocationString()).add(hopper);
			}
			else{
				List<CustomHopper> hopperList = new ArrayList<CustomHopper>();
				hopperList.add(hopper);
				customHoppers.put(hopper.getChunkLocationString(), hopperList);
			}
		}
		LoadHopperHologram(hopper);
		config.set("hoppers."+ hopper.toString(), "");
		//Bukkit.getScheduler().runTaskTimer(plugin, () -> SellItemsInHopper(hopper), 20, 200);
		save();
	}

	public boolean removeHopper(CustomHopper hopperToRemove) {
		if(customHoppers == null)
			return false;
		
		List<CustomHopper> hoppers = customHoppers.get(hopperToRemove.getChunkLocationString());
		CustomHopper hopper = hoppers.stream().filter(x -> x.getLocation().equals(hopperToRemove.getLocation())).findFirst().orElse(null);
		if(hopper == null)
			return false;
		else {
			if(hoppers.size() == 1) 
				customHoppers.remove(hopperToRemove.getChunkLocationString());
			else 
				customHoppers.get(hopperToRemove.getChunkLocationString()).removeIf(x -> x.getLocation().equals(hopperToRemove.getLocation()));
			
			config.set("hoppers." + hopper.toString(), null);
			RemoveHopperHologram(hopperToRemove);
			save();
			return true;
		}
	}
	
	public void LoadHopperData() {

        if (config.getConfigurationSection("hoppers") == null) {
            config.createSection("hoppers"); 
        }
        
        if(customHoppers == null) 
        	customHoppers = new HashMap<String, List<CustomHopper>>();
        
    	HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("hoppers").getValues(false);
    	try {
			Bukkit.getLogger().warning("size of map: " + map.size());
    		for (String line : map.keySet()) {
				Location loc = Utils.StringToLocation(line);
				if(loc != null) {
					CustomHopper hopper = new CustomHopper(loc);
					AddHopperData(hopper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void LoadHopperHologram(CustomHopper hopper) {
		Hologram hopperHolo = HologramsAPI.createHologram(plugin, hopper.getHologramLocation());
		hopperHolo.appendTextLine(Utils.colorize("&3&lChunk Hopper"));
		hopperHolo.appendItemLine(new ItemStack(Material.HOPPER));
	}
	
	public void RemoveHopperHologram(CustomHopper hopper) {
		Hologram hopperHolo = HologramsAPI.getHolograms(plugin).stream()
				.filter(x ->x.getLocation().equals(hopper.getHologramLocation())).findFirst().orElse(null);
		if(hopperHolo != null) {
			Bukkit.getPlayer("Mafkees92").sendMessage("Removing hopper holo");
			hopperHolo.delete();
		}
		else {
			Bukkit.getPlayer("Mafkees92").sendMessage("hopper holo is null");
		}
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
	public boolean containsHopperInChunk(Location location) {
		if(customHoppers == null) 
			return false;
		if(customHoppers.containsKey(Utils.LocationToChunkString(location)))
			return true;
		return false;
	}
	
	public boolean isChunkHopper(Location location) {
		if(customHoppers == null) return false;
		
		if(customHoppers.containsKey(Utils.LocationToChunkString(location) )) {
			List<CustomHopper> hoppers = customHoppers.get(Utils.LocationToChunkString(location) );
			CustomHopper hopper = hoppers.stream().filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
			if(hopper == null) 
				return false;
			else 
				return true;
		}
		return false;
	}
	
	public List<CustomHopper> getHoppersInChunk(Location location){
		
		if(customHoppers == null) return null;
		if(!customHoppers.containsKey(Utils.LocationToChunkString(location))) return null;
		List<CustomHopper> hoppersInChunk = customHoppers.get(Utils.LocationToChunkString(location));
		return hoppersInChunk;
	}
	
	public void printChunkHoppers() {
		Bukkit.broadcastMessage(customHoppers.toString());
	}
}


