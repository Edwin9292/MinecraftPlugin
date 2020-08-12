package me.mafkees92.ChunkLoaders;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class ChunkLoaderHolograms {
	
	Main plugin;
	
	public ChunkLoaderHolograms(Main plugin) {
		this.plugin = plugin;
	}
	
	public void loadChunkLoaderHologram(ChunkLoader loader) {
		Hologram loaderHolo = HologramsAPI.createHologram(plugin, loader.getHologramLocation());
		loaderHolo.appendTextLine(Utils.colorize("&3&lChunk Loader"));
		loaderHolo.appendItemLine(new HeadDatabaseAPI().getItemHead("25842"));
	}
	
	public boolean removeChunkLoaderHologram(ChunkLoader loader) {
		Hologram loaderHolo = HologramsAPI.getHolograms(plugin).stream()
				.filter(x ->x.getLocation().equals(loader.getHologramLocation())).findFirst().orElse(null);
		if(loaderHolo != null) {
			loaderHolo.delete();
			return true;
		}
		return false;
	}
	
}
