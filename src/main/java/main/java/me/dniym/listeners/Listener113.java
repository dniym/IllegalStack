package main.java.me.dniym.listeners;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Listener113 implements Listener {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Listener113.class.getSimpleName());
    IllegalStack plugin;

    public Listener113(IllegalStack illegalStack) {
        plugin = illegalStack;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        LOGGER.info("Enabling 1.13+ Checks");

    }

    @EventHandler
    public void spawnerChangeCheck(PlayerInteractEvent event) {
        if (Protections.PreventSpawnEggsOnSpawners.isEnabled()) {

            Player plr = event.getPlayer();
            ItemStack is = plr.getInventory().getItemInMainHand();

            if (is == null) {
                is = plr.getInventory().getItemInOffHand();
            }

            if (is != null && is.getType().name().toLowerCase().contains("spawn_egg")) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    Block blk = event.getClickedBlock();
                    if (blk.getType() == Material.SPAWNER && !event.getPlayer().isOp()) {
                        plr.sendMessage(Msg.PlayerSpawnEggBlock.getValue());
                        event.setCancelled(true);

                    } else if (blk.getType() == Material.SPAWNER) {
                        fListener.getLog().append(
                                Msg.StaffMsgChangedSpawnerType.getValue(plr, is.getType().name()),
                                Protections.PreventSpawnEggsOnSpawners
                        );
                    }


                }
            }
        }
    }

}
