package me.mafkees92.Files;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.HologramParkour.ParkourItem;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;

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
		Messages.actionBarIslandEnter = Utils.colorize(config.getString("ActionBar.islandEnter"));
		Messages.actionBarIslandEnterOwnIsland = Utils.colorize(config.getString("ActionBar.islandEnterOwnIsland"));
		Messages.actionBarSpawnEnter = Utils.colorize(config.getString("ActionBar.spawnEnter"));
		Messages.actionBarSkillExpGained = Utils.colorize(config.getString("ActionBar.skillExpGain"));
		
		//flight time left messages
		Messages.flightWontExpire = Utils.colorize(config.getString("RemainingFlightTime.flightWontExpire"));
		Messages.flightExpirationTime = Utils.colorize(config.getString("RemainingFlightTime.flightExpirationTime"));
		Messages.noFlightTimeLeft = Utils.colorize(config.getString("RemainingFlightTime.noFlightTimeLeft"));
		
		//custom voucher
		Messages.invalidVoucherArguments = Utils.colorize(config.getString("CustomVoucher.invalidVoucherArguments"));
		Messages.invalidVoucherType = Utils.colorize(config.getString("CustomVoucher.invalidVoucherType"));
		Messages.OPAlreadyHasPermissions = Utils.colorize(config.getString("CustomVoucher.OPAlreadyHasPermissions"));
		
    	//custom Hoppers
    	Messages.invalidChunkHopperArguments = Utils.colorize(config.getString("CustomHoppers.invalidArguments"));
    	Messages.youReceivedCustomHopperMessage = Utils.colorize(config.getString("CustomHoppers.youReceivedHopper"));
    	Messages.gaveHopperToPlayerMessage = Utils.colorize(config.getString("CustomHoppers.gaveHopperToPlayer"));
    	
    	Messages.hopperHologramMaterial = Material.valueOf(config.getString("CustomHoppers.hopperHologram.item"));
    	Messages.hopperHologramText = config.getStringList("CustomHoppers.hopperHologram.text");

    	Messages.customHopperDisplayName = Utils.colorize(config.getString("CustomHoppers.hopperItemStack.displayName"));
    	Messages.customHopperLore = Utils.colorize(config.getStringList("CustomHoppers.hopperItemStack.lore"));
	
    	
    	// custom potions
    	Messages.customPotionReceivedInventoryFull = Utils.colorize(config.getString("CustomPotions.potionReceivedInventoryFull"));
    	Messages.customPotionSendTargetInventoryFull = Utils.colorize(config.getString("CustomPotions.potionSendTargetInventoryFull"));
    	Messages.invalidPotionType = Utils.colorize(config.getString("CustomPotions.invalidPotionType"));
    	Messages.invalidPotionArguments = Utils.colorize(config.getString("CustomPotions.invalidPotionArguments"));

    	Messages.freezePotionDisplayName = Utils.colorize(config.getString("CustomPotions.freezePotion.displayName"));
    	Messages.freezePotionLore = Utils.colorize(config.getStringList("CustomPotions.freezePotion.lore"));
    	
    	
    	//Game Master
    	Messages.gameMasterInventoryTitle = Utils.colorize(config.getString("GameMaster.inventoryTitle"));
    	Messages.gameMasterRollTheDiceMaterial = Material.valueOf(config.getString("GameMaster.rollTheDice.material"));
    	Messages.gameMasterRollTheDiceSlot = config.getInt("GameMaster.rollTheDice.inventorySlot");
    	Messages.gameMasterRollTheDiceDisplayName = Utils.colorize(config.getString("GameMaster.rollTheDice.displayName"));
    	Messages.gameMasterRollTheDiceLore = Utils.colorize(config.getStringList("GameMaster.rollTheDice.lore"));
    	Messages.gameMasterInsufficientBalance = Utils.colorize(config.getString("GameMaster.insufficientBalance"));
    	
    	//roll the dice
    	Messages.rollTheDiceWinningMessage = Utils.colorize(config.getString("RollTheDice.winningMessage"));
    	Messages.rollTheDiceLostMessage = Utils.colorize(config.getString("RollTheDice.lostMessage"));
    	Messages.rollTheDiceBetNotFinishedMessage =  Utils.colorize(config.getString("RollTheDice.previousBetNotFinishedMessage"));
    	Messages.rollTheDiceBetPlacedMessage = Utils.colorize(config.getString("RollTheDice.betPlacedMessage"));
    	Messages.rollTheDiceGameStartedMessage = Utils.colorize(config.getString("RollTheDice.startingGameMessage"));
    	
    	
    	//inventory titles
    	Messages.rollTheDicePickANumberInventoryTitle = Utils.colorize(config.getString("RollTheDice.pickANumberInventoryTitle"));
    	Messages.rollTheDicePickABetInventoryTitle = Utils.colorize(config.getString("RollTheDice.pickABetInventoryTitle"));
    	Messages.rollTheDiceGameInventoryTitle = Utils.colorize(config.getString("RollTheDice.gameInventoryTitle"));
    	
    	//items
    	Messages.wonFillItemDisplayName = Utils.colorize(config.getString("RollTheDice.wonFillItem.displayName"));
    	Messages.wonFillItemLore = Utils.colorize(config.getStringList("RollTheDice.wonFillItem.lore"));
    	Messages.lostFillItemDisplayName = Utils.colorize(config.getString("RollTheDice.lostFillItem.displayName"));
    	Messages.lostFillItemLore = Utils.colorize(config.getStringList("RollTheDice.lostFillItem.lore"));
    	
    	
    	//Custom help
    	Messages.helpMessage = Utils.readConfigTextComponents(config, "HelpMenu");
    	
    	//Custom /rules
    	Messages.rulesMessage = Utils.readConfigTextComponents(config, "RulesMessage");

    	//Custom /discord
    	Messages.discordMessage = Utils.readConfigTextComponents(config, "DiscordMessage");
    	
    	//Custom /apply
    	Messages.applyMessage = Utils.readConfigTextComponents(config, "ApplyMessage");
    	
    	//Parkour messages
    	Messages.alreadyDoingParkour = Utils.colorize(config.getString("Parkour.alreadyDoingParkour"));
    	Messages.existingParkoursMessage = Utils.colorize(config.getString("Parkour.existingParkours"));
    	Messages.startedParkour = Utils.colorize(config.getString("Parkour.startedParkour"));
    	Messages.failedLoadingParkour = Utils.colorize(config.getString("Parkour.failedLoadingParkour"));
    	Messages.parkourInfoHeader = Utils.colorize(config.getString("Parkour.info.Header"));
    	Messages.parkourInfoMessage = Utils.colorize(config.getString("Parkour.info.Message"));
    	Messages.parkourInfoFooter = Utils.colorize(config.getString("Parkour.info.Footer"));
    	Messages.parkourWaypointRewardMessage = Utils.colorize(config.getString("Parkour.rewardPickedUp"));
    	Messages.timeLeftTillWaypoint = Utils.colorize(config.getString("Parkour.timeLeftTillWaypoint"));
    	Messages.pickedUpLastWaypoint = Utils.colorize(config.getString("Parkour.pickedUpLast"));
    	Messages.parkourTimeRanOut = Utils.colorize(config.getString("Parkour.timeRanOut"));
    	Messages.parkourLostMessage = Utils.colorize(config.getString("Parkour.parkourLostMessage"));
    	Messages.parkourWonMessage = Utils.colorize(config.getString("Parkour.parkourWonMessage"));
    	Messages.parkourWonBroadcastMessage = Utils.colorize(config.getString("Parkour.parkourWonBroadcastMessage"));
    	Messages.totalMoneyWonMessage = Utils.colorize(config.getString("Parkour.totalMoneyWon"));
    	Messages.parkourRecentlyWon = Utils.colorize(config.getString("Parkour.parkourRecentlyWon"));
    	
    	for(String key : config.getConfigurationSection("Parkour.items").getKeys(false)) {
		    String path = "Parkour.items." + key;
		    String hologramMessage = Utils.colorize(config.getString(path + ".hologramText"));
		    Material material = Material.valueOf(config.getString(path + ".material"));
		    int timeToPickup = config.getInt(path + ".timeToPickup");
		    double reward = config.getDouble(path + ".pickUpReward");
		    int timesToDisplay = config.getInt(path + ".timesToDisplay");
		    
		    Messages.parkourItems.add(new ParkourItem(hologramMessage, material, timeToPickup, reward, timesToDisplay));
    	}
    	

    	Messages.finishItem = new ParkourItem(
    			Utils.colorize(config.getString("Parkour.finishitem.hologramText")),
    			Material.valueOf(config.getString("Parkour.finishitem.material")),
    			config.getInt("Parkour.finishitem.timeToPickup"),
    			config.getDouble("Parkour.finishitem.pickUpReward"),
    			0
    			);
    	
    	String world = config.getString("SeasonHologram.location.world");
    	double x = config.getDouble("SeasonHologram.location.x");
    	double y = config.getDouble("SeasonHologram.location.y");
    	double z = config.getDouble("SeasonHologram.location.z");
    	Messages.seasonHologramLocation = new Location(Bukkit.getWorld(world), x , y , z);
    	Messages.seasonHologramText = config.getStringList("SeasonHologram.text");
    	int year = config.getInt("SeasonHologram.season-ending.year");
    	int month = config.getInt("SeasonHologram.season-ending.month");
    	int dayofmonth = config.getInt("SeasonHologram.season-ending.dayofmonth");
    	int hour = config.getInt("SeasonHologram.season-ending.hour");
    	int minute = config.getInt("SeasonHologram.season-ending.minute");
    	seasonEnding = LocalDateTime.of(year, month, dayofmonth, hour, minute);
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
	public static String actionBarIslandEnter;
	public static String actionBarIslandEnterOwnIsland;
	public static String actionBarSpawnEnter;
	public static String actionBarSkillExpGained;
	
	//remaining fly time messages 
	public static String flightWontExpire;
	private static String flightExpirationTime;
	public static String noFlightTimeLeft;
	public static String flightExpirationTime(String remainingTime) {
		return Messages.flightExpirationTime.replace("%remainingtime%", remainingTime);	}
	
	//Custom voucher messages
	public static String invalidVoucherArguments;
	public static String invalidVoucherType; 
	public static String OPAlreadyHasPermissions;
	
	
	//custom ChunkHopper messages
	public static String invalidChunkHopperArguments;
	public static String youReceivedCustomHopperMessage;
	private static String gaveHopperToPlayerMessage;
	public static String gaveHopperToPlayerMessage(Player targetPlayer, int amount) {
		return gaveHopperToPlayerMessage.replace("{target_player}", targetPlayer.getName()).replace("{amount}", amount + "");
	}
	
	//chunkhopper hologram
	private static List<String> hopperHologramText;
	public static List<String> hopperHologramText(int grade, OfflinePlayer player){
		List<String> temp = new ArrayList<String>(hopperHologramText);
		ListIterator<String> it = temp.listIterator();
		while(it.hasNext()) {
			String line = it.next().replace("{playername}", player.getName());
			line = line.replace("{hopper_grade}", grade + "");
			it.set(line);
		};
		return temp;
	}
	public static Material hopperHologramMaterial;
	
	//chunkhopper itemstack
	private static String customHopperDisplayName;
	public static String customHopperDisplayName(int grade){
		return customHopperDisplayName.replace("{hopper_grade}", grade + "");
	}
	public static List<String> customHopperLore;
	
	
	
	
	
	
	//custom potions
	public static String invalidPotionArguments;
	public static String invalidPotionType;
	public static String customPotionReceivedInventoryFull;
	public static String customPotionSendTargetInventoryFull;
	public static String freezePotionDisplayName;
	public static List<String> freezePotionLore;
	
	
	
	//Game Master
	public static String gameMasterInventoryTitle;
	public static Material gameMasterRollTheDiceMaterial;
	public static int gameMasterRollTheDiceSlot;
	public static String gameMasterRollTheDiceDisplayName;
	public static List<String> gameMasterRollTheDiceLore;
	public static String gameMasterInsufficientBalance;
	
	//Roll the dice
	//messages
	private static String rollTheDiceWinningMessage;
	public static String rollTheDiceWinningMessage(String moneyWon) {
		return rollTheDiceWinningMessage.replace("{money_won}", moneyWon.replace("$", ""));
	}
	private static String rollTheDiceLostMessage;
	public static String rollTheDiceLostMessage(int winningNumber) {
		return rollTheDiceLostMessage.replace("{winning_number}", winningNumber + "");
	}
	
	public static String rollTheDiceBetNotFinishedMessage;
	private static String rollTheDiceBetPlacedMessage;
	public static String rollTheDiceBetPlacedMessage(int numberPicked) {
		return rollTheDiceBetPlacedMessage.replace("{picked_number}", numberPicked + "");
	}
	private static String rollTheDiceGameStartedMessage;
	public static String rollTheDiceGameStartedMessage(String betValue) {
		return rollTheDiceGameStartedMessage.replace("{betvalue}", betValue.replace("$", ""));
	}
	
	public static String rollTheDicePickANumberInventoryTitle;
	public static String rollTheDicePickABetInventoryTitle;
	public static String rollTheDiceGameInventoryTitle;
	
	//items
	public static String wonFillItemDisplayName;
	public static List<String> wonFillItemLore;
	public static String lostFillItemDisplayName;
	public static List<String> lostFillItemLore;
	
	//custom /help
	public static BaseComponent[] helpMessage;
	
	//custom /rules
	public static BaseComponent[] rulesMessage;
	
	//discord command
	public static BaseComponent[] discordMessage;

	//apply command
	public static BaseComponent[] applyMessage;
	
	
	//Parkour messages
	public static String alreadyDoingParkour;
	public static String existingParkoursMessage;
	public static String startedParkour;
	public static String failedLoadingParkour;
	public static String parkourInfoHeader;
	public static String parkourInfoMessage;
	public static String parkourInfoFooter;
	public static String parkourWaypointRewardMessage;
	public static String timeLeftTillWaypoint;
	public static String pickedUpLastWaypoint;
	public static String parkourTimeRanOut;
	public static String parkourLostMessage;
	public static String parkourWonMessage;
	public static String parkourWonBroadcastMessage;
	public static String totalMoneyWonMessage;
	public static String parkourRecentlyWon;
	
	public static List<ParkourItem> parkourItems = new ArrayList<ParkourItem>();
	public static ParkourItem finishItem;
	
	
	//Season Hologram
	public static LocalDateTime seasonEnding;
	public static Location seasonHologramLocation;
	public static List<String> seasonHologramText;
	
	
	
}
