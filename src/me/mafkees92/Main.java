package me.mafkees92;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.mafkees92.ActionBar.ActionBar;
import me.mafkees92.CustomPotions.CustomSplashPotions;
import me.mafkees92.CustomPotions.GiveCustomPotion;
import me.mafkees92.CustomVouchers.FlyExpirationListener;
import me.mafkees92.CustomVouchers.GetRemainingFlyTime;
import me.mafkees92.CustomVouchers.GiveVoucher;
import me.mafkees92.CustomVouchers.VoucherUsageEvent;
import me.mafkees92.Files.Messages;
import me.mafkees92.MVdWPlaceholders.mvdwPlaceholders;
import me.mafkees92.RenameItems.RenameItem;
import me.mafkees92.VoidTeleportation.VoidDamage;
import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin {
	
	private LuckPerms luckperms;

	public void onEnable() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		saveDefaultConfig();
		new Messages(this, "Messages.yml");
		
		
		//PluginConfig.LoadConfig(this);
		//getLogger().warning(Settings.testLine);
		//getLogger().warning(Settings.voidTestLine);
		
		getServer().getPluginManager().registerEvents(new VoidDamage(this), this);
		getServer().getPluginManager().registerEvents(new CustomSplashPotions(this), this);
		getServer().getPluginManager().registerEvents(new ActionBar(this), this);
		getServer().getPluginManager().registerEvents(new VoucherUsageEvent(this), this);
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
		    new FlyExpirationListener(this, luckperms);
		}
		
	}

	public LuckPerms getLuckperms() {
		return luckperms;
	}
}
