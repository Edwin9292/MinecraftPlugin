package me.mafkees92;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.wasteofplastic.askyblock.ASkyBlock;

import me.mafkees92.ActionBar.ActionBar;
import me.mafkees92.Commands.Apply;
import me.mafkees92.Commands.Discord;
import me.mafkees92.Commands.Help;
import me.mafkees92.Commands.RepairHand;
import me.mafkees92.Commands.Rules;
import me.mafkees92.Commands.hub;
import me.mafkees92.CustomHoppers.ChunkHoppers;
import me.mafkees92.CustomPotions.CustomSplashPotions;
import me.mafkees92.CustomPotions.GiveCustomPotion;
import me.mafkees92.CustomVouchers.CustomVouchers;
import me.mafkees92.CustomVouchers.FlyExpirationListener;
import me.mafkees92.CustomVouchers.GetRemainingFlyTime;
import me.mafkees92.CustomVouchers.GiveVoucher;
import me.mafkees92.CustomVouchers.VoucherUsageEvent;
import me.mafkees92.Files.Messages;
import me.mafkees92.Gambling.GameMasterHandler;
import me.mafkees92.HologramParkour.Parkour;
import me.mafkees92.IslandChests.IslandChests;
import me.mafkees92.IslandChests.IslandInvSee;
import me.mafkees92.IslandChests.OpenIslandChest;
import me.mafkees92.IslandChests.UpgradeIslandChestSize;
import me.mafkees92.Listeners.BlockBreakListeners;
import me.mafkees92.Listeners.BlockDispenseListener;
import me.mafkees92.Listeners.BlockPlaceListener;
import me.mafkees92.Listeners.PlayerLoginListener;
import me.mafkees92.Listeners.PlayerPortalEventListener;
import me.mafkees92.Listeners.PlayerTeleportListener;
import me.mafkees92.MVdWPlaceholders.mvdwPlaceholders;
import me.mafkees92.RenameItems.RenameItem;
import me.mafkees92.RenameItems.SetLore;
import me.mafkees92.SeasonHologram.SeasonHologram;
import me.mafkees92.VoidChests.VoidChests;
import me.mafkees92.VoidTeleportation.VoidDamage;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Main instance;
	private LuckPerms luckperms;
	public static Economy econ = null;
	private ActionBar actionBar;
	private IslandChests isChests;
	private VoidChests voidChestsInstance;
	private ChunkHoppers chunkHoppersInstance;
	private GameMasterHandler gamblerHandlerInstance;

	public void onEnable() {
		
		if(Main.instance == null) {
			Main.instance = this;
		}

		// saveDefaultConfig();
		new Messages(this, "Messages.yml");
		//chunkLoadersInstance = new ChunkLoaders(this);
		this.voidChestsInstance = new VoidChests(this, "VoidChests.yml");
		this.chunkHoppersInstance = new ChunkHoppers(this, "HopperData.yml");
		new CustomVouchers(this, "CustomVouchers.yml");
		this.gamblerHandlerInstance = new GameMasterHandler(this);

		getServer().getPluginManager().registerEvents(new VoidDamage(this), this);
		getServer().getPluginManager().registerEvents(new CustomSplashPotions(this), this);
		getServer().getPluginManager().registerEvents(actionBar = new ActionBar(this), this);
		getServer().getPluginManager().registerEvents(new VoucherUsageEvent(this), this);
		getServer().getPluginManager().registerEvents(new DisableCraftingValueBlocks(this), this);
		getServer().getPluginManager().registerEvents(isChests = new IslandChests(this), this);
		getServer().getPluginManager().registerEvents(this.voidChestsInstance, this);
		getServer().getPluginManager().registerEvents(this.chunkHoppersInstance, this);
		getServer().getPluginManager().registerEvents(this.gamblerHandlerInstance, this);
		getServer().getPluginManager().registerEvents(new PlayerPortalEventListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
		//getServer().getPluginManager().registerEvents(new ChunkLoaderEvents(this), this);
		getServer().getPluginManager().registerEvents(new BlockBreakListeners(), this );
		getServer().getPluginManager().registerEvents(new BlockDispenseListener(), this );
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this );
		getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this );
		
		getCommand("rename").setExecutor(new RenameItem());
		getCommand("setlore").setExecutor(new SetLore());
		getCommand("givecustompotion").setExecutor(new GiveCustomPotion());
		getCommand("givevoucher").setExecutor(new GiveVoucher());
		getCommand("flytime").setExecutor(new GetRemainingFlyTime(this));
		getCommand("islandchest").setExecutor(new OpenIslandChest(this));
		getCommand("islandinvsee").setExecutor(new IslandInvSee(this));
		getCommand("upgradeislandchestsize").setExecutor(new UpgradeIslandChestSize(this));
		getCommand("givevoidchest").setExecutor(this.voidChestsInstance);
		getCommand("givecustomhopper").setExecutor(this.chunkHoppersInstance);
		getCommand("help").setExecutor(new Help());
		getCommand("gamble").setExecutor(this.gamblerHandlerInstance);
		getCommand("parkour").setExecutor(new Parkour(this, "Parkour/ParkourData.yml"));
		getCommand("hub").setExecutor(new hub());
		getCommand("rules").setExecutor(new Rules());
		getCommand("discord").setExecutor(new Discord());
		getCommand("repairhand").setExecutor(new RepairHand());
		getCommand("Apply").setExecutor(new Apply());

		new SeasonHologram(this);

		if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			new mvdwPlaceholders(this);
		}

		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			Bukkit.getLogger().info("Hooked into LuckPerms");
			luckperms = provider.getProvider();
			new FlyExpirationListener(this, luckperms);
		}

		if (Bukkit.getPluginManager().getPlugin("ShopGUIPlus") != null) {
			Bukkit.getLogger().info("Hooked into ShopGUI+");
		}

		if (!setupEconomy()) {
			Bukkit.getLogger().warning(ChatColor.DARK_RED + "ERROR: could not load vault plugin");
		}
		if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
			System.out.println("Timeout > You need ProtocolLib in order to use this plugin");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if (Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) {
			System.out.println("Timeout > You need HeadDatabase in order to use this plugin");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		
	}

	public void onDisable() {
		Holograms.RemoveAllHolograms();
		isChests.saveChests();
		voidChestsInstance.onDisable();
		chunkHoppersInstance.onDisable();
	}
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider =  ASkyBlock.getPlugin().getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}
		return econ != null;
	}

	public static Main getInstance() {
		return Main.instance;
	}
	
	public LuckPerms getLuckperms() {
		return luckperms;
	}

	public ActionBar getActionBar() {
		return actionBar;
	}

	public IslandChests getIslandChests() {
		return this.isChests;
	}

}
