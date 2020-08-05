package me.mafkees92.CustomHoppers;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class CustomHopper {

	String locationSeparator = "|";
	
	private Location location;
	private Chunk chunk;
	
	
	public CustomHopper(Location location) {
		this.location = location;
		this.chunk = location.getChunk();
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public Chunk getChunk() {
		return this.chunk;
	}
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(location.getWorld().getName());
		sb.append(locationSeparator);
		sb.append(location.getBlockX());
		sb.append(locationSeparator);
		sb.append(location.getBlockY());
		sb.append(locationSeparator);
		sb.append(location.getBlockZ());
		
		return sb.toString();
	}

}
