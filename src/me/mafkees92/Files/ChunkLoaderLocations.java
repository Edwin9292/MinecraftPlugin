package me.mafkees92.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import me.mafkees92.Main;
import me.mafkees92.ChunkLoaders.ChunkLoader;
import me.mafkees92.Utils.Utils;

public class ChunkLoaderLocations extends BaseFile{
	

	public HashMap<String, List<ChunkLoader>> customChunkLoaderLocations;

	
	public ChunkLoaderLocations(Main plugin, String fileName) {
		super(plugin, fileName);
		
		loadChunkLoaderLocations();
	}
	
	//load the locations of chunkloaders from config
	public void loadChunkLoaderLocations() {
        if (config.getConfigurationSection("chunkloaders") == null) {
            config.createSection("chunkloaders"); 
        }
        
        if(customChunkLoaderLocations == null) 
        	customChunkLoaderLocations = new HashMap<>();
        
    	HashMap<String, Object> map = (HashMap<String, Object>) config.getConfigurationSection("chunkloaders").getValues(false);
    	try {
			Bukkit.getLogger().warning("size of map: " + map.size());
    		for (String line : map.keySet()) {
				Location loc = Utils.StringToLocation(line);
				if(loc != null) {
					ChunkLoader loader = new ChunkLoader(loc);
					addChunkLoader(loader);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addChunkLoader(ChunkLoader loader) {
		if(customChunkLoaderLocations != null) {
			if(customChunkLoaderLocations.containsKey(loader.getChunkLocationString())) {
				customChunkLoaderLocations.get(loader.getChunkLocationString()).add(loader);
			}
			else{
				List<ChunkLoader> chunkLoaderList = new ArrayList<>();
				chunkLoaderList.add(loader);
				customChunkLoaderLocations.put(loader.getChunkLocationString(), chunkLoaderList);
			}
		}
		config.set("chunkloaders."+ loader.toString(), "");
		save();
	}
	
	//remove a chunkloader from memory and config
	public boolean removeChunkLoader(ChunkLoader loader) {
		if(customChunkLoaderLocations == null)
			return false;
		
		List<ChunkLoader> loaders = customChunkLoaderLocations.get(loader.getChunkLocationString());
		ChunkLoader loaderToRemove = loaders.stream().filter(x -> x.getLocation().equals(loader.getLocation())).findFirst().orElse(null);
		if(loaderToRemove == null)
			return false;
		else {
			if(loaders.size() == 1) 
				customChunkLoaderLocations.remove(loaderToRemove.getChunkLocationString());
			else 
				customChunkLoaderLocations.get(loaderToRemove.getChunkLocationString()).removeIf(x -> x.getLocation().equals(loader.getLocation()));
			
			config.set("chunkloaders." + loaderToRemove.toString(), null);
			//RemoveHopperHologram(hopperToRemove);
			save();
			return true;
		}
	}
	
	public boolean containsChunkLoader(Chunk chunk) {
		if(customChunkLoaderLocations == null) return false;
		return customChunkLoaderLocations.containsKey(Utils.ChunkToString(chunk));
	}
	
	public boolean isChunkLoader(Location location) {
		if(customChunkLoaderLocations == null) return false;
		
		if(customChunkLoaderLocations.containsKey(Utils.LocationToChunkString(location) )) {
			List<ChunkLoader> loaders = customChunkLoaderLocations.get(Utils.LocationToChunkString(location) );
			ChunkLoader hopper = loaders.stream().filter(x -> x.getLocation().equals(location)).findFirst().orElse(null);
			return hopper != null;
		}
		return false;
	}
	
	public List<ChunkLoader> getAllChunkLoaders(){
		if(customChunkLoaderLocations == null) return null;
		List<ChunkLoader> loaders = new ArrayList<>();
		customChunkLoaderLocations.values().forEach(loaders::addAll);
		return loaders;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
