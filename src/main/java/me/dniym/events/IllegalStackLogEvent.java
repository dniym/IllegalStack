package me.dniym.events;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.dniym.enums.Protections;

public class IllegalStackLogEvent extends Event implements Cancellable {

	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private String message = "";
	private Protections protection = null;
	
	public IllegalStackLogEvent(String message, Protections prot) {
		this.setMessage(message);
		this.setProtection(prot);
		
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Protections getProtection() {
		return protection;
	}

	public void setProtection(Protections protection) {
		this.protection = protection;
	}

}