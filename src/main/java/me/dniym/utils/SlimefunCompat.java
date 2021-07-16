package main.java.me.dniym.utils;

import de.tr7zw.nbtapi.NBTItem;
import main.java.me.dniym.IllegalStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class SlimefunCompat {

    public static boolean isValid(Inventory inv) {
        return true;
    }

    public static boolean isValid(ItemStack is, Enchantment en) {

        if (is == null) {
            return false;
        }

        if (IllegalStack.isSlimeFun()) {
            SlimefunItem sfi = SlimefunItem.getByItem(is);
            if (sfi != null) {
                ItemStack sfiBase = SlimefunItem.getByID(sfi.getID()).getItem();
                //base Slimefun item does not have this enchantment.
                return sfiBase.containsEnchantment(en);
                //otherwise it is a slimefun item and it does contain the enchantment.
            }
        }

        if (IllegalStack.isClueScrolls() && IllegalStack.isNbtAPI()) {
            if (is.hasItemMeta()) {
                NBTItem nbti = new NBTItem(is);
                for (String key : nbti.getKeys()) {
                    if (key.contains("ClueScrolls")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
