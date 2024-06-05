package main.java.me.dniym.timers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.util.TrackedProjectile;

public class syncTimer implements Runnable {

    IllegalStack plugin;
    Long nextScan = 0l;
    int scanDelay = 3;

    private static Set<Entity> entitiesToRemove = new HashSet<Entity>();

    public syncTimer(IllegalStack illegalStack) {
        this.plugin = illegalStack;
        this.nextScan = System.currentTimeMillis() + (scanDelay * 1000);
    }

    @Override
    public void run() {

        if (!IllegalStack.isIsHybridEnvironment() && IllegalStack.isPaperServer()
                && IllegalStack.getMajorServerVersion() >= 16) {
            if (IllegalStack.isDisable() || Bukkit.getServer().isStopping()) {
                return;
            }
        }

        if (IllegalStack.isDisable()) {
            return;
        }

        if (System.currentTimeMillis() >= nextScan) {
            TrackedProjectile.manage();
        }

    }

    public static void removeEntity(Entity ent) {
        getEntitiesToRemove().add(ent);
    }

    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    public static void setEntitiesToRemove(Set<Entity> entitiesToRemove) {
        syncTimer.entitiesToRemove = entitiesToRemove;
    }


}
