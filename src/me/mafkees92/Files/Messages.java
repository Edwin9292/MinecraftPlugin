package me.mafkees92.Files;

import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class Messages extends BaseFile {

	private final String GENERAL_WARNINGS = "General.warnings.";
	private final String ACTION_BAR_NORMAL = "ActionBar.normal.";
	private final String CUSTOM_VOUCHER_NORMAL = "CustomVoucher.normal.";
	private final String CUSTOM_VOUCHER_WARNINGS = "CustomVoucher.warnings.";
	
	
	
	public Messages(Main plugin, String fileName) {
		super(plugin, fileName);
		
		//general messages
		Messages.GeneralMessages.noPermission = Utils.colorize(GENERAL_WARNINGS + "noPermission");
		Messages.GeneralMessages.invalidTargetPlayer = Utils.colorize(GENERAL_WARNINGS + "invalidTargetPlayer");
		
		
		//action bar
		Messages.ActionBarMessages.message = Utils.colorize(config.getString(ACTION_BAR_NORMAL + "message"));
		Messages.ActionBarMessages.messageNoIsland = Utils.colorize(config.getString(ACTION_BAR_NORMAL + "messageNoIsland"));
		
		//custom voucher
		Messages.CustomVoucherMessages.inventoryFull = Utils.colorize(config.getString(CUSTOM_VOUCHER_WARNINGS + "inventoryFull"));
		Messages.CustomVoucherMessages.invalidArguments = Utils.colorize(config.getString(CUSTOM_VOUCHER_WARNINGS + "invalidVoucherArguments"));
		Messages.CustomVoucherMessages.invalidType = Utils.colorize(config.getString(CUSTOM_VOUCHER_WARNINGS + "invalidVoucherType"));
		Messages.CustomVoucherMessages.alreadyHasPermanentFly = Utils.colorize(config.getString(CUSTOM_VOUCHER_WARNINGS + "alreadyHasPermanentFly"));
		Messages.CustomVoucherMessages.nonStackableFlightDuration = Utils.colorize(config.getString(CUSTOM_VOUCHER_WARNINGS + "nonStackableFlightDuration"));

		Messages.CustomVoucherMessages.firstVoucherUsed = Utils.colorize(config.getString(CUSTOM_VOUCHER_NORMAL + "firstVoucherUsed"));
		Messages.CustomVoucherMessages.extentionVoucherUsed = Utils.colorize(config.getString(CUSTOM_VOUCHER_NORMAL + "extentionVoucherUsed"));
		
		//custom potions
		
		
	}
	
	//general messages
	public static class GeneralMessages{
		public static String noPermission;
		public static String invalidTargetPlayer;
	}

	//Action bar messages
	public static class ActionBarMessages{
		public static String message;
		public static String messageNoIsland;
	}
	
	//Custom voucher messages
	public static class CustomVoucherMessages{
		private static String inventoryFull;
		public static String inventoryFull(Player player){
			return Messages.CustomVoucherMessages.inventoryFull.replace("%playername%", player.getName());}
		
		private static String firstVoucherUsed;
		public static String firstVoucherUsed(int durationInMinutes) {
			return Messages.CustomVoucherMessages.firstVoucherUsed.replace("%durationInMinutes%", Integer.toString(durationInMinutes));
		}
		private static String extentionVoucherUsed;
		public static String extentionVoucherUsed(int durationInMinutes) {
			return Messages.CustomVoucherMessages.extentionVoucherUsed.replace("%durationInMinutes%", Integer.toString(durationInMinutes));}
		
		public static String invalidArguments;
		public static String invalidType; 
		public static String alreadyHasPermanentFly; 
		public static String nonStackableFlightDuration;
	}
	
	public static class CustomPotionMessages{
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
