package me.dniym.checks;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
import me.dniym.utils.SlimefunCompat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;


public class IllegalEnchantCheck {
	
	public static boolean CheckForIllegalEnchants (ItemStack is, Object obj) {
		
		if(is != null && (obj instanceof Container || obj instanceof Inventory))
			return isIllegallyEnchanted(is,obj);
		
		return false;
	}

    public static boolean isIllegallyEnchanted(ItemStack is, Object obj) {
    	
        if (is == null)
            return false;

        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !is.getEnchantments().isEmpty()) 
        {
            if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) {//world list isnt empty
            	Location loc = null;	
            	if(obj instanceof Container)
            		loc = ((Container) obj).getLocation();	
            	if(obj instanceof Inventory)
            		loc = ((Inventory)obj).getLocation();
            
                if (loc != null && !Protections.OnlyFunctionInWorlds.getTxtSet().contains(loc.getWorld().getName())) //isnt in a checked world
                    return false;
            }
            
            HashSet<Enchantment> replace = new HashSet<>();
            for (Enchantment en : is.getEnchantments().keySet())
                if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {

                    if (SlimefunCompat.isValid(is, en))
                        continue;

                    if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(en) == 4341))
                        continue;
                    if (Protections.EnchantedItemWhitelist.isWhitelisted(is))
                        break;

                    if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en)))
                        continue;

                    if (Protections.DestroyIllegallyEnchantedItemsInstead.isEnabled()) {
                        fListener.getLog().append(Msg.DestroyedEnchantedItem.getValue(obj, is, en));
                        is.setType(Material.AIR);
                        return true;
                    }
                    if (en.canEnchantItem(is))
                        fListener.getLog().append(Msg.IllegalEnchantLevel.getValue(obj, is, en));
                    else
                        fListener.getLog().append(Msg.IllegalEnchantType.getValue(obj, is, en));
                    replace.add(en);
                } else {
                    if (!en.canEnchantItem(is)) {
                        if (SlimefunCompat.isValid(is, en))
                            continue;

                        replace.add(en);
                        fListener.getLog().append(Msg.IllegalEnchantType.getValue(obj, is, en));
                    }
                }

            for (Enchantment en : replace) {
                is.removeEnchantment(en);
                if (en.canEnchantItem(is))
                    is.addEnchantment(en, en.getMaxLevel());
            }
        }

        return false;
    }


}
