package me.dniym.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.timers.fTimer;
import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class pLisbListener {

    Plugin plugin;
    int debug = 0;
    HashMap<UUID, Long> messageDelay = new HashMap<>();


    public pLisbListener(IllegalStack illegalStack, String version) {
        plugin = illegalStack;

        //ProtocolLibrary.getProtocolManager().addPacketListener(new BookCrashExploitCheck(plugin));
        if (Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(plugin, PacketType.Play.Client.SET_CREATIVE_SLOT) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (!Protections.BlockBadItemsFromCreativeTab.isEnabled() || event.getPlayer().isOp() || event.getPlayer().hasPermission("illegalstack.admin"))
                                return;
                            try {
                                ItemStack stack = event.getPacket().getItemModifier().readSafely(0);
                                if (stack != null && stack.hasItemMeta()) {
                                    stack = new ItemStack(Material.AIR);
                                    final Player player = event.getPlayer();
                                    plugin.getServer().getScheduler().runTaskLater(plugin, player::updateInventory, 5L);
                                    event.setCancelled(true);
                                    Msg.StaffMsgCreativeBlock.getValue(event.getPlayer().getName());
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                System.out.println("[IllegalStack] - an error receiving a SET_CREATIVE_SLOT packet has occured, you are probably using paper and have BlockBadItemsFromCreativeTab turned on.   This setting is needed very rarely, and ONLY if you have regular non-op players with access to /gmc.");
                            }
                        }
                    });
        }

        if (Protections.DisableChestsOnMobs.isEnabled()) {
        	
            ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
                    	
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                        	
                        	
                        	if(event.getPacket().getIntegers().read(0) <= 0) {
                        		System.out.println(event.getPlayer().getName() + " tried to interact with entity with an invalid id: " + event.getPacket().getIntegers().read(0) + " if this happens often please investigate what the player is doing and message dNiym on spigot or the IllegalStack discord.");
                        		event.setCancelled(true);
                        		return;
                        	}
                        	
                            if (IllegalStack.hasChestedAnimals()) {
                                try {
                                    Entity entity = event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0);
                                    if (entity instanceof ChestedHorse && ((ChestedHorse) entity).isTamed()) {
                                        ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
                                        if (is == null || is.getType() != Material.CHEST)
                                            is = event.getPlayer().getInventory().getItemInOffHand();
                                        if (is == null || is.getType() != Material.CHEST)
                                            return;
                                        exploitMessage(event.getPlayer(), entity);
                                        event.setCancelled(true);
//                                        event.setPacket(new PacketContainer(Packet));
                                        ((ChestedHorse) entity).setCarryingChest(true);
                                        ((ChestedHorse) entity).setCarryingChest(false);
                                        fTimer.getPunish().put(event.getPlayer(), entity);
                                    }
                                } catch (IndexOutOfBoundsException ex) {
                                    System.out.println("[IllegalStack] - an error receiving a USE_ENTITY packet has occured, ");
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    Entity entity = event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0);
                                    if (entity instanceof Horse && ((Horse) entity).isTamed()) {
                                        ItemStack is = event.getPlayer().getInventory().getItemInHand();
                                        if (!fListener.getInstance().is18() && (is == null || is.getType() != Material.CHEST))
                                            is = event.getPlayer().getInventory().getItemInOffHand();
                                        if (is == null || is.getType() != Material.CHEST)
                                            return;
                                        exploitMessage(event.getPlayer(), entity);
                                        event.setCancelled(true);
                                        //event.setPacket(new PacketContainer(0));
                                        //	((Horse) entity).setCarryingChest(true);
                                        //((Horse) entity).setCarryingChest(false);
                                        fTimer.getPunish().put(event.getPlayer(), entity);
                                    }
                                } catch (IndexOutOfBoundsException ex) {
                                    System.out.println("[IllegalStack] - an error receiving a USE_ENTITY packet has occurred, ");
                                    ex.printStackTrace();
                                }
                            }

                        }
                    });
        }
    }

    private void exploitMessage(Player p, Entity ent) {
        if (!messageDelay.containsKey(p.getUniqueId()))
            messageDelay.put(p.getUniqueId(), 0L);

        if (System.currentTimeMillis() > messageDelay.get(p.getUniqueId())) {
            p.sendMessage(Msg.PlayerDisabledHorseChestMsg.getValue());
            fListener.getLog().append2(Msg.ChestPrevented.getValue(p, ent));
            messageDelay.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
        }
    }
}
