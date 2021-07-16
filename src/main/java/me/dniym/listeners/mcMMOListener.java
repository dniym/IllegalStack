package main.java.me.dniym.listeners;

import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import main.java.me.dniym.IllegalStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.UUID;

public class mcMMOListener implements Listener {

    private static final HashSet<UUID> mcMMOactive = new HashSet<>();
    private final IllegalStack plugin;

    public mcMMOListener(IllegalStack illegalStack) {
        this.plugin = illegalStack;
    }

    public static boolean ismcMMOActive(Player player) {
        if (mcMMOactive == null) {
            return false;
        }
        return mcMMOactive.contains(player.getUniqueId());
    }

    /*
    @EventHandler
    public void onPotionBrew(McMMOPlayerBrewEvent e) {
        BrewingStand bs =(BrewingStand) e.getBrewingStand();
        for(int i = 0; i < bs.getInventory().getStorageContents().length;i++) {
            ItemStack is = bs.getInventory().getStorageContents()[i];

            if(is == null)
                continue;

            if(is.getType() == Material.POTION || is.getType() == Material.SPLASH_POTION)
            {
                int slot = i;

                new BukkitRunnable()
                {

                    @Override
                    public void run()
                    {

                        bs.getInventory().setItem(slot, NBTStuff.addNBTTag(is, "mcmmoitem"));

                    }
                }.runTaskLater(this.plugin, 12);

            }

        }
    }
    */
    @EventHandler
    public void onAbilActivate(McMMOPlayerAbilityActivateEvent e) {
        mcMMOactive.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onAbilDeactivate(McMMOPlayerAbilityDeactivateEvent e) {
        mcMMOactive.remove(e.getPlayer().getUniqueId());
    }

}
