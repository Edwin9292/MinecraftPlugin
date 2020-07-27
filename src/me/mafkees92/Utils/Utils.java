package me.mafkees92.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class Utils {
	
	
    public static String colorize(String message){  
    	return message =  ChatColor.translateAlternateColorCodes('&', message);
   }
    
    public static ItemStack setNBTTag(ItemStack item, String tagName, String tagValue) {
    	net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
    	NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
    	tag.set(tagName, new NBTTagString(tagValue));
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
    
    public static String LocationToString(Location loc) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("X: ");
    	sb.append((int)loc.getX());
    	sb.append(" Y: ");
    	sb.append((int)loc.getY());
    	sb.append(" Z: ");
    	sb.append((int)loc.getZ());
    	return sb.toString();
    }
}
