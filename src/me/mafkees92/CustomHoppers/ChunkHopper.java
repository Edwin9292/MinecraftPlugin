package me.mafkees92.CustomHoppers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Hopper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.mafkees92.Utils.Utils;

public class ChunkHopper {

	private final Vector hologramOffset = new Vector(0.5D, 1.99D, 0.5D);
	
	private final Location location;
	private final Chunk chunk;
	private final Location hologramLocation;
	private final OfflinePlayer hopperOwner;
	private final int hopperGrade;
	
	//for now data is empty, may be added in the future;
	public ChunkHopper(String location, String data) {
		String[] splitData = data.split("[|]");
		this.hopperOwner = Bukkit.getOfflinePlayer(UUID.fromString(splitData[0]));
		this.location = Utils.StringToLocation(location);
		this.chunk = this.location.getChunk();
		this.hologramLocation = this.location.clone().add(hologramOffset);
		this.hopperGrade = 1;
	}
	
	
	public ChunkHopper(Location location, Player player) {
		this.location = location;
		this.chunk = location.getChunk();
		this.hologramLocation = location.clone().add(hologramOffset);
		this.hopperOwner = player;
		this.hopperGrade = 1;
	}
	
	
	public boolean isChunkHopper() {
		return this.location.getBlock().getState() instanceof Hopper;
	}
	
	public String getDataString() {

		return this.hopperOwner.getUniqueId().toString();
	}
	
	
	public Location getLocation() {
		return this.location;
	}
	
	public Chunk getChunk() {
		return this.chunk;
	}

	public String getLocationString() {
		return Utils.LocationToString(this.location);
	}
	
	public String getChunkLocationString() {
		return Utils.ChunkToString(this.chunk);
	}
	
	public void updateInventoryViewers(Hopper hopper) {
		for (HumanEntity entity : hopper.getInventory().getViewers()) {
			Player p = (Player)entity;
			p.updateInventory();
		}
	}
	
	public Location getHologramLocation() {
		return this.hologramLocation;
	}
	
	public OfflinePlayer getHopperOwner() {
		return this.hopperOwner;
	}


	public int getHopperGrade() {
		return hopperGrade;
	}
	
	
}
