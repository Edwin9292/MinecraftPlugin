package me.mafkees92.CustomVouchers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;

import me.mafkees92.Main;
import me.mafkees92.Files.BaseFile;
import me.mafkees92.Utils.Utils;

public class CustomVouchers extends BaseFile{

	public static List<Voucher> vouchersList = new ArrayList<Voucher>();
	
	public CustomVouchers(Main plugin, String fileName) {
		super(plugin, fileName);
		
		//read vouchers from config;
		for(String key : config.getConfigurationSection("CustomVoucher.vouchers").getKeys(false)) {
		    String path = "CustomVoucher.vouchers." + key;
		    Material material = Material.valueOf(config.getString(path + ".material"));
		    String displayName = Utils.colorize(config.getString(path + ".displayName"));
		    List<String> lore = Utils.colorize(config.getStringList(path + ".lore"));
		    boolean hasGlow = config.getBoolean(path + ".glow");
		    String permission = config.getString(path + ".permission");
		    String duration = config.getString(path + ".duration");
		    boolean isStackable = config.getBoolean(path + ".stackable");
		    
		    Voucher voucher = new Voucher(key, material, displayName, lore, hasGlow, permission, duration, isStackable);
		    voucher.firstVoucherUsedMessage = Utils.colorize(config.getString(path + ".messages.firstVoucherUsed")).replace("%duration%", Utils.luckPermDurationToFullDuration(duration));
		    voucher.extentionVoucherUsedMessage = Utils.colorize(config.getString(path + ".messages.extentionVoucherUsed")).replace("%duration%", Utils.luckPermDurationToFullDuration(duration));
		    voucher.alreadyHasPermanentMessage = Utils.colorize(config.getString(path + ".messages.alreadyHasPermanent")).replace("%duration%", Utils.luckPermDurationToFullDuration(duration));
		    String[] split = permission.split("[.]");
		    if(StringUtils.isNumeric(split[split.length -1])) {
			    voucher.alreadyHasBetterPermissionMessage = Utils.colorize(config.getString(path + ".messages.alreadyHasBetter"));
		    }
//		    voucher.nonStackableUsedMessage = Utils.colorize(config.getString(path + ".messages.nonStackableUsedMessage"));
		    
		    CustomVouchers.vouchersList.add(voucher);
		    
		    
		}

	}
	
}
