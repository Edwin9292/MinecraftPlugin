package me.mafkees92.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
			if(customHoppers.containsKey(hopper.getChunk().toString())) {
				customHoppers.get(hopper.getChunk().toString()).add(hopper);
			}
			else{
				List<CustomHopper> hopperList = new ArrayList<CustomHopper>();
				hopperList.add(hopper);
				customHoppers.put(hopper.getChunk().toString(), hopperList);
			}
		}
		config.set("hoppers."+ hopper.toString(), "");
		save();
	}
	
	
	public void LoadHopperData() {

        if (config.getConfigurationSection("hoppers") == null) {
            config.createSection("hoppers"); 
        }
        
        if(customHoppers == null) customHoppers = new HashMap<String, List<CustomHopper>>();
        
    	HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("hoppers").getValues(false);
    	try {
    		
    		for (String line : map.keySet()) {
				Location loc = Utils.StringToLocation(line);
				if(loc != null) {
					Chunk chunk = loc.getChunk();
					CustomHopper hopper = new CustomHopper(loc);
					if(customHoppers.containsKey(chunk.toString())) {
						customHoppers.get(chunk.toString()).add(hopper);
					}
					else {
						List<CustomHopper> hopperList = new ArrayList<CustomHopper>();
						hopperList.add(hopper);
						customHoppers.put(chunk.toString(), hopperList);
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean containsHopperInChunk(Chunk chunk, Player player) {
		if(customHoppers == null) 
			return false;
		if(customHoppers.containsKey(chunk.toString()))
			return true;
		return false;
	}
	
	public boolean isChunkHopper(Location location) {
		if(customHoppers == null) return false;
		
		if(customHoppers.containsKey(location.getChunk().toString())) {
			List<CustomHopper> hoppers = customHoppers.get(location.getChunk().toString());
			CustomHopper hopper = hoppers.stream().filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
			if(hopper == null) 
				return false;
			else 
				return true;
		}
		return false;
	}
	
	public boolean removeHopper(Location hopperLocation) {
		if(customHoppers == null)
			return false;
		
		List<CustomHopper> hoppers = customHoppers.get(hopperLocation.getChunk().toString());
		CustomHopper hopper = hoppers.stream().filter(x -> x.getLocation().equals(hopperLocation)).findFirst().orElse(null);
		if(hopper == null)
			return false;
		else {
			if(hoppers.size() == 1) 
				customHoppers.remove(hopperLocation.getChunk().toString());
			else 
				customHoppers.get(hopperLocation.getChunk().toString()).removeIf(x -> x.getLocation().equals(hopperLocation));
			
			config.set("hoppers." + hopper.toString(), null);
			save();
			return true;
		}
	}
}


