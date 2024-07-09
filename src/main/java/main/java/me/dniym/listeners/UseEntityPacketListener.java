package main.java.me.dniym.listeners;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.timers.fTimer;
import main.java.me.dniym.utils.Scheduler;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class UseEntityPacketListener extends PacketListenerAbstract {

    private final IllegalStack plugin;
    private final HashMap<UUID, Long> messageDelay = new HashMap<>();

    public UseEntityPacketListener(IllegalStack plugin) {
        super(PacketListenerPriority.LOW);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
            return;
        }

        if (!IllegalStack.hasChestedAnimals()) {
            return;
        }

        if (!Protections.DisableChestsOnMobs.isEnabled()) {
            return;
        }

        User user = event.getUser();
        UUID uuid = user.getUUID();
        Player player = plugin.getServer().getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (player.isOp() || player.hasPermission("illegalstack.admin")) {
            return;
        }

        WrapperPlayClientInteractEntity packetWrapper = new WrapperPlayClientInteractEntity(event);

        World world = player.getWorld();
        int entityId = packetWrapper.getEntityId();

        if (entityId <= 0) {
            return;
        }

        Scheduler.runTask(plugin, () -> {

            Entity entity = world.getEntities().stream().filter(e -> e.getEntityId() == entityId).findFirst().orElse(null);

            Scheduler.runTaskLater(plugin, () -> {

                if (entity instanceof Horse && ((Horse) entity).isTamed()) {
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (!fListener.is18() && (is == null || is.getType() != Material.CHEST)) {
                        is = player.getInventory().getItemInOffHand();
                    }
                    if (is == null || is.getType() != Material.CHEST) {
                        return;
                    }
                    exploitMessage(player, entity);
                    event.setCancelled(true);
                    fTimer.getPunish().put(player, entity);
                    return;
                }

                if (entity instanceof ChestedHorse && ((ChestedHorse) entity).isTamed()) {
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (is == null || is.getType() != Material.CHEST) {
                        is = player.getInventory().getItemInOffHand();
                    }
                    if (is == null || is.getType() != Material.CHEST) {
                        return;
                    }
                    exploitMessage(player, entity);
                    event.setCancelled(true);

                    ((ChestedHorse) entity).setCarryingChest(true);
                    ((ChestedHorse) entity).setCarryingChest(false);
                    fTimer.getPunish().put(player, entity);
                }

            }, 1, entity);

        }, player.getLocation());
    }

    private void exploitMessage(Player p, Entity ent) {
        if (!messageDelay.containsKey(p.getUniqueId())) {
            messageDelay.put(p.getUniqueId(), 0L);
        }

        if (System.currentTimeMillis() > messageDelay.get(p.getUniqueId())) {
            p.sendMessage(Msg.PlayerDisabledHorseChestMsg.getValue());
            fListener.getLog().append2(Msg.ChestPrevented.getValue(p, ent));
            messageDelay.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
        }
    }

}
