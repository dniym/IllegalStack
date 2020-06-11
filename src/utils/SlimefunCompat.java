package utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import main.IllegalStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class SlimefunCompat {

	public static boolean isValid(ItemStack is, Enchantment en) {
		
	
		if(IllegalStack.isSlimeFun()) {
			SlimefunItem sfi = SlimefunItem.getByItem(is);
			if(sfi != null) {
				ItemStack sfiBase = SlimefunItem.getByID(sfi.getID()).getItem();
				if(!sfiBase.containsEnchantment(en)) //base Slimefun item does not have this enchantment.
					return false;
				//otherwise it is a slimefun item and it does contain the enchantment.
				return true;
				
			}
			
		}
		
		
		
		if(IllegalStack.isClueScrolls() && IllegalStack.isNbtAPI()) {
			if(is.hasItemMeta())
			{
				
				NBTItem nbti = new NBTItem(is);

				for(String key:nbti.getKeys())
					if(key.contains("ClueScrolls"))
						return true;
			}
		}
		
		return false;
	}

	
}
