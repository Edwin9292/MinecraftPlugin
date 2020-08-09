package me.mafkees92.Files;

import java.util.List;

import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;

public class Messages extends BaseFile {

	public Messages(Main plugin, String fileName) {
		super(plugin, fileName);
		
		//general messages
		Messages.noPermission = Utils.colorize(config.getString("General.noPermission"));
		Messages.invalidTargetPlayer = Utils.colorize(config.getString("General.invalidTargetPlayer"));
		Messages.inventoryFull = Utils.colorize(config.getString("General.inventoryFull"));
		Messages.invalidAmount = Utils.colorize(config.getString("General.invalidAmount"));
		
		//set lore
		Messages.noSetLoreArguments = Utils.colorize(config.getString("SetLore.noArguments"));
		Messages.succeededSettingLore = Utils.colorize(config.getString("SetLore.succes"));
		
		//action bar
		Messages.actionBar = Utils.colorize(config.getString("ActionBar.message"));
		Messages.actionBarNoIsland = Utils.colorize(config.getString("ActionBar.messageNoIsland"));
		
		//custom voucher
		Messages.invalidVoucherArguments = Utils.colorize(config.getString("CustomVoucher.invalidVoucherArguments"));
		Messages.invalidVoucherType = Utils.colorize(config.getString("CustomVoucher.invalidVoucherType"));
		Messages.alreadyHasPermanentFly = Utils.colorize(config.getString("CustomVoucher.alreadyHasPermanentFly"));
		Messages.nonStackableFlightDuration = Utils.colorize(config.getString("CustomVoucher.nonStackableFlightDuration"));
		Messages.firstVoucherUsed = Utils.colorize(config.getString("CustomVoucher.firstVoucherUsed"));
		Messages.extentionVoucherUsed = Utils.colorize(config.getString("CustomVoucher.extentionVoucherUsed"));
		Messages.OPAlreadyHasPermissions = Utils.colorize(config.getString("CustomVoucher.OPAlreadyHasPermissions"));
		Messages.flightWontExpire = Utils.colorize(config.getString("CustomVoucher.flightWontExpire"));
		Messages.flightExpirationTime = Utils.colorize(config.getString("CustomVoucher.flightExpirationTime"));
		Messages.noFlightTimeLeft = Utils.colorize(config.getString("CustomVoucher.noFlightTimeLeft"));
		
		Messages.flyVoucherName = Utils.colorize(config.getString("CustomVoucher.flyVoucherName"));
		Messages.flyVoucherLore = config.getStringList("CustomVoucher.flyVoucherLore");
    	Messages.invalidFlyVoucherArguments = Utils.colorize(config.getString("CustomVoucher.invalidFlyVoucherArguments"));
    	
    	//custom Hoppers
    	Messages.invalidChunkHopperArguments = Utils.colorize(config.getString("CustomHoppers.invalidArguments"));
	
	}
	
	//general messages
	public static String noPermission;
	public static String invalidTargetPlayer;
	private static String inventoryFull;
	public static String inventoryFull(Player player){
		return Messages.inventoryFull.replace("%playername%", player.getName());}
	public static String invalidAmount;

	//Set lore messages
	public static String noSetLoreArguments;
	public static String succeededSettingLore;
	
	//Action bar messages
	public static String actionBar;
	public static String actionBarNoIsland;
	
	//Custom voucher messages
	public static String invalidVoucherArguments;
	public static String invalidVoucherType; 
	public static String alreadyHasPermanentFly; 
	public static String nonStackableFlightDuration;
	private static String firstVoucherUsed;
	private static String extentionVoucherUsed;
	public static String OPAlreadyHasPermissions;
	public static String flightWontExpire;
	private static String flightExpirationTime;
	public static String noFlightTimeLeft;
	public static String flyVoucherName;
	public static List<String> flyVoucherLore;
	public static String invalidFlyVoucherArguments;
	
	public static String firstVoucherUsed(String duration) {
		return Messages.firstVoucherUsed.replace("%duration%", duration); }
	
	public static String extentionVoucherUsed(String duration) {
		return Messages.extentionVoucherUsed.replace("%duration%", duration); }
	
	public static String flightExpirationTime(String remainingTime) {
		return Messages.flightExpirationTime.replace("%remainingtime%", remainingTime);	}
	
	//custom ChunkHopper messages
	public static String invalidChunkHopperArguments;
	
	
}
