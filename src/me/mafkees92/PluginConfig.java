package me.mafkees92;

public class PluginConfig {

	
	public static void LoadConfig(Main plugin) {
		Settings.testLine = plugin.getConfig().getString("TestLine");
	}
}
