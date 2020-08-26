package me.mafkees92.ChunkLoadersNotUSED;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;

import me.mafkees92.Main;
import me.mafkees92.Files.ChunkLoaderLocations;

public class ChunkLoaders{

	Main plugin;
	ChunkLoaderLocations chunkLoaderLocations;
	ChunkLoaderHolograms chunkLoaderHolograms;
	
	public ChunkLoaders(Main plugin) {
		this.plugin = plugin;
		this.chunkLoaderHolograms = new ChunkLoaderHolograms(plugin);
		
		loadChunkLoaders();
	}
	
	private void loadChunkLoaders() {
		chunkLoaderLocations = new ChunkLoaderLocations(plugin, "chunkloaderlocations.yml");
		List<ChunkLoader> loaders = chunkLoaderLocations.getAllChunkLoaders();
		for(ChunkLoader loader : loaders){
			chunkLoaderHolograms.loadChunkLoaderHologram(loader);
		}
	}
	

	public void addChunkLoader(ChunkLoader loader) {
		chunkLoaderLocations.addChunkLoader(loader);
		chunkLoaderHolograms.loadChunkLoaderHologram(loader);
	}
	
	public boolean removeChunkLoader(ChunkLoader loader) {
		if(chunkLoaderLocations.removeChunkLoader(loader) &&
				chunkLoaderHolograms.removeChunkLoaderHologram(loader)){
			return true;
		}
		return false;
	}
	
	public boolean containsChunkLoader(Chunk chunk) {
		return chunkLoaderLocations.containsChunkLoader(chunk);
	}
	
	public boolean isChunkLoader(Location location) {
		return chunkLoaderLocations.isChunkLoader(location);
	}
	
	
}
