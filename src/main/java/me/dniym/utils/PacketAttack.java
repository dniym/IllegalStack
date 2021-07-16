package main.java.me.dniym.utils;

/*
 * This shouldn't be used anywhere, was some experimental code.
 */

import com.comphenix.protocol.events.PacketEvent;
import main.java.me.dniym.listeners.fListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PacketAttack {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + PacketAttack.class.getSimpleName());

    private static final HashSet<PacketAttack> attacks = new HashSet<>();
    private final UUID playerid;
    private final HashMap<String, Integer> pAttack = new HashMap<>();

    public PacketAttack(UUID uniqueId) {
        playerid = uniqueId;
        attacks.add(this);
    }

    public static PacketAttack findPlayer(UUID uniqueId) {

        for (PacketAttack pa : attacks) {
            if (pa.playerid.equals(uniqueId)) {
                return pa;
            }
        }

        return new PacketAttack(uniqueId);
    }

    public boolean addAttempt(Object packetEvent, String attackType) {

        if (!this.pAttack.containsKey(attackType)) {
            this.pAttack.put(attackType, 0);
        }

        this.pAttack.put(attackType, this.pAttack.get(attackType) + 1);
        int maxAttempts = 250;

        if (attackType.equalsIgnoreCase("WINDOW_INVALID_CLICK")) {
            maxAttempts = 5;
        }
        if (attackType.equalsIgnoreCase("WINDOW_CLICK_SPAM")) {
            maxAttempts = 80;
        }

        return shouldCancelPacket(packetEvent, maxAttempts, attackType);

    }

    public boolean shouldCancelPacket(Object packetEvent, int maxAttempts, String attackType) {
        if (packetEvent instanceof PacketEvent) {
            PacketEvent event = (PacketEvent) packetEvent;

            if (!event.isCancelled()) {
                int size = event.getPacket().getBytes().toString().length();

                if (size > 2700) { //sending too large of a packet, cancel it regardless
                    fListener.getLog().append2(
                            "Player sent a packet too large for the server to handle!  Probably a packet attack!");
                    event.getPlayer().kickPlayer("Invalid Packet Detected (packet too large)");
                    return true;
                } else if (maxAttempts > 0) {
                    if (this.pAttack.get(attackType) >= maxAttempts) {
                        //player has sent too many invalid packets of a given type.
                        LOGGER.info("Player sent too many bad packets of type: {} kicking them.", attackType);
                        event.getPlayer().kickPlayer("Too many bad packets detected! " + attackType);
                        return true;
                    }
                }
            }

        }
        return false;
    }

}
