package main.java.me.dniym.logging;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.events.IllegalStackLogEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Log {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Log.class.getSimpleName());
    IllegalStack plugin;
    File file;
    Calendar date;

    public Log(IllegalStack plugin) {
        this.plugin = plugin;
        if (Protections.LogOffensesInSeparateFile.isEnabled()) {
            file = new File(plugin.getDataFolder() + "/OffenseLog.txt");
        }
        date = Calendar.getInstance();
    }

    @Deprecated
    public void append2(String message) {
        this.append(message, null);

    }

    public void append(String message, Protections prot) {

        if (prot != null) {
            IllegalStackLogEvent event = new IllegalStackLogEvent(message, prot);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }

        if (Protections.LogOffensesInSeparateFile.isEnabled()) {
            try {
                LOGGER.info(message);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(dateStamp()).append(message).append("\r\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info(message);
        }

        if (Protections.InGameNotifications.isEnabled()) {
            for (Player p : IllegalStack.getPlugin().getServer().getOnlinePlayers()) {
                if (p.hasPermission("illegalstack.notify")) {
                    message = cleanMessage(message);

                    if (IllegalStack.isSpigot() && message.contains("@")) {
                        TextComponent msg = new TextComponent(message);
                        msg.setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(Msg.PluginTeleportText.getValue()).create()
                        ));
                        msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, getTeleportLoc(message)));
                        p.spigot().sendMessage(msg);
                    } else {
                        p.sendMessage(Msg.PluginPrefix.getValue() + ChatColor.RESET + message);
                    }
                }
            }
        }
    }

    private String getTeleportLoc(String message) {
        String[] words = message.split("@");
        String[] coords = words[1].split(" ");
        String position = "";

        try {
            int x = Integer.parseInt(coords[2]);
            int y = Integer.parseInt(coords[3]);
            int z = Integer.parseInt(coords[4]);
            position = "/istack teleport " + x + " " + y + " " + z + " " + ChatColor.stripColor(coords[1]);
        } catch (NumberFormatException ex) {
            LOGGER.error("Failed to get position");
            for (int i = 0; i < coords.length; i++) {
                LOGGER.error("Coord: {} {}", i, coords[i]);
            }
        }
        return position;
    }

    public String cleanMessage(String message) {
        if (message.contains("@Location{") || message.contains("@ Location{")) {
            String msg = message.substring(message.indexOf('@'));
            String[] words = msg.split(",");
            String[] wld = words[0].split("name=");

            msg = wld[0].split("@")[0];
			/*
			for(int i = 0; i < words.length;i++)

			for(int i = 0; i < wld.length;i++)
			*/

            String world = wld[1].substring(0, wld[1].indexOf("}"));
            int x = (int) Double.parseDouble(words[1].substring(2));
            int y = (int) Double.parseDouble(words[2].substring(2));
            int z = (int) Double.parseDouble(words[3].substring(2));

            if (Protections.PlayerOffenseNotifications.isEnabled()) {
                World w = IllegalStack.getPlugin().getServer().getWorld(world);
                Location offense = new Location(w, x, y, z);
                for (Player p : w.getPlayers()) {
                    if (p.getLocation().distance(offense) <= 10) {
                        //[IllegalStack] -Stopped Retraction Dupe Glitch & Removed Piston @ Location{world=CraftWorld{name=event},x=-266.0,y=81.0,z=-238.0,pitch=0.0,yaw=0.0}
                        String mNear = message.substring(0, message.indexOf('@'));
                        mNear = Msg.PluginPrefix.getValue() + " " + Msg.PlayerNearbyNotification.getValue(mNear);
                        p.sendMessage(mNear);
                    }
                }
            }

            String coords = "@ " + ChatColor.AQUA + world + " " + x + " " + y + " " + z;
            //message = message + " @ " + ChatColor.AQUA + world + " " + x + " " + y + " " + z;
            message = message.substring(0, message.indexOf('@')) + " " + coords;
        }
        return message;
    }

    public String dateStamp() {
        String now;

        now = monthFromNumber(date.get(Calendar.MONTH)) + " " + date.get(Calendar.DAY_OF_MONTH) + " " + date.get(Calendar.YEAR) + " (" + date
                .get(Calendar.HOUR) + ":" +
                date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND) + ") - ";
        return now;
    }

    public String monthFromNumber(int month) {

        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "Unknown";
    }

    public void notify(Protections prot, String message) {

        if (Protections.LogOffensesInSeparateFile.isEnabled()) {
            try {
                LOGGER.info("(Notification Only) {} {}", prot.name(), message);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(dateStamp()).append(message).append("\r\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("(Notification Only) {}", message);
        }

        if (Protections.LogOffensesInSeparateFile.isEnabled()) {
            for (Player p : IllegalStack.getPlugin().getServer().getOnlinePlayers()) {
                if (p.hasPermission("illegalstack.notify")) {
                    message = cleanMessage(message);

                    if (IllegalStack.isSpigot() && message.contains("@")) {

                        TextComponent msg = new TextComponent(ChatColor.GREEN + "(Notification Only) " + message);
                        msg.setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(Msg.PluginTeleportText.getValue()).create()
                        ));
                        msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, getTeleportLoc(message)));
                        p.spigot().sendMessage(msg);
                    } else {
                        p.sendMessage(ChatColor.RED + "[IllegalStack] - (Notification Only)" + ChatColor.RESET + message);
                    }
                }
            }
        }
    }

}
