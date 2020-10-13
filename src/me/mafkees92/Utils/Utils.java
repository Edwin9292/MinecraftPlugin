package me.mafkees92.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class Utils {
	
	
	 public static String colorize(String message){  
    	return ChatColor.translateAlternateColorCodes('&', message);
	 }
	 public static List<String> colorize(List<String> message){
		List<String> temp = new ArrayList<String>(message);
		ListIterator<String> it = temp.listIterator();
		while(it.hasNext()) {
			it.set(Utils.colorize(it.next()));
		};
		return temp;
	}
    
    public static ItemStack setNBTTag(ItemStack item, String tagName, String tagValue) {
    	net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
    	NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		if (tag != null) {
			tag.set(tagName, new NBTTagString(tagValue));
		}
		nmsItem.setTag(tag);
    	item = CraftItemStack.asBukkitCopy(nmsItem);
    	
    	return item;
    }
    
    public static String getNBTTag(ItemStack item, String tagKey) {
    	
    	net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
    	NBTTagCompound tagCompound = nmsItem.getTag();
    	if(tagCompound == null) return null;
    	
    	return tagCompound.getString(tagKey);
    }
    
    public static String LocationToString(Location location) {

		return location.getWorld().getName() +
				"|" +
				location.getBlockX() +
				"|" +
				location.getBlockY() +
				"|" +
				location.getBlockZ();
    }
    
	public static String luckPermDurationToFullDuration(String duration) {
		String tempString = duration;
		if(duration.contains("s")) {
			if(Integer.parseInt(tempString.replace("s", "")) == 1) {
				tempString = tempString.replace("s", " Second");
			}
			else {
				tempString = tempString.replace("s", " Seconds");
			}
		}
		if(duration.contains("m")) {
			if(Integer.parseInt(tempString.replace("m", "")) == 1) {
				tempString = tempString.replace("m", " Minute");
			}
			else {
				tempString = tempString.replace("m", " Minutes");
			}
		}
		if(duration.contains("h")) {
			if(Integer.parseInt(tempString.replace("h", "")) == 1) {
				tempString = tempString.replace("h", " Hour");
			}
			else {
			tempString = tempString.replace("h", " Hours");
			}
		}
		if(duration.contains("d")) {
			if(Integer.parseInt(tempString.replace("d", "")) == 1) {
				tempString = tempString.replace("d", " Day");
			}
			else {
			tempString = tempString.replace("d", " Days");
			}
		}
		return tempString;
	}
	
	public static String luckPermsDurationToRarityString(String duration) {

		String rarity;
		long seconds = 0;
		
		if(duration.contains("s")) 
			seconds = Integer.parseInt(duration.replace("s", ""));
		if(duration.contains("m")) 
			seconds = Integer.parseInt(duration.replace("m", ""))*60;
		if(duration.contains("h")) 
			seconds = Integer.parseInt(duration.replace("h", ""))*3600;
		if(duration.contains("d")) 
			seconds = Integer.parseInt(duration.replace("d", ""))*86400;
		
		if(seconds < 3600)
			rarity = "&a&lCOMMON";
		else if(seconds < 21600)
			rarity = "&b&lUNCOMMON";
		else if(seconds < 86400)
			rarity = "&9&lRARE";
		else if(seconds < 604800)
			rarity = "&d&lEPIC";
		else
			rarity = "&6&lLEGENDARY";
		
		return Utils.colorize(rarity);
		
	}
	
	public static Location StringToLocation(String loc) {
		
		//format = world|1123|30|4045  world|x|y|z
		String[] coords = loc.split("[|]");
		if(coords.length != 4)
			return null;

		
		try {
			World world = Bukkit.getWorld(coords[0]);
			int x = Integer.parseInt(coords[1]);
			int y = Integer.parseInt(coords[2]);
			int z = Integer.parseInt(coords[3]);

			return new Location(world, x, y, z);
					
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}

	public static String LocationToChunkString(Location location) {
		return location.getWorld().getName() + ":" + location.getBlockX()/16 + ":" + location.getBlockZ()/16;
	}
	
	public static String ChunkToString(Chunk chunk) {
		return chunk.getWorld().getName() + ":" + chunk.getX() + ":" + chunk.getZ();
	}
	
	/**
	* Parse a string to an integer.
	* If the string is not an integer, it returns -1.
	*/
	public static int tryParseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	* Parse a string to a double.
	* If the string is not a double, it returns -1.
	*/
	public static double tryParseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static UUID getTeamOrIslandOwner(UUID player) {
		if(ASkyBlockAPI.getInstance().inTeam(player)){
			return ASkyBlockAPI.getInstance().getTeamLeader(player);
		}
		else if(ASkyBlockAPI.getInstance().hasIsland(player)) {
			return player;
		}
		else return null;
	}
	
	
	public static ItemStack createCustomItem(Material material, String displayName, String... loreLines ) {
		
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		List<String> lore = new ArrayList<>();
		
		for(String loreLine : loreLines) {
			lore.add(Utils.colorize(loreLine));
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createCustomItem(Material material, String displayName, List<String> lore ) {
		
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createCustomItem(Material material, int data, String displayName, String... loreLines ) {
		
		ItemStack item = new ItemStack(material, 1 , (byte) data);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		List<String> lore = new ArrayList<>();
		
		for(String loreLine : loreLines) {
			lore.add(Utils.colorize(loreLine));
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createCustomItem(Material material, int data, String displayName, List<String> lore ) {
		
		ItemStack item = new ItemStack(material, 1 , (byte) data);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createCustomHeadItem(String headID, String displayName, String... loreLines ) {
		
		ItemStack item = new HeadDatabaseAPI().getItemHead(headID);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		List<String> lore = new ArrayList<>();
		
		for(String loreLine : loreLines) {
			lore.add(Utils.colorize(loreLine));
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack createCustomHeadItem(String headID, String displayName, List<String> lore ) {
		
		ItemStack item = new HeadDatabaseAPI().getItemHead(headID);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(Utils.colorize(displayName));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	
	public static BaseComponent[] createTextComponentCommand(String text, String hoverText, String clickCommand) {
		ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand);
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create());
		return new ComponentBuilder(text).event(clickEvent).event(hoverEvent).create();
	}
	
	public static BaseComponent[] createTextComponentLink(String text, String hoverText, String link) {
		ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, link);
		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create());
		return new ComponentBuilder(text).event(clickEvent).event(hoverEvent).create();
	}
	
	
	
	
	
	
	
	
	
}
