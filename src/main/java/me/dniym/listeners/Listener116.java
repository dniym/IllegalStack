package me.dniym.listeners;

import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import me.dniym.IllegalStack;
import me.dniym.enums.Protections;

public class Listener116 implements Listener {

	public Listener116(IllegalStack plugin) {
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(Protections.PreventPiglinDupe.isEnabled() && !IllegalStack.isPaper() && e.getEntity() instanceof Piglin)  
		{
			Piglin p = (Piglin) e.getEntity();
			p.setCanPickupItems(false);
		}
	}

}
