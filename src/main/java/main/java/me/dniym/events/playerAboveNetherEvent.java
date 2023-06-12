package main.java.me.dniym.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.dniym.enums.Protections;

public class playerAboveNetherEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	public playerAboveNetherEvent(Player p) {
		if(this.isAsynchronous()) {
			return;
		} else {
			p.damage(Protections.AboveNetherDamageAmount.getIntValue());

		}
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}
