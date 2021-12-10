package main.java.me.dniym.actions;

import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.events.IllegalStackActionEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IllegalStackAction {

    public static boolean isCompleted(
            Protections protection,
            Entity cause,
            Block block,
            ItemStack itemInHand,
            Entity mount,
            Inventory inventory
    ) {
        IllegalStackActionEvent event = new IllegalStackActionEvent(protection, cause, block, itemInHand, mount, inventory);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    public static boolean isCompleted(Protections protection, Entity entity, Player player) {
        return isCompleted(protection, player, null, null, entity, null);
    }

    public static boolean isCompleted(Protections protection, Player player, Block block) {
        return isCompleted(protection, player, block, null, null, null);
    }

    public static boolean isCompleted(Protections protection, Player player, Block block, ItemStack itemInHand) {
        return isCompleted(protection, player, block, itemInHand, null, null);
    }

    public static boolean isCompleted(Protections protection, Player player) {

        return isCompleted(protection, player, null, null, null, null);
    }

    public static boolean isCompleted(Protections protection, Entity entity, Player player, Block block) {
        return isCompleted(protection, player, block, null, entity, null);
    }

    public static boolean isCompleted(Protections protection, Entity entity, Entity mount) {
        return isCompleted(protection, entity, null, null, mount, null);
    }

    public static boolean isCompleted(Protections protection, ItemStack item, Block block) {
        return isCompleted(protection, null, block, item, null, null);
    }

    public static boolean isCompleted(Protections protection, ItemStack item, Inventory inventory) {
        return isCompleted(protection, null, null, item, null, inventory);
    }


}
