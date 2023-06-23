package main.java.me.dniym.util;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.utils.Scheduler;

public class TrackedProjectile {
	private static final Set<TrackedProjectile> trackedProjectiles = new HashSet<TrackedProjectile>();
	private Long despawnTimestamp = 0l;
	private Entity entity;
	
	public TrackedProjectile(Projectile proj) {
		
	
		if(alreadyTracked(proj) || !shouldTrack(proj))
			return;
		
		entity = proj;
		despawnTimestamp = System.currentTimeMillis() + (Protections.ProjectileDespawnDelay.getIntValue() * 1000);
		trackedProjectiles.add(this);
	}

	private boolean shouldTrack(Projectile proj) {
		if(proj != null) {
			if(proj instanceof Arrow ||  proj instanceof ThrowableProjectile)
				return true;
		}
		return false;
	}

	public static boolean alreadyTracked(Entity ent) {
		
		for(TrackedProjectile tp:trackedProjectiles)
			if(ent.equals(tp.entity))
				return true;
		
		return false;
	}
	public static void manage() { 
		
		Set<TrackedProjectile> removed = new HashSet<>();
		
		for (TrackedProjectile tp:trackedProjectiles)
		{
			if(System.currentTimeMillis() > tp.despawnTimestamp) {
				//projectile has expired remove it.
				if(tp.entity != null) {
					removed.add(tp);
					tp.entity.remove();
					
				}
			}
		}
		
		trackedProjectiles.removeAll(removed);
		
	}

	/*
	 * I cannot figure out why this will not work async.   Doing this async always seems to result in the server crashing.. 
	 * I suspected the problem was due to the ProjectileLaunchEvent is fired twice whenever a projectile is launched, this seemed to be
	 * causing 4 objects to get tracked when 2 were fired..    However the crash persists whenever I try to do this async it pukes. 
	 * Nothing that is going on here is difficult to do sync.   I have moved this to a sync timer and it works flawlessly... 
	 */
	public static void manageAsync() {

		Set<TrackedProjectile> removed = new HashSet<>();
		
		for (TrackedProjectile tp:trackedProjectiles)
		{
			if(System.currentTimeMillis() > tp.despawnTimestamp) {
				//projectile has expired remove it.
				if(tp.entity != null) {
					removed.add(tp);
					Entity remove = tp.entity;
//					tp.entity.remove();
					Scheduler.executeOrScheduleSync(IllegalStack.getPlugin(), () -> {remove.remove();},  remove);
					
				}
			}
		}
		System.out.println("There are " + trackedProjectiles.size() + " projectiles being tracked");
		trackedProjectiles.removeAll(removed);

		
	}

}
