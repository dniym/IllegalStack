package me.dniym.checks;

import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

    public static void CheckContainer(ItemStack is, Container c) {
        if (is == null)
            return;
        if (is.getAmount() > is.getMaxStackSize()) {

            if (!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
            {
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                    is.setAmount(is.getType().getMaxStackSize());
                    fListener.getLog().append(Msg.IllegalStackShorten.getValue(c, is));
                    return;
                } else {
                    c.getInventory().remove(is);
                    fListener.getLog().append(Msg.IllegalStackItemScan.getValue(c, is));
                    return;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
                return;

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                is.setAmount(is.getType().getMaxStackSize());
                fListener.getLog().append(Msg.IllegalStackShorten.getValue(c, is));
            } else {
                c.getInventory().remove(is);
                fListener.getLog().append(Msg.IllegalStackItemScan.getValue(c, is));
            }
        }
    }
}
