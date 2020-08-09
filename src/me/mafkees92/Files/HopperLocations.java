package me.mafkees92.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import me.mafkees92.Main;
import me.mafkees92.CustomHoppers.ChunkHopper;
import me.mafkees92.Utils.Utils;

public class HopperLocations extends BaseFile{
	

	public HashMap<String, List<ChunkHopper>> customHopperLocations;

	
	public HopperLocations(Main plugin, String fileName) {
		super(plugin, fileName);
		
		loadHopperLocations();
	}
	
	public void loadHopperLocations() {
        if (config.getConfigurationSection("hoppers") == null) {
            config.createSection("hoppers"); 
        }
        
        if(customHopperLocations == null) 
        	customHopperLocations = new HashMap<String, List<ChunkHopper>>();
        
    	HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("hoppers").getValues(false);
    	try {
			Bukkit.getLogger().warning("size of map: " + map.size());
    		for (String line : map.keySet()) {
				Location loc = Utils.StringToLocation(line);
				if(loc != null) {
					ChunkHopper hopper = new ChunkHopper(loc);
					addHopper(hopper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addHopper(ChunkHopper hopper) {
		if(customHopperLocations != null) {
			if(customHopperLocations.containsKey(hopper.getChunkLocationString())) {
				customHopperLocations.get(hopper.getChunkLocationString()).add(hopper);
			}
			else{
				List<ChunkHopper> hopperList = new ArrayList<ChunkHopper>();
				hopperList.add(hopper);
				customHopperLocations.put(hopper.getChunkLocationString(), hopperList);
			}
		}
		config.set("hoppers."+ hopper.toString(), "");
		save();
	}
	
	public boolean removeHopper(ChunkHopper hopper) {
		if(customHopperLocations == null)
			return false;
		
		List<ChunkHopper> hoppers = customHopperLocations.get(hopper.getChunkLocationString());
		ChunkHopper hopperToRemove = hoppers.stream().filter(x -> x.getLocation().equals(hopper.getLocation())).findFirst().orElse(null);
		if(hopperToRemove == null)
			return false;
		else {
			if(hoppers.size() == 1) 
				customHopperLocations.remove(hopperToRemove.getChunkLocationString());
			else 
				customHopperLocations.get(hopperToRemove.getChunkLocationString()).removeIf(x -> x.getLocation().equals(hopper.getLocation()));
			
			config.set("hoppers." + hopperToRemove.toString(), null);
			//RemoveHopperHologram(hopperToRemove);
			save();
			return true;
		}
	}
	
	public boolean containsCustomHopper(Chunk chunk) {
		if(customHopperLocations == null) return false;
		if(customHopperLocations.containsKey(Utils.ChunkToString(chunk)))
			return true;
		return false;			
	}
	
	public boolean containsCustomHopper(Location location) {
		if(customHopperLocations == null) return false;
		if(customHopperLocations.containsKey(Utils.LocationToChunkString(location)))
			return true;
		return false;
	}
	
	public boolean isCustomHopper(Location location) {
		if(customHopperLocations == null) return false;
		
		if(customHopperLocations.containsKey(Utils.LocationToChunkString(location) )) {
			List<ChunkHopper> hoppers = customHopperLocations.get(Utils.LocationToChunkString(location) );
			ChunkHopper hopper = hoppers.stream().filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
			if(hopper == null) 
				return false;
			else 
				return true;
		}
		return false;
	}
	
	public List<ChunkHopper> getHoppersInChunk(Chunk chunk){
		if(customHopperLocations == null) return null;
		if(!customHopperLocations.containsKey(Utils.ChunkToString(chunk))) return null;
		return customHopperLocations.get(Utils.ChunkToString(chunk));
	}
	
	public List<ChunkHopper> getHoppersInChunk(Location location) {
		if(customHopperLocations == null) return null;
		if(!customHopperLocations.containsKey(Utils.LocationToChunkString(location))) return null;
		return customHopperLocations.get(Utils.LocationToChunkString(location));
	}
	
	public List<ChunkHopper> getAllChunkHoppers(){
		if(customHopperLocations == null) return null;
		List<ChunkHopper> hoppers = new ArrayList<ChunkHopper>();
		customHopperLocations.values().forEach(x -> x.forEach(y -> hoppers.add(y)));
		return hoppers;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
