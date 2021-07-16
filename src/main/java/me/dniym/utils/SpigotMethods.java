package main.java.me.dniym.utils;

import main.java.me.dniym.enums.Protections;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.LivingEntity;

public class SpigotMethods {

    public static BaseComponent makeParentText(Protections protections, String status, boolean children, int catId) {

        String sTag = "     ";
        if (!protections.isList()) {
            sTag = ChatColor.DARK_AQUA + "[" + status + ChatColor.DARK_AQUA + "]";
        }

        TextComponent cLink = new TextComponent(sTag);
        if (!protections.isList()) {
            cLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/istack toggle " + protections.name()));
            cLink.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Toggle This Protection On/Off").create()
            ));
        } else {
            cLink = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GREEN + "A");
            String cmd = "/istack value add " + protections.name();
            cLink.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
            cLink.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Add a value to this list").create()
            ));
            TextComponent opt;

            if (!protections.getTxtSet().isEmpty()) {
                opt = new TextComponent(ChatColor.RED + "R");
                cmd = "/istack value remove " + protections.name();
                opt.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
                opt.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Remove a value from this list").create()
                ));

                cLink.addExtra(opt);
            } else {
                cLink.addExtra(" ");
            }
            opt = new TextComponent(ChatColor.AQUA + "L");
            opt.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.AQUA + protections.findValue()).create()
            ));

            cLink.addExtra(opt);
            cLink.addExtra(ChatColor.GRAY + "] ");
        }

        cLink.addExtra(ChatColor.GOLD + " " + protections.getDisplayName());
        TextComponent info = new TextComponent(ChatColor.GREEN + " *");
        if (!protections.getDescription().equalsIgnoreCase("")) {
            String desc = protections.getDescription() + ChatColor.AQUA + " " + protections.getVersion() + ChatColor.RESET;
            info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc).create()));
        }
        cLink.addExtra(info);

        //kids
        if (children) {
            TextComponent opt = new TextComponent(ChatColor.YELLOW + "(more options)");
            opt.setClickEvent(new ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/istack prot " + catId + " " + protections.getProtId()
            ));
            opt.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to show/hide more options").create()
            ));
            cLink.addExtra(opt);
        }

        return cLink;
    }


    public static BaseComponent makeCategoryText(String category, Boolean show, int catID) {

        TextComponent cLink = new TextComponent(ChatColor.AQUA + "-     " + category + "     -");
        String sText = ChatColor.GRAY + " (" + ChatColor.GREEN + "SHOW" + ChatColor.GRAY + ")";

        if (show) {
            sText = ChatColor.GRAY + " (" + ChatColor.RED + "HIDE" + ChatColor.GRAY + ")";
        }

        TextComponent option = new TextComponent(sText);
        option.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Click to activate").create()
        ));

        String command = "/istack prot ";
        if (!show) {
            if (catID == 1) {
                command = command + 1;
            }
            if (catID == 2) {
                command = command + 2;
            }
        }

        option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        cLink.addExtra(option);
        return cLink;
    }

    public static BaseComponent makeChildText(Protections p, int catId) {

        TextComponent cLink = new TextComponent(ChatColor.GRAY + "  -> ");

        cLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(p.getDescription()).create()));

        if (p.isList()) {
            TextComponent option = new TextComponent(ChatColor.GOLD + " [" + ChatColor.GREEN + "A");
            if (p == Protections.EnchantedItemWhitelist) {
                option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/istack enchantwhitelistmode"));
                option.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to toggle adding EnchantedItemWhitelist add mode on/off").create()
                ));
            } else {
                option.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/istack value add " + p.name()));
                option.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to add a value to this list.").create()
                ));
            }

            cLink.addExtra(option);
            if (!p.getTxtSet().isEmpty()) {
                option = new TextComponent(ChatColor.RED + "R");
                option.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/istack value remove " + p.name()));
                option.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to remove a value from this list.").create()
                ));
                cLink.addExtra(option);
            } else {
                cLink.addExtra(" ");
            }

            option = new TextComponent(ChatColor.GRAY + "L" + ChatColor.GOLD + "] ");
            option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(p.findValue()).create()));
            cLink.addExtra(option);

        } else if (p.getIntValue() >= 0) {

            int val = 0;
            if (p.getConfigValue() instanceof Integer) {
                val = (Integer) p.getConfigValue();
            }

            String pad = "";
            if (val <= 9) {
                pad = "   ";
            } else if (val <= 99) {
                pad = " ";
            }


            TextComponent option = new TextComponent(ChatColor.GRAY + " [" + p.findValue() + pad + "] ");
            option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(p.findValue()).create()));
            option.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.GREEN + "(Click to change)").create()
            ));
            String command = "/istack value set " + p.name();
            option.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));

            cLink.addExtra(option);
        } else if (!p.getTxtValue().isEmpty()) {
            TextComponent option = new TextComponent(ChatColor.GRAY + " [" + ChatColor.GREEN + "TXT" + ChatColor.GRAY + "] ");
            option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(p.findValue()).create()));
            option.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.GRAY + p.findValue() + ChatColor.GREEN + " (Click to change)").create()
            ));
            String command = "/istack value set " + p.name() + " " + p.getConfigValue();
            option.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
            cLink.addExtra(option);
        } else {
            String status = ChatColor.GREEN + " ON";
            if (!p.isEnabled()) {
                status = ChatColor.DARK_RED + "OFF";
            }

            TextComponent option = new TextComponent(ChatColor.GOLD + " [" + status + ChatColor.GOLD + "] ");
            option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to toggle").create()));
            String command = "/istack toggle " + p.name();
            option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + catId));
            cLink.addExtra(option);
        }

        cLink.addExtra(ChatColor.DARK_AQUA + p.getDisplayName());
        if (!p.getDescription().equalsIgnoreCase("")) {

            String desc = p.getDescription();

            cLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc).create()));
        }


        return cLink;
    }

    public static String wordWrap(String text, int length) {
        StringBuilder newString = new StringBuilder();
        if (text.length() <= length) {
            return text;
        }
        String[] words = text.split(" ");

        if (words.length == 0) {
            newString = new StringBuilder(longWord(text, length));
        }

        if (words.length > 1) {

            String temp1 = "";
            for (String word : words) {
                if (temp1.length() > length) {
                    newString.append("\n").append(temp1);
                    temp1 = word;
                } else {
                    if (!temp1.equalsIgnoreCase("")) {
                        temp1 = temp1 + " " + word;
                    } else {
                        temp1 = word;
                    }
                }

            }
            newString.append(" ").append(words[words.length - 1]);

        }

        return newString.toString();

    }

    public static String longWord(String text, int length) {
        String[] words = text.split(" ");
        int counter = 0;
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < words[0].length(); i++) {
            if (counter == length - 2) {
                newString.append(words[0].charAt(i)).append("-\n");
                counter = 0;
            } else {
                newString.append(words[0].charAt(i));
                counter++;
            }
        }
        return newString.toString();
    }

    public static boolean isNPC(LivingEntity ent) {


        return ent.hasMetadata("shopkeeper");
    }

    private static final class FlagFontInfo {

        static int getPxLength(char c) {
            switch (c) {
                case 'i':
                case ':':
                    return 1;
                case 'l':
                    return 2;
                case '*':
                case 't':
                    return 3;
                case 'f':
                case 'k':
                    return 4;
                default:
                    return 5;
            }
        }

        static int getPxLength(String string) {
            return string.chars().reduce(0, (p, i) -> p + getPxLength((char) i) + 1);
        }

    }

}
