package me.mafkees92.CustomHoppers;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import me.mafkees92.Utils.Utils;

public class ChunkHopper {

	private final Vector hologramOffset = new Vector(0.5D, 1.99D, 0.5D);
	
	private final Location location;
	private final Chunk chunk;
	private final Location hologramLocation;
	
	//for now data is empty, may be added in the future;
	public ChunkHopper(String location, String data) {
		this.location = Utils.StringToLocation(location);
		this.chunk = this.location.getChunk();
		this.hologramLocation = this.location.clone().add(hologramOffset);
	}
	
	
	public ChunkHopper(Location location) {
		this.location = location;
		this.chunk = location.getChunk();
		this.hologramLocation = location.clone().add(hologramOffset);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public Chunk getChunk() {
		return this.chunk;
	}
	
	public Hopper getHopper() {
		return (Hopper) location.getBlock().getState();
	}
	
	public Inventory getInventory() {
		return this.getHopper().getInventory();
	}
	
	public String getLocationString() {
		return Utils.LocationToString(this.location);
	}
	
	public String getChunkLocationString() {
		return Utils.LocationToChunkString(this.location);
	}
	
	public String getDataString() {
		return "";
	}
	
	public void updateInventoryViewers() {
		for (HumanEntity entity : getInventory().getViewers()) {
			Player p = (Player)entity;
			p.updateInventory();
		}
	}
	
	public Location getHologramLocation() {
		return this.hologramLocation;
	}
	
	
	
}
