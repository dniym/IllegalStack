package me.dniym.listeners;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.utils.NBTStuff;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class protListener implements Listener {

    IllegalStack plugin;

    public protListener(IllegalStack illegalStack) {
        this.plugin = illegalStack;
    }

        @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {

        if (!Protections.PreventItemSwapLagExploit.isEnabled())
            return;
        if (!fListener.getInstance().getSwapDelay().containsKey(e.getPlayer()))
            fListener.getInstance().getSwapDelay().put(e.getPlayer(), 0l);
        if (System.currentTimeMillis() < fListener.getInstance().getSwapDelay().get(e.getPlayer()))
            e.setCancelled(true);
        else
            fListener.getInstance().getSwapDelay().put(e.getPlayer(), System.currentTimeMillis() + 750l);
    }


    @EventHandler
    public void onElytraFlight(EntityToggleGlideEvent e) {
        if (Protections.PreventInfiniteElytraFlight.isEnabled() && e.isGliding() && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getLocation().getBlockY() >= 255) {
                fListener.getLog().append(Msg.GlideActivateMaxBuild.getValue(p, ""));
                p.setGliding(false);
                e.setCancelled(true);
            }
            new BukkitRunnable() {
                final Player player = p;
                @Override
                public void run() {
                    if (player.getLocation().getBlockY() > 255 && player.isGliding()) {
                        fListener.getLog().append(Msg.GlideAboveMaxBuild.getValue(p, ""));
                        player.setGliding(false);
                    }
                }
            }.runTaskLater(this.plugin, 3250);

        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent e) {
        if (!Protections.PreventCobbleGenerators.isEnabled())
            return;

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
                fListener.getLog().append(Msg.PlayerRepairBlocked.getValue(e.getView().getPlayer().getName()));
                return;
            }

            if (is != null && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) {
                    e.setResult(new ItemStack(Material.AIR, 1));
                    e.getView().close();
                    fListener.getLog().append(Msg.PlayerRepairBlocked.getValue(e.getView().getPlayer().getName()));
                }
            }
        }
    }
}
