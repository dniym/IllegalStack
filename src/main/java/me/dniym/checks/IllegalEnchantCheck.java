package me.dniym.checks;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
import me.dniym.utils.SlimefunCompat;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;


public class IllegalEnchantCheck {
    public static void isIllegallyEnchanted(ItemStack is, Container c) {
        if (is == null)
            return;

        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !is.getEnchantments().isEmpty()) {
            if (!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isn't empty
                if (!Protections.OnlyFunctionInWorlds.getTxtSet().contains(c.getWorld().getName())) //isn't in a checked world
                    return;

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
                        fListener.getLog().append(Msg.DestroyedEnchantedItem.getValue(c, is, en));
                        is.setType(Material.AIR);
                        return;
                    }
                    if (en.canEnchantItem(is))
                        fListener.getLog().append(Msg.IllegalEnchantLevel.getValue(c, is, en));
                    else
                        fListener.getLog().append(Msg.IllegalEnchantType.getValue(c, is, en));
                    replace.add(en);
                } else {
                    if (!en.canEnchantItem(is)) {
                        if (SlimefunCompat.isValid(is, en))
                            continue;

                        replace.add(en);
                        fListener.getLog().append(Msg.IllegalEnchantType.getValue(c, is, en));
                    }
                }

            for (Enchantment en : replace) {
                is.removeEnchantment(en);
                if (en.canEnchantItem(is))
                    is.addEnchantment(en, en.getMaxLevel());
            }
        }
    }
}
