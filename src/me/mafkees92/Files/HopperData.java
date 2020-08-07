package me.mafkees92.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.mafkees92.Main;
import me.mafkees92.CustomHoppers.CustomHopper;
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
		config.set("hoppers."+ hopper.toString(), "");
		save();
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
					Bukkit.getLogger().warning(hopper.getChunkLocationString());
					if(customHoppers.containsKey(hopper.getChunkLocationString())) {
						customHoppers.get(hopper.getChunkLocationString()).add(hopper);
					}
					else {
						List<CustomHopper> hopperList = new ArrayList<CustomHopper>();
						hopperList.add(hopper);
						customHoppers.put(hopper.getChunkLocationString(), hopperList);
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
			save();
			return true;
		}
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


