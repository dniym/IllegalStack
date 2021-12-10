package main.java.me.dniym.checks;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

    public static boolean CheckForOverstackedItems(ItemStack itemStack, Object obj) {

        if (itemStack != null && (obj instanceof Inventory || obj instanceof Container)) {
            return CheckContainer(itemStack, obj);
        }

        return false;
    }

    public static boolean CheckStorageInventory(Inventory inv, Player player) {
        if (!IllegalStack.hasStorage()) {
            return false;
        }

        for (ItemStack is : inv.getStorageContents()) {
            if (is != null && is.getType() != Material.AIR && CheckContainer(is, inv, true)) {
                fListener.getLog().append2(Msg.GenericItemRemoval.getValue(
                        is,
                        Protections.RemoveOverstackedItems,
                        player,
                        "Crafting Inventory"
                ));
            }
        }
        return false;
    }

    public static boolean CheckContainer(ItemStack itemStack, Object obj) {
        return CheckContainer(itemStack, obj, false);
    }

    public static boolean CheckContainer(ItemStack is, Object obj, Boolean silent) {
        if (is == null) {
            return false;
        }

        if (is.getAmount() > is.getMaxStackSize()) {

            if (!Protections.IllegalStackMode.isEnabled()) { //in blacklist mode and on the blacklist
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                    if (!silent) {
                        fListener.getLog().append(
                                Msg.IllegalStackShorten.getValue(obj, is),
                                Protections.RemoveOverstackedItems
                        );
                    }
                    is.setAmount(is.getType().getMaxStackSize());
                    return true;
                } else {
                    if (!silent) {
                        fListener.getLog().append(
                                Msg.IllegalStackItemScan.getValue(obj, is),
                                Protections.RemoveOverstackedItems
                        );
                    }
                    if (obj instanceof Inventory) {
                        ((Inventory) obj).remove(is);
                    } else {
                        ((Container) obj).getInventory().remove(is);
                    }
                    return true;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null)) {
                return false;
            }

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                if (!silent) {
                    fListener.getLog().append2(Msg.IllegalStackShorten.getValue(obj, is));
                }
                is.setAmount(is.getType().getMaxStackSize());

                return true;
            } else {
                if (!silent) {
                    fListener.getLog().append2(Msg.IllegalStackItemScan.getValue(obj, is));
                }
                if (obj instanceof Inventory) {
                    ((Inventory) obj).remove(is);
                } else {
                    ((Container) obj).getInventory().remove(is);
                }
                return true;
            }
        }

        return false;
    }

}
