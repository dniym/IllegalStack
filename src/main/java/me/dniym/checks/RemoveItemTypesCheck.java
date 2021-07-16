package main.java.me.dniym.checks;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RemoveItemTypesCheck {

    public static boolean CheckForIllegalTypes(ItemStack itemStack, Object obj) {

        if (itemStack != null && (obj instanceof Inventory || (IllegalStack.hasContainers() && obj instanceof Container))) {
            if (shouldRemove(itemStack, obj)) {
                if (obj instanceof Inventory) {
                    ((Inventory) obj).remove(itemStack);
                } else {
                    ((Container) obj).getInventory().remove(itemStack);
                }
            }
        }

        return false;
    }

    public static boolean shouldRemove(Material type) {
        return Protections.RemoveItemTypes.isWhitelisted(type);
    }

    public static boolean shouldRemove(ItemStack itemStack, Object obj) {


        if (Protections.RemoveItemTypes.isWhitelisted(itemStack)) {

            if (obj != null && Protections.RemoveItemTypes.notifyOnly()) {
                fListener.getLog().notify(
                        Protections.RemoveItemTypes,
                        " Triggered by: a container with item: " + itemStack.getType().name()
                );
                return false;
            } else if (obj != null) {
                fListener.getLog().append2(Msg.ItemTypeRemoved.getValue(obj, itemStack, null));
            }
            return true;

        }
        return false;
    }

}
