package main.java.me.dniym.events;

import main.java.me.dniym.enums.Protections;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IllegalStackActionEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Protections protection;
    private Entity cause;
    private Block block;
    private ItemStack item;
    private Entity mount;
    private Inventory affectedInventory;

    public IllegalStackActionEvent(
            Protections protections,
            Entity entity,
            Block block,
            ItemStack itemStack,
            Entity entity1,
            Inventory inventory
    ) {
        this.protection = protections;
        this.setCause(entity);
        this.block = block;
        this.item = itemStack;
        this.setMount(entity1);
        this.affectedInventory = inventory;

    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Protections getProtection() {
        return protection;
    }

    public void setProtection(Protections protection) {
        this.protection = protection;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }


    public Entity getMount() {
        return mount;
    }


    public void setMount(Entity mount) {
        this.mount = mount;
    }


    public Entity getCause() {
        return cause;
    }


    public void setCause(Entity cause) {
        this.cause = cause;
    }


    public Inventory getAffectedInventory() {
        return affectedInventory;
    }


    public void setAffectedInventory(Inventory affectedInventory) {
        this.affectedInventory = affectedInventory;
    }

}
