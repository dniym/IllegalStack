package main.java.me.dniym.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.timers.fTimer;
import main.java.me.dniym.utils.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.UUID;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class pLisbListener {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + pLisbListener.class.getSimpleName());

    Plugin plugin;
    int debug = 0;
    HashMap<UUID, Long> messageDelay = new HashMap<>();


    public pLisbListener(IllegalStack illegalStack) {
        plugin = illegalStack;

        //ProtocolLibrary.getProtocolManager().addPacketListener(new BookCrashExploitCheck(plugin));
        if (Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
        	ProtocolLibrary.getProtocolManager().addPacketListener(
            		new PacketAdapter(PacketAdapter.params(plugin, PacketType.Play.Client.SET_CREATIVE_SLOT).optionAsync()) {
                    //new PacketAdapter(plugin, PacketType.Play.Client.SET_CREATIVE_SLOT) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (!Protections.BlockBadItemsFromCreativeTab.isEnabled() || event.getPlayer().isOp() || event
                                    .getPlayer()
                                    .hasPermission("illegalstack.admin")) {
                                return;
                            }
                            try {
                                ItemStack stack = event.getPacket().getItemModifier().readSafely(0);
                                if (stack != null && stack.hasItemMeta()) {
                                    stack = new ItemStack(Material.AIR);
                                    final Player player = event.getPlayer();
                                    Scheduler.runTaskLater(plugin, player::updateInventory, 5L, player);
                                    event.setCancelled(true);
                                    Msg.StaffMsgCreativeBlock.getValue(event.getPlayer().getName());
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                LOGGER.error(
                                        "An error receiving a SET_CREATIVE_SLOT packet has occurred, you are probably using paper and have BlockBadItemsFromCreativeTab turned on.   This setting is needed very rarely, and ONLY if you have regular non-op players with access to /gmc.");
                            }
                        }
                    });
        }

        if (Protections.DisableChestsOnMobs.isEnabled()) {
    
        	
       		ProtocolLibrary.getProtocolManager().addPacketListener(
       				new PacketAdapter(PacketAdapter.params(plugin, PacketType.Play.Client.USE_ENTITY).optionAsync()) {
       					
       					/*
       					 * Must use optionAsync here... if optionSync is used it breaks player damage, eg no crits, no sweeping edge...
       					 * 
       					 * new PacketAdapter(PacketAdapter.params().plugin(plugin).optionSync().types(PacketType.Play.Client.USE_ENTITY)) {
       					 * 
       					 */
       					
       					

                        @Override
                        public void onPacketReceiving(PacketEvent event) {

                            if (event.getPacket().getIntegers().read(0) <= 0) {
                                return;
                            }

                            if (IllegalStack.hasChestedAnimals() && Protections.DisableChestsOnMobs.isEnabled()) {
                                    Entity entity;
                                    try {
                                        entity = event
                                                .getPacket()
                                                .getEntityModifier(event.getPlayer().getWorld())
                                                .read(0);
                                    } catch (RuntimeException ex) {
                                   //     LOGGER.error("Async Packet - Couldn't get an entity from id: ", ex);
                                        return;
                                    }
                                
                                    Scheduler.runTaskLater(this.plugin, () -> {
                                        if (entity instanceof Horse && ((Horse) entity).isTamed()) {
                                            ItemStack is = event.getPlayer().getInventory().getItemInHand();
                                            if (!fListener.is18() && (is == null || is.getType() != Material.CHEST)) {
                                                is = event.getPlayer().getInventory().getItemInOffHand();
                                            }
                                            if (is == null || is.getType() != Material.CHEST) {
                                                return;
                                            }
                                            exploitMessage(event.getPlayer(), entity);
                                            event.setCancelled(true);
                                            fTimer.getPunish().put(event.getPlayer(), entity);
                                            return;
                                        }

                                        if (entity instanceof ChestedHorse && ((ChestedHorse) entity).isTamed()) {
                                            ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
                                            if (is == null || is.getType() != Material.CHEST) {
                                                is = event.getPlayer().getInventory().getItemInOffHand();
                                            }
                                            if (is == null || is.getType() != Material.CHEST) {
                                                return;
                                            }
                                            exploitMessage(event.getPlayer(), entity);
                                            event.setCancelled(true);

                                            ((ChestedHorse) entity).setCarryingChest(true);
                                            ((ChestedHorse) entity).setCarryingChest(false);
                                            fTimer.getPunish().put(event.getPlayer(), entity);
                                        }
                                    }, 1, entity);
                                    

                            }

                        }
                    });
        }
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
