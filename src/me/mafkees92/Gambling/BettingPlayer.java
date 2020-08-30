package me.mafkees92.Gambling;

import org.bukkit.entity.Player;

public class BettingPlayer {

	
	private Player player;
	private int numberPicked;
	private int betValue;
	private boolean isBetting;
	
	public BettingPlayer(Player player) {
		this.player = player;
	}



	public int getBetValue() {
		return betValue;
	}



	public void setBetValue(int betValue) {
		this.betValue = betValue;
	}



	public int getNumberPicked() {
		return numberPicked;
	}



	public void setNumberPicked(int numberPicked) {
		this.numberPicked = numberPicked;
	}



	public Player getPlayer() {
		return player;
	}



	public boolean isBetting() {
		return isBetting;
	}



	public void setBetting(boolean isBetting) {
		this.isBetting = isBetting;
	}
}
