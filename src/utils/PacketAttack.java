package utils;

/*
 * This shouldn't be used anywhere, was some experimental code.  
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import com.comphenix.protocol.events.PacketEvent;

import main.fListener;

public class PacketAttack {

	private static HashSet<PacketAttack> attacks = new HashSet<>();
	private UUID playerid;
	private HashMap<String,Integer> pAttack = new HashMap<>();
	
	public PacketAttack(UUID uniqueId) {
		playerid = uniqueId;
		attacks.add(this);
	}

	public static PacketAttack findPlayer(UUID uniqueId) {
	
		for(PacketAttack pa:attacks)
			if(pa.playerid.equals(uniqueId))
				return pa;
		
		return new PacketAttack(uniqueId);
	}

	public boolean addAttempt(Object packetEvent, String attackType) {
		
		if(!this.pAttack.containsKey(attackType))
			this.pAttack.put(attackType,0);
		
		this.pAttack.put(attackType,this.pAttack.get(attackType) + 1);
		int maxAttempts = 250;
		
		if(attackType.equalsIgnoreCase("WINDOW_INVALID_CLICK"))
			maxAttempts = 5;
		if(attackType.equalsIgnoreCase("WINDOW_CLICK_SPAM"))
			maxAttempts = 80;
		
		return shouldCancelPacket(packetEvent,maxAttempts,attackType);
		
	}

	public boolean shouldCancelPacket(Object packetEvent, int maxAttempts,String attackType) {
		if (packetEvent instanceof PacketEvent) 
		{
			PacketEvent event = (PacketEvent) packetEvent;

			if (!event.isCancelled()) 
			{
				int size = event.getPacket().getBytes().toString().length();

				if(size > 2700) { //sending too large of a packet, cancel it regardless
					fListener.getLog().append("Player sent a packet too large for the server to handle!  Probably a packet attack!");
					event.getPlayer().kickPlayer("Invalid Packet Detected (packet too large)");
					return true;
				} else if (maxAttempts > 0) {
					if(this.pAttack.get(attackType) >= maxAttempts) {
						//player has sent too many invalid packets of a given type.
						System.out.println("Player sent too many bad packets of type: " + attackType + " kicking them.");
						event.getPlayer().kickPlayer("Too many bad packets detected! " + attackType);
						return true;
					}
				}
			}	
				
		}
		return false;
	}
}
