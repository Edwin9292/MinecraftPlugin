package me.mafkees92.Files;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.mafkees92.Main;
import me.mafkees92.Utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

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
    	Set<String> keys = config.getConfigurationSection("HelpMenu").getKeys(false);
    	ComponentBuilder builder = new ComponentBuilder("");
    	
    	for(String key : keys) {
    	    ConfigurationSection line = config.getConfigurationSection("HelpMenu." + key);
    	        String type = line.getString("type");
    	        switch(type) {

    	        case "empty":
    	        	builder.append("\n");
    				builder.event((HoverEvent)null).event((ClickEvent) null);
    	        	break;
    	        case "text":
    	        	builder.append(Utils.colorize(line.getString("text")));
    				builder.event((HoverEvent)null).event((ClickEvent) null);
    	        	break;
    	        case "hovertext":
    	        	builder.append(Utils.colorize(line.getString("text")));
    	        	builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.colorize(line.getString("hover"))).create()));
    	        	break;
    	        case "link":
    	        	builder.append(Utils.createTextComponentLink(
    	        			Utils.colorize(line.getString("text")), Utils.colorize(line.getString("hover")), line.getString("link")));
    	        	break;
    	        case "command":
    	        	builder.append(Utils.createTextComponentCommand(
    	        			Utils.colorize(line.getString("text")), Utils.colorize(line.getString("hover")), Utils.colorize(line.getString("command"))));
    	        	break;
    	        }
    	        //add an enter at the end of every line
    	        builder.append("\n");
    	}
    	Messages.helpMessage = builder.create();
    	
    	Messages.rulesMessage = Utils.colorize(config.getStringList("RulesMessage"));

    	Messages.discordLinkText = Utils.colorize(config.getString("Discord.text"));
    	Messages.discordLinkHoverText = Utils.colorize(config.getString("Discord.hovertext"));
    	Messages.discordLink = Utils.colorize(config.getString("Discord.link"));
    	
    	
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
		return rollTheDiceWinningMessage.replace("{money_won}", moneyWon);
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
		return rollTheDiceGameStartedMessage.replace("{betvalue}", betValue);
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
	public static List<String> rulesMessage;
	
	
	//discord command
	public static String discordLinkText;
	public static String discordLinkHoverText;
	public static String discordLink;
	
	
	
	
	
	
	
	
}
