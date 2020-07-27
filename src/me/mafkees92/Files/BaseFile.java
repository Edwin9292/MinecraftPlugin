package me.mafkees92.Files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.mafkees92.Main;

public abstract class BaseFile {

	protected Main plugin;
	private File file;
	protected FileConfiguration config;

	public BaseFile(Main main, String fileName) {

		this.plugin = main;
		this.file = new File(main.getDataFolder(), fileName);
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save() {
		try {
			config.save(file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
