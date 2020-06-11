package fishing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

public class FishAttempt {
	static HashMap<UUID,FishAttempt>attempts = new HashMap<>();
	
	Long lastFish = 0l;
	private int count = 0;
	long spamThreshold = 1000l;
	Location lastLocation = null;
	private int sameSpotCount = 0;
	private List<Location> BLACKLIST = new ArrayList<>();
	double range = 0.3;
	public FishAttempt(Player p) {
		
		attempts.put(p.getUniqueId(), this);
	}

	public static FishAttempt findPlayer(Player p) {
		
		if(attempts.containsKey(p.getUniqueId()))
			return attempts.get(p.getUniqueId());
		
		return new FishAttempt(p);
	}

	public void fishCaught(Location hookLoc) {
		if(lastLocation == null || lastLocation.getWorld() != hookLoc.getWorld())
			lastLocation = hookLoc;
	
		Location testLoc = hookLoc.clone();
		testLoc.setY(lastLocation.getY());
		
				
		if(lastLocation.getWorld() == testLoc.getWorld() && lastLocation.distance(testLoc) <= range)
			setSameSpotCount(getSameSpotCount() + 1);
		else {
			setSameSpotCount(1);
			lastLocation = hookLoc;
			
		}
		
	}
	public void addAttempt() {
		if(System.currentTimeMillis() >= lastFish) 
			count = 1;
		else
			count ++;
		
		lastFish = System.currentTimeMillis() + spamThreshold;
		
		
			
		
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSameSpotCount() {
		return sameSpotCount;
	}

	public void setSameSpotCount(int sameSpotCount) {
		this.sameSpotCount = sameSpotCount;
	}

	public void reset() {
		sameSpotCount = 0;
		lastLocation = null;
		
	}

	public void blackList(Location location) {
		
		if(getBLACKLIST().size() > 3)
			getBLACKLIST().remove(0);
		
		getBLACKLIST().add(location);
	}

	public List<Location> getBLACKLIST() {
		return BLACKLIST;
	}

	public void setBLACKLIST(List<Location> bLACKLIST) {
		BLACKLIST = bLACKLIST;
	}

	public boolean isBlackListedSpot(Location location) {
		
		for(Location l:BLACKLIST)
		{
			Location testLoc = l.clone();
			testLoc.setY(location.getY());
			if(location.getWorld() != testLoc.getWorld())
				continue;
			
			if(testLoc.distance(location) <= range)
				return true;
		}
		return false;
	}

	public static FishHook findHook(Player player) {
		for(FishHook fh:player.getWorld().getEntitiesByClass(FishHook.class))
			if(fh.getShooter() instanceof Player)
				if(((Player)fh.getShooter()) == player)
					return fh;
		
		return null;
	}
	
}
