package me.mafkees92.HologramParkour;

import org.bukkit.Material;

public class ParkourItem {
	public String hologramMessage;
	public Material material;
	public int timeToPickup;
	public double pickUpReward = 0;
	public int timesToDisplay;
	
	public ParkourItem(String message, Material material,int timeToPickup, double pickUpReward, int timesToDisplay) {
		this.hologramMessage = message;
		this.material = material;
		this.timeToPickup = timeToPickup;
		this.pickUpReward = pickUpReward;
		this.timesToDisplay = timesToDisplay;
	}
}
