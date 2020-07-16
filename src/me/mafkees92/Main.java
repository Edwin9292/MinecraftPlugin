package me.mafkees92;

import org.bukkit.plugin.java.JavaPlugin;

import me.mafkees92.Commands.RenameItem;
import me.mafkees92.Events.ActionBar;
import me.mafkees92.Events.CustomSplashPotions;
import me.mafkees92.Events.VoidDamage;
import me.mafkees92.MVdWPlaceholders.mvdwPlaceholders;

public class Main extends JavaPlugin {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new VoidDamage(this), this);
		getServer().getPluginManager().registerEvents(new CustomSplashPotions(this), this);
		getServer().getPluginManager().registerEvents(new ActionBar(this), this);
		getCommand("rename").setExecutor(new RenameItem());

		if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			new mvdwPlaceholders(this);
		}
	}
}
