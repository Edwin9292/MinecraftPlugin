package me.mafkees92.CustomHoppers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class ChunkHopperHolograms {
	
	Main plugin;
	
	public ChunkHopperHolograms(Main plugin) {
		this.plugin = plugin;
	}
	
	public void LoadHopperHologram(ChunkHopper hopper) {
		Hologram hopperHolo = HologramsAPI.createHologram(plugin, hopper.getHologramLocation());
		hopperHolo.appendTextLine(Utils.colorize("&3&lChunk Hopper"));
		hopperHolo.appendItemLine(new ItemStack(Material.HOPPER));
	}
	
	public boolean RemoveHopperHologram(ChunkHopper hopper) {
		Hologram hopperHolo = HologramsAPI.getHolograms(plugin).stream()
				.filter(x ->x.getLocation().equals(hopper.getHologramLocation())).findFirst().orElse(null);
		if(hopperHolo != null) {
			hopperHolo.delete();
			return true;
		}
		return false;
	}
	
	public void RemoveAllHopperHolograms() {
		HologramsAPI.getHolograms(plugin).forEach(x -> x.delete());
	}
}
