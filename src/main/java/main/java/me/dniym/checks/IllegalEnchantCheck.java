package main.java.me.dniym.checks;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.listeners.mcMMOListener;
import main.java.me.dniym.utils.SlimefunCompat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

public class IllegalEnchantCheck {

    public static boolean CheckForIllegalEnchants(ItemStack itemStack, Object obj) {

        if (itemStack != null && (obj instanceof Inventory || obj instanceof Container)) {
            return isIllegallyEnchanted(itemStack, obj);
        }

        return false;
    }

    public static boolean CheckStorageInventory(Inventory inv, Player player) {
        if (!IllegalStack.hasStorage()) {
            return false;
        }

        boolean invalid = false;
        for (ItemStack itemStack : inv.getStorageContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR && (invalid = isIllegallyEnchanted(
                    itemStack,
                    inv,
                    true
            ))) {
                fListener.getLog().append2(Msg.GenericItemRemoval.getValue(
                        itemStack,
                        Protections.FixIllegalEnchantmentLevels,
                        player,
                        "Crafting Inventory"
                ));
            }
        }

        return invalid;
    }

    public static HashMap<Enchantment,String> checkEnchants (ItemStack is,Player p) {
    	
    	HashMap<Enchantment,String> replace = new HashMap<Enchantment,String>();
        if (!mcMMOListener.ismcMMOActive(p)) {
        	
            if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
                if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isn't in a checked world
                    return replace;
            
            if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
                return replace;
            
            if (is != null && !is.getEnchantments().isEmpty()) {
            	
                for (Enchantment en : is.getEnchantments().keySet()) {
                    if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {
                    	
                        if (SlimefunCompat.isValid(is, en)) 
                            continue;
                        
                        if (IllegalStack.isClueScrolls() && en == Enchantment.DURABILITY && is.getType() == Material.PAPER) 
                            continue;
                        
                        if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is
                                .getEnchantmentLevel(en) == 4341)) 
                            continue;
                        
                        if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) 
                            break;
                        
                        if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en))) 
                            continue;

                        replace.put(en,Msg.IllegalEnchantLevel.getValue(p,is,en));
                        
                    } else {
                        if (!en.canEnchantItem(is)) {
                            if (Protections.EnchantedItemWhitelist.isWhitelisted(is)) 
                                continue;
                            
                            if (SlimefunCompat.isValid(is, en)) 
                                continue;
                            
                            replace.put(en,Msg.IllegalEnchantType.getValue(p,is,en));
                            
                        }
                    }
                }
                
            }
        }
        return replace;
    }
    public static boolean isIllegallyEnchanted(ItemStack itemStack, Object obj) {
        return isIllegallyEnchanted(itemStack, obj, false);
    }

    public static boolean isIllegallyEnchanted(ItemStack itemStack, Object obj, boolean silent) {

        if (itemStack == null) {
            return false;
        }

        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !itemStack.getEnchantments().isEmpty()) {
            if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) {//world list isnt empty
                Location loc = null;

                if (obj instanceof Inventory) {
                    loc = ((Inventory) obj).getLocation();
                } else if (obj instanceof Location) {
                    loc = ((Location) obj);
                } else if (obj instanceof Container) {
                    loc = ((Container) obj).getLocation();
                }

                if (loc != null && !Protections.OnlyFunctionInWorlds.getTxtSet().contains(loc
                        .getWorld()
                        .getName())) //isnt in a checked world
                {
                    return false;
                }
            }

            HashSet<Enchantment> replace = new HashSet<>();
            for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                if (itemStack.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel()) {

                    if (SlimefunCompat.isValid(itemStack, enchantment)) {
                        continue;
                    }

                    if (IllegalStack.isEpicRename() && ((enchantment == Enchantment.LURE || enchantment == Enchantment.ARROW_INFINITE) && itemStack
                            .getEnchantmentLevel(
                                    enchantment) == 4341)) {
                        continue;
                    }
                    if (Protections.EnchantedItemWhitelist.isWhitelisted(itemStack)) {
                        break;
                    }

                    if (Protections.CustomEnchantOverride.isAllowedEnchant(
                            enchantment,
                            itemStack.getEnchantmentLevel(enchantment)
                    )) {
                        continue;
                    }

                    if (Protections.DestroyIllegallyEnchantedItemsInstead.isEnabled()) {
                        if (!silent) {
                            fListener.getLog().append2(Msg.DestroyedEnchantedItem.getValue(obj, itemStack, enchantment));
                        }
                        itemStack.setType(Material.AIR);
                        return true;
                    }
                    if (enchantment.canEnchantItem(itemStack)) {
                        if (!silent) {
                            fListener.getLog().append2(Msg.IllegalEnchantLevel.getValue(obj, itemStack, enchantment));
                        }
                    } else {
                        if (!silent) {
                            fListener.getLog().append2(Msg.IllegalEnchantType.getValue(obj, itemStack, enchantment));
                        }
                        replace.add(enchantment);
                    }
                } else {
                    if (!enchantment.canEnchantItem(itemStack)) {
                        if (SlimefunCompat.isValid(itemStack, enchantment)) {
                            continue;
                        }

                        replace.add(enchantment);
                        if (!silent) {
                            fListener.getLog().append2(Msg.IllegalEnchantType.getValue(obj, itemStack, enchantment));
                        }
                    }
                }
            }

            for (Enchantment en : replace) {
                itemStack.removeEnchantment(en);
                if (en.canEnchantItem(itemStack)) {
                	int maxLevel = Protections.CustomEnchantOverride.getMaxAllowedEnchantLevel(en);
                	
                    itemStack.addEnchantment(en, maxLevel);
                }
            }
        }

        return false;
    }

}
