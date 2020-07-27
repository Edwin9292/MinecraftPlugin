package me.mafkees92;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.mafkees92.Commands.GetRemainingFlyTime;
import me.mafkees92.Commands.GiveCustomPotion;
import me.mafkees92.Commands.GiveVoucher;
import me.mafkees92.Commands.RenameItem;
import me.mafkees92.Events.ActionBar;
import me.mafkees92.Events.CustomSplashPotions;
import me.mafkees92.Events.LuckPermsListener;
import me.mafkees92.Events.VoidDamage;
import me.mafkees92.Events.VoucherUsageEvent;
import me.mafkees92.Files.HopperData;
import me.mafkees92.MVdWPlaceholders.mvdwPlaceholders;
import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin {
	
	private HopperData hopperData;
	private LuckPerms luckperms;

	public void onEnable() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		this.hopperData = new HopperData(this);
		
		getServer().getPluginManager().registerEvents(new VoidDamage(this), this);
		getServer().getPluginManager().registerEvents(new CustomSplashPotions(this), this);
		getServer().getPluginManager().registerEvents(new ActionBar(this), this);
		getServer().getPluginManager().registerEvents(new VoucherUsageEvent(), this);
		getCommand("rename").setExecutor(new RenameItem());
		getCommand("givecustompotion").setExecutor(new GiveCustomPotion());
		getCommand("givevoucher").setExecutor(new GiveVoucher());
		getCommand("flytime").setExecutor(new GetRemainingFlyTime(this));

		if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			new mvdwPlaceholders(this);
		}
		
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
		    luckperms = provider.getProvider();
		    new LuckPermsListener(this, luckperms);
		}
		
	}

	public HopperData getHopperData() {
		return hopperData;
	}

	public LuckPerms getLuckperms() {
		return luckperms;
	}
}
