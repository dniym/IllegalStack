package main.java.me.dniym.listeners;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.utils.NBTStuff;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ProtectionListener implements Listener {

    IllegalStack plugin;
    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + ProtectionListener.class.getSimpleName());
    public ProtectionListener(IllegalStack illegalStack) {
        this.plugin = illegalStack;
    }

    @EventHandler
    public void onPortal(PortalCreateEvent event) {
        if (Protections.DisableInWorlds.isWhitelisted(event.getWorld().getName())) {
            return;
        }

        if (Protections.PreventBedrockDestruction.isEnabled()) {
            for (BlockState b : event.getBlocks()) {
                if (fListener.getUnbreakable().contains(b.getType())) {
                    //Blocking breaking of unbreakable blocks.
                    LOGGER.info("Portal tried to break an unbreakable block: {}", b.getType().name());
                    event.setCancelled(true);
                    break;
                }
                if (b.getY() > 255) {
                    //Blocking portals spawning at world height limit, preventing from https://i.imgur.com/mqAXdpU.png
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {

        if (!Protections.PreventItemSwapLagExploit.isEnabled()) {
            return;
        }
        if (!fListener.getInstance().getSwapDelay().containsKey(e.getPlayer())) {
            fListener.getInstance().getSwapDelay().put(e.getPlayer(), 0L);
        }
        if (System.currentTimeMillis() < fListener.getInstance().getSwapDelay().get(e.getPlayer())) {
            e.setCancelled(true);
        } else {
            fListener.getInstance().getSwapDelay().put(e.getPlayer(), System.currentTimeMillis() + 750L);
        }
    }


    @EventHandler
    public void onElytraFlight(EntityToggleGlideEvent e) {
        if (Protections.PreventInfiniteElytraFlight.isEnabled() && e.isGliding() && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getLocation().getBlockY() >= 255) {
                fListener.getLog().append2(Msg.GlideActivateMaxBuild.getValue(p, ""));
                p.setGliding(false);
                e.setCancelled(true);
            }
            new BukkitRunnable() {
                final Player player = p;

                @Override
                public void run() {
                    if (player.getLocation().getBlockY() > 255 && player.isGliding()) {
                        fListener.getLog().append2(Msg.GlideAboveMaxBuild.getValue(p, ""));
                        player.setGliding(false);
                    }
                }
            }.runTaskLater(this.plugin, 3250);

        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent e) {
        if (!Protections.PreventCobbleGenerators.isEnabled()) {
            return;
        }

        if (e.getNewState().getType() == Material.COBBLESTONE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRepairAttempt(PrepareAnvilEvent e) {
        if (Protections.BlockRepairsInstead.isEnabled()) {
            ItemStack is = e.getResult();
            if (NBTStuff.hasNbtTag("IllegalStack", is, "NoRepair", Protections.BlockRepairsInstead)) {
                e.setResult(new ItemStack(Material.AIR, 1));
                e.getView().close();
                fListener.getLog().append2(Msg.PlayerRepairBlocked.getValue(e.getView().getPlayer().getName()));
                return;
            }

            if (is != null && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {
                    e.setResult(new ItemStack(Material.AIR, 1));
                    e.getView().close();
                    fListener.getLog().append2(Msg.PlayerRepairBlocked.getValue(e.getView().getPlayer().getName()));
                }
            }
        }
    }

}
