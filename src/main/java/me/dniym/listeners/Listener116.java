package me.dniym.listeners;

import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import me.dniym.IllegalStack;
import me.dniym.enums.Protections;

public class Listener116 implements Listener {

	public Listener116(IllegalStack plugin) {
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(Protections.PreventPiglinDupe.isEnabled() && e.getEntity() instanceof Piglin)  
		{
			Piglin p = (Piglin) e.getEntity();
			p.setCanPickupItems(false);
		}
	}

	@EventHandler
	public void onEntityPickup(EntityPickupItemEvent e) {
		if(Protections.PreventPiglinDupe.isEnabled() && e.getEntity() instanceof Piglin)
		{
			e.setCancelled(true);
			Piglin pig = (Piglin)e.getEntity();
			pig.setCanPickupItems(false);
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(Protections.PreventPiglinDupe.isEnabled() && e.getEntity() instanceof Piglin) {
			e.getDrops().clear();
			
		}
	}
	
}
