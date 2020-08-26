package me.dniym.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dniym.enums.Protections;

public class IllegalStackActionEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private Protections protection = null;
	private Entity cause;
	private Block block;
	private ItemStack item;
    private Entity mount;
	private Inventory affectedInventory;
	
	public IllegalStackActionEvent(Protections prot, Entity src, Block blk, ItemStack itm, Entity mnt, Inventory inventory) {
		this.protection = prot;
		this.setCause(src);
		this.block = blk;
		this.item = itm;
		this.setMount(mnt);
		this.affectedInventory = inventory;
		
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
	 
	public static HandlerList getHandlerList() {
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
