package main.java.me.dniym.checks;


import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.NBTApiStuff;
import main.java.me.dniym.utils.NBTStuff;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.dniym.IllegalStack;

import java.util.HashSet;

public class BadAttributeCheck {

    public static void CheckStorageInventory(CraftingInventory inventory, Player player) {
        if (Protections.RemoveCustomAttributes.isEnabled() && IllegalStack.hasStorage()) {
            if (Protections.AllowBypass.isEnabled() && player.hasPermission("illegalstack.enchantbypass")) {
                return;
            }
            for (ItemStack itemStack : inventory.getStorageContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR && NBTStuff.hasBadCustomData(itemStack)) {
                    fListener.getLog().append2(Msg.GenericItemRemoval.getValue(
                            itemStack,
                            Protections.RemoveCustomAttributes,
                            player,
                            "Crafting Inventory"
                    ));
                    inventory.removeItem(itemStack);
                }
            }
        }
    }

    public static boolean hasBadAttributes(ItemStack is, Object obj) {
        if (!Protections.RemoveCustomAttributes.isEnabled()) {
            return false;
        }
        return is != null && is.getType() != Material.AIR && checkForBadCustomData(is, obj);
    }

    public static boolean checkForBadCustomData(ItemStack itemStack, Object obj) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (IllegalStack.isHasAttribAPI()) {
            if (itemMeta.hasAttributeModifiers()) {
                StringBuilder attribs = new StringBuilder();
                HashSet<Attribute> toRemove = new HashSet<>();
                for (Attribute a : itemMeta.getAttributeModifiers().keySet()) {

                    for (AttributeModifier st : itemMeta.getAttributeModifiers(a)) {
                        attribs.append(" ").append(st.getName()).append(" value: ").append(st.getAmount());
                    }
                    toRemove.add(a);
                }

                fListener.getLog().append(
                        Msg.CustomAttribsRemoved3.getValue(itemStack, obj, attribs),
                        Protections.RemoveCustomAttributes
                );

                for (Attribute remove : toRemove) {
                    itemMeta.removeAttributeModifier(remove);
                }

                for (ItemFlag iFlag : itemMeta.getItemFlags()) {
                    itemMeta.removeItemFlags(iFlag);
                }

                itemStack.setItemMeta(itemMeta);
                return true;
            }
        } else if (IllegalStack.isNbtAPI()) {
            return NBTApiStuff.checkForBadCustomDataLegacy(itemStack, obj);

        } else {
            fListener.getLog().append(
                    Msg.StaffNoNBTAPI.getValue(Protections.RemoveCustomAttributes.name()),
                    Protections.RemoveCustomAttributes
            );
        }
        return false;
    }

}
