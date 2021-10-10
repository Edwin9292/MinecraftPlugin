package me.mafkees92.CustomVouchers;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mafkees92.Utils.Utils;

public class Voucher {
	public String voucherName;
	public Material material;
	public String displayName;
	public List<String> lore;
	public boolean hasGlow;
	public String permission;
	public String duration;
//	public boolean isStackable;
	public String firstVoucherUsedMessage;
	public String extentionVoucherUsedMessage;
	public String alreadyHasPermanentMessage;
	public String alreadyHasBetterPermissionMessage;
//	public String nonStackableUsedMessage;
	
	public Voucher(String voucherName, Material material, String displayName, List<String> lore, boolean hasGlow, String permission, String duration, boolean isStackable) {
		this.voucherName = voucherName;
		this.material = material;
		this.displayName = displayName;
		this.lore = lore;
		this.hasGlow = hasGlow;
		this.permission = permission;
		this.duration = duration;
//		this.isStackable = isStackable;
	}
	
	
	
	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(this.material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);

        for (ListIterator<String> i = this.lore.listIterator(); i.hasNext(); )   {
        	String loreLine = i.next().replace("%duration%", Utils.luckPermDurationToFullDuration(duration));
        	loreLine = loreLine.replace("%rarity%", Utils.luckPermsDurationToRarityString(duration));
            i.set(Utils.colorize(loreLine));
        }
		meta.setLore(this.lore);
		
		if(hasGlow) {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.FROST_WALKER, 1, false);
		}
		
		item.setItemMeta(meta);
		item = Utils.setNBTTag(item, "customVoucher", permission);
		item = Utils.setNBTTag(item, "duration", duration);
//		item = Utils.setNBTTag(item, "stackable", Boolean.toString(this.isStackable));
		
		return item;
	}
}
