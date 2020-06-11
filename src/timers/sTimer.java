package timers;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import enums.Msg;
import enums.Protections;
import main.IllegalStack;
import main.fListener;
import net.md_5.bungee.api.ChatColor;

public class sTimer implements Runnable {

	private static long signCheck = 0l;
	private static long chunkScan = 0l;
	private static boolean scanChunks = false;
	private static HashMap<Block,Player> signBlock = new HashMap<>();
	private static HashSet<BlockState[]> entList = new HashSet<>();
	private static HashSet<String> checkedChunks = new HashSet<>();
	
	public sTimer(IllegalStack illegalStack) {
		if(Protections.DestroyBadSignsonChunkLoad.isEnabled())
			scanChunks = true;
	}
	@Override
	public void run() {
		
		if(!Protections.DestroyBadSignsonChunkLoad.isEnabled() || Protections.DestroyBadSignsonChunkLoad.notifyOnly())
			return;
		if(getSignCheck() != -1l && System.currentTimeMillis() >= getSignCheck())
		{
		
			
			
			for(Block b:signBlock.keySet())
			{
				
				BlockState st = b.getState();
				if(st instanceof Sign) {
					Sign sign = (Sign) st;
					boolean illegal = false;
					for(String line:sign.getLines()) 
						if(!Charset.forName(Protections.ValidCharset.getTxtValue()).newEncoder().canEncode(ChatColor.stripColor(line))) { 
							illegal = true;
							System.out.println("[IllegalStack Debug]: - Found a sign with illegal chars: " + " line with invalid text was: " + line + " @" + sign.getLocation().toString());
						}
						
						
					
						
						
					Player p = signBlock.get(sign.getBlock());
					if(illegal) {
						if(p != null) 
							fListener.getLog().append(Msg.SignRemovedOnPlace.getValue(sign.getLocation(),signBlock.get(b).getName()));
							
							
						
					sign.getBlock().setType(Material.AIR);
					
		            p.kickPlayer(Msg.SignKickPlayerMsg.getValue());
					}
				}
			}
			
			signBlock.clear();
			setSignCheck(-1l);
			
		}
		
		if(System.currentTimeMillis() >= chunkScan && scanChunks)
		{
			HashSet<Block> found = new HashSet<>();
			
			for(BlockState[] bs:entList)
				for(BlockState st:bs)
					if(st instanceof Sign)
					{
						Sign sign = (Sign) st;
						for(String line:sign.getLines())
							if(!Charset.forName(Protections.ValidCharset.getTxtValue()).newEncoder().canEncode(ChatColor.stripColor(line))) {
								sign.getBlock().setType(Material.AIR);
								found.add(sign.getBlock());
							}
					
					}

			if(!found.isEmpty())
				for(Block b:found)
					fListener.getLog().append(Msg.SignRemoved.getValue(b.getLocation(), ""));
			chunkScan = System.currentTimeMillis() + 5000l;
			entList.clear();
		}

	}
	public static long getSignCheck() {
		return signCheck;
	}
	public static void setSignCheck(long signCheck) {
		sTimer.signCheck = signCheck;
	}
	public static void checkSign(Block block, Player player) {
		
		signBlock.put(block, player);
		if(signCheck == -1l)
			signCheck = System.currentTimeMillis() + 4000l;
		
	}
	public static void checkChunk(String chunkID, BlockState[] tileEntities) {
		
		if(!checkedChunks.contains(chunkID)) {
			entList.add(tileEntities);
			checkedChunks.add(chunkID);
		}
		
	}

}
