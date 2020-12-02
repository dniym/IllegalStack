package me.dniym.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;

public class Listener113 implements Listener {

	IllegalStack plugin;

	public Listener113(IllegalStack illegalStack) {
		plugin = illegalStack;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		System.out.println("[IllegalStack] - Enabling 1.13+ Checks");

	}

	@EventHandler 
	public void spawnerChangeCheck(PlayerInteractEvent event) {
		if(Protections.PreventSpawnEggsOnSpawners.isEnabled()) {

			Player plr = event.getPlayer();
			ItemStack is = plr.getInventory().getItemInMainHand();
			
			if(is == null)
				is = plr.getInventory().getItemInOffHand();
		
			if(is != null && is.getType().name().toLowerCase().contains("spawn_egg")) {
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

					Block blk = event.getClickedBlock();
					if(blk.getType() == Material.SPAWNER && !event.getPlayer().isOp())
					{
						plr.sendMessage(Msg.PlayerSpawnEggBlock.getValue());
						event.setCancelled(true);

					} else if (blk.getType() == Material.SPAWNER) 
						fListener.getLog().append(Msg.StaffMsgChangedSpawnerType.getValue(plr,is.getType().name()),Protections.PreventSpawnEggsOnSpawners);

					
				}
			}
		}
	}

}
