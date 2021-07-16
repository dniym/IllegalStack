package main.java.me.dniym.commands;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.SpigotMethods;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class IllegalStackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }

                sender.sendMessage(ChatColor.GREEN + "[IllegalStack] - All configuration settings have been reloaded.");
                IllegalStack.ReloadConfig(true);
                return true;
            }
        }

        if (args.length >= 1) {
            if (args[0].toLowerCase().startsWith("ver")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }

                String ver = IllegalStack.getPlugin().getDescription().getVersion();
                sender.sendMessage(ChatColor.AQUA + "-----===== Illegal Stack (" + ver + ") =====-----");
                sender.sendMessage(ChatColor.GOLD + " Detected Server Version: " + IllegalStack.getVersion() + (IllegalStack.isSpigot()
                        ? ""
                        : "Spigot"));
                return true;

            }

            if (args[0].toLowerCase().startsWith("fix")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("IllegalStack.fixCommand")) {
                        Protections.fixEnchants(((Player) sender));
                    } else {
                        sender.sendMessage("You don't have permission to force fix enchantment levels on an item.");
                    }
                } else {
                    sender.sendMessage("This command can only be used in game by a player.");
                }
                return true;

            }
            if (args[0].toLowerCase().startsWith("prot")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== Illegal Stack Protections =====-----");
                sender.sendMessage(ChatColor.GOLD + " Detected Server Version: " + IllegalStack.getVersion() + (IllegalStack.isSpigot()
                        ? ""
                        : "Spigot"));
                sender.sendMessage(ChatColor.AQUA + "-" + "   Version Specific Protections   -");
                int parentId = 0;
                int catId = 0;
                if (args.length >= 2) {
                    if (StringUtils.isNumeric(args[1].trim())) {
                        catId = Integer.parseInt(args[1].trim());
                    }
                }
                if (args.length == 3) {
                    if (StringUtils.isNumeric(args[1].trim())) {
                        parentId = Integer.parseInt(args[2].trim());
                    }
                }
                for (Protections p : Protections.values()) {
                    if (!p.getCommand().isEmpty()) {
                        continue;
                    }
                    if (p.isVersionSpecific(IllegalStack.getVersion()) && p.isRelevantToVersion(IllegalStack.getVersion()) && p.getCatId() != 2) {
                        sendProtection(sender, p, parentId, catId);
                    }
                }

                //sender.sendMessage(ChatColor.AQUA + "-     Multi-Version Protections     -");
                sendCategory(sender, "Multi-Version Protections", (catId == 1), 1);
                if (catId == 1) {
                    for (Protections p : Protections.values()) {
                        if (!p.getCommand().isEmpty() || p.getCatId() == 2) {
                            continue;
                        }
                        if (p.isRelevantToVersion(IllegalStack.getVersion()) && !p.isVersionSpecific(IllegalStack.getVersion())) {
                            sendProtection(sender, p, parentId, catId);
                        }
                    }

                }

                sendCategory(sender, "Misc / User Requested Features", (catId == 2), 2);
                //sender.sendMessage(ChatColor.AQUA + "-  Misc / User Requested Features  -");
                if (catId == 2) {
                    for (Protections p : Protections.values()) {
                        if (!p.getCommand().isEmpty()) {
                            continue;
                        }

                        if (p.isRelevantToVersion(IllegalStack.getVersion()) && p.getCatId() == catId) {
                            sendProtection(sender, p, parentId, catId);
                        }
                    }
                }
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("enchantwhitelistmode")) {
                if (sender instanceof Player) {
                    Player plr = (Player) sender;
                    if (!fListener.getInstance().getItemWatcher().remove(plr)) {
                        fListener.getInstance().getItemWatcher().add(plr);
                        plr.sendMessage(Msg.StaffEnchantBypass.getValue());
                        return true;
                    } else {
                        plr.sendMessage(Msg.StaffEnchantBypassCancel.getValue());
                        return true;

                    }
                }
            }
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("teleport")) {
                if (!hasPerm(sender, "illegalstack.notify")) {
                    return true;
                }
                if (sender instanceof Player) {
                    if (args.length >= 4) {
                        World w = IllegalStack.getPlugin().getServer().getWorld(args[4]);
                        Location loc = new Location(
                                w,
                                Integer.parseInt(args[1]),
                                Integer.parseInt(args[2]),
                                Integer.parseInt(args[3])
                        );
                        ((Player) sender).teleport(loc);
                        return true;
                    }
                } else {
                    sender.sendMessage("This command can only be used by players.");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                Protections p = Protections.getProtection(args[1]);
                if (p != null) {
                    p.toggleProtection();
                    String status = "ON";
                    if (!p.isEnabled()) {
                        status = "OFF";
                    }
                    fListener.getLog().append2(Msg.StaffProtectionToggleMsg.getValue(p, sender.getName(), status));
                    refreshCommands(sender);
                    return true;
                }
            }

        }

        if (args.length >= 4) {
            if (!hasPerm(sender, "illegalstack.admin")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("value")) {

                Protections pro = Protections.getProtection(args[2]);
                if (pro == null) {
                    sender.sendMessage(Msg.StaffInvalidProtectionMsg.getValue());
                    StringBuilder prots = new StringBuilder();
                    for (Protections p : Protections.values()) {
                        if (p.isList()) {
                            prots.append(p.name()).append(", ");
                        }
                    }
                    sender.sendMessage(ChatColor.GRAY + prots.toString());

                    return true;
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    StringBuilder val = new StringBuilder(args[3].trim());
                    if (args.length >= 4) {
                        val = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            val.append(args[i]).append(" ");
                        }

                        val = new StringBuilder(val.toString().trim());

                    }

                    pro.remTxtSet(val.toString(), sender);
                    //refreshCommands(sender);
                    return true;
                }

                if (args[1].equalsIgnoreCase("add")) {

                    if (pro.validate(args[3].trim(), sender)) {
                        sender.sendMessage(Msg.StaffOptionUpdated.getValue());
                        //refreshCommands(sender);
                    }
                    return true;
                }
            }
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("toggle")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== Available Protection Toggles =====-----");
                StringBuilder prots = new StringBuilder();
                for (Protections p : Protections.values()) {
                    if (p.getParentId() == 0) {
                        prots.append(p.name()).append(", ");
                    }
                }

                sender.sendMessage(ChatColor.GRAY + prots.toString());
                sender.sendMessage(ChatColor.GOLD + "/istack toggle <protection>" + ChatColor.GRAY + " - Toggles a protection on/off");
                return true;
            }

            if (args[0].equalsIgnoreCase("value")) {
                if (!hasPerm(sender, "illegalstack.admin")) {
                    return true;
                }
                if (args.length > 3 && args[1].equalsIgnoreCase("add")) {
                    Protections pro = Protections.getProtection(args[2]);
                    if (pro != Protections.ItemNamesToRemove && pro != Protections.ItemLoresToRemove) {
                        sender.sendMessage(Msg.StaffSingleWordsOnly.getValue());
                        return true;
                    }
                    StringBuilder text = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    if (pro.validate(text.toString().trim(), sender)) {
                        sender.sendMessage(Msg.StaffStringUpdated.getValue(text.toString()));
                    }
                    return true;

                }
                if (args.length > 3 && args[1].equalsIgnoreCase("set")) {
                    Protections pro = Protections.getProtection(args[2]);
                    StringBuilder text = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    if (pro.validate(text.toString(), sender)) {
                        sender.sendMessage(Msg.StaffOptionUpdated.getValue());
                        //refreshCommands(sender);
                    }

                    return true;
                }
                sender.sendMessage(ChatColor.AQUA + "-----===== Available Protection Options =====-----");
                sender.sendMessage(ChatColor.GOLD + "/istack values < set | remove > <protection>" + ChatColor.GRAY + " - Add/Remove a value from a protection's list");
            }
        }

        sender.sendMessage(ChatColor.AQUA + "Illegal Stack - Available Commands");
        sender.sendMessage(ChatColor.GOLD + "/istack protections" + ChatColor.GRAY + " Shows Protection Status'");
        sender.sendMessage(ChatColor.GOLD + "/istack toggle <protection>" + ChatColor.GRAY + " - Toggles a protection on/off");
        sender.sendMessage(ChatColor.GOLD + "/istack reload" + ChatColor.GRAY + " - Reloads config from config.yml");

        return true;
    }

    private boolean hasPerm(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        }
        sender.sendMessage(Msg.StaffMsgNoPerm.getValue(perm));

        return false;
    }

    private void sendCategory(CommandSender sender, String category, Boolean show, int catId) {
        Player plr = null;
        if (sender instanceof Player) {
            plr = (Player) sender;
        }

        if (IllegalStack.isSpigot() && plr != null) {
            plr.spigot().sendMessage(SpigotMethods.makeCategoryText(category, show, catId));
        }
    }

    private void sendProtection(CommandSender sender, Protections p, int parentId, int catId) {

        boolean line = false;
        String status = ChatColor.GREEN + " ON";
        Player plr = null;
        if (sender instanceof Player) {
            plr = (Player) sender;
        }
        if (!p.isEnabled()) {
            status = ChatColor.DARK_RED + "OFF";
        }
        if (IllegalStack.isSpigot() && plr != null) {
            boolean hasKids = false;
            HashSet<Protections> kids = new HashSet<>();
            for (Protections child : Protections.values()) {
                if (child.getParentId() == p.getProtId()) {
                    hasKids = true;
                    kids.add(child);
                }
            }

            plr.spigot().sendMessage(SpigotMethods.makeParentText(p, status, hasKids, catId));
            for (Protections child : kids) {
                if (parentId != 0 && parentId == p.getProtId()) {
                    plr.spigot().sendMessage(SpigotMethods.makeChildText(child, catId));
                }
            }

        } else {
            if (!p.isList()) {
                sender.sendMessage(ChatColor.GOLD + "[" + status + ChatColor.GOLD + "] " + ChatColor.DARK_AQUA + "" + p.getDisplayName() + "");
            } else {
                sender.sendMessage(ChatColor.DARK_GRAY + "[" + "   " + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "" + p.getDisplayName() + " " + p
                        .findValue());
            }
            for (Protections child : Protections.values()) {
                if (child.getParentId() == p.getProtId()) {
                    sender.sendMessage(ChatColor.AQUA + "-> " + ChatColor.DARK_AQUA + child.getDisplayName() + " " + ChatColor.GRAY + child
                            .findValue());
                    line = false;
                }
            }
        }
        if (line) {
            sender.sendMessage(" ");
        }
    }

    private void refreshCommands(final CommandSender sender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                IllegalStack.getPlugin().getServer().dispatchCommand(sender, "istack prot");
            }
        }.runTaskLater(IllegalStack.getPlugin(), 5);
    }

}
