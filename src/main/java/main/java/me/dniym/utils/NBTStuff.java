package main.java.me.dniym.utils;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class NBTStuff {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + NBTStuff.class.getSimpleName());

    public static ItemStack addNBTTag(ItemStack item, String value, Protections prot) {
        if (hasSpigotNBT()) {

            ItemMeta im = item.getItemMeta();
            PersistentDataContainer data = im.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(IllegalStack.getPlugin(), value);

            data.set(key, PersistentDataType.STRING, value);

            item.setItemMeta(im);

        } else if (IllegalStack.isNbtAPI()) {
            item = NBTApiStuff.addNBTTagLegacy(item, value);
        } else {
            Msg.StaffNoNBTAPI.getValue(prot.name());
        }
        return item;
    }

    public static ItemStack updateTimeStamp(ItemStack item, Protections prot) {
        if (hasSpigotNBT()) {
            //use the new built in methods.

            ItemMeta im = item.getItemMeta();

            PersistentDataContainer data = im.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(IllegalStack.getPlugin(), "timestamp");
            if (data.has(key, PersistentDataType.LONG)) {
                return null;
            }

            data.set(key, PersistentDataType.LONG, (System.currentTimeMillis() + 4500L));
            item.setItemMeta(im);

        } else if (IllegalStack.isNbtAPI()) {
            item = NBTApiStuff.updateTimeStampLegacy(item);
        } else {
            Msg.StaffNoNBTAPI.getValue(prot.name());
        }
        return item;
    }

    public static ItemStack checkTimestamp(ItemStack item, Protections prot) {
        if (hasSpigotNBT()) {
            //use the new built in methods.
            if (item.hasItemMeta()) {
                ItemMeta im = item.getItemMeta();

                PersistentDataContainer data = im.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(IllegalStack.getPlugin(), "timestamp");
                if (data.has(key, PersistentDataType.LONG)) {
                    Long Timestamp = data.get(key, PersistentDataType.LONG);
                    if (System.currentTimeMillis() >= Timestamp) { //timestamp expired reset it
                        data.set(key, PersistentDataType.LONG, System.currentTimeMillis() + 4500L);
                        item.setItemMeta(im);
                    } else {

                        return null;
                    }
                }

            }
        } else if (IllegalStack.isNbtAPI()) {
            item = NBTApiStuff.checkTimestampLegacy(item);
        } else {
            Msg.StaffNoNBTAPI.getValue(prot.name());
        }

        return item;
    }

    public static boolean hasSpigotNBT() {
        return fListener.getInstance().is115() || fListener.getInstance().is114();

    }

    public static boolean isProCosmetics(ItemStack is, Protections prot) {
        if (IllegalStack.getProCosmetics() != null) {
            if (IllegalStack.isNbtAPI()) {
                NBTApiStuff.isProCosmeticsLegacy(is);
            } else {
                Msg.StaffNoNBTAPI.getValue(prot.name());
            }
        }
        return false;
    }

    public static Boolean hasNbtTag(String pluginName, ItemStack item, String tag, Protections prot) {
        if (item == null) {
            return false;
        }

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
        if (plugin == null) {
            return false;
        }

        if (hasSpigotNBT()) {
            if (item.hasItemMeta()) {
                ItemMeta im = item.getItemMeta();

                PersistentDataContainer data = im.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(plugin, tag);
                return data.has(key, PersistentDataType.STRING);
            }
        } else if (IllegalStack.isNbtAPI()) {
            return NBTApiStuff.hasNbtTagLegacy(item, tag);
        } else {
            Msg.StaffNoNBTAPI.getValue(prot.name());
        }
        return false;
    }

    public static int isBadShulker(ItemStack is) {

        if (!IllegalStack.isNbtAPI() && Protections.DestroyInvalidShulkers.isEnabled()) {
            Protections.DestroyInvalidShulkers.setEnabled(false);
            LOGGER.error(
                    "Protection DestroyInvalidShulkers was enabled, however NBTAPI 2.0+ was not loaded on this server, this protection will only work if NBTAPI is present and running.   This protection has automatically been disabled. ");
            return 0;
        }

        if (!Protections.DestroyInvalidShulkers.isEnabled()) {
            return 0;
        }

        return NBTApiStuff.isBadShulkerLegacy(is);


    }

    public static boolean hasBadCustomData(ItemStack is) {

        ItemMeta im = is.getItemMeta();

        if (IllegalStack.isHasAttribAPI() && im.hasAttributeModifiers()) {

            return true;
        } else if (IllegalStack.isNbtAPI()) {
            return NBTApiStuff.hasBadCustomDataLegacy(is);
        }


        return false;
    }

    public static void checkForNegativeDurability(ItemStack is, Player p) {
        if (is == null) {
            return;
        }

        if (IllegalStack.isHasAttribAPI()) {
            if (is.getItemMeta() instanceof Damageable) {
                Damageable dmg = (Damageable) is.getItemMeta();
                if (dmg.getDamage() > is.getType().getMaxDurability()) {
                    fListener.getLog().append(Msg.IllegalStackDurability.getValue(p, is), Protections.FixNegativeDurability);
                    dmg.setDamage(is.getType().getMaxDurability());
                    is.setItemMeta((ItemMeta) dmg);

                }
            }
        }
    }

    public static void checkForBadCustomData(ItemStack is, Player p, boolean sendToPlayer) {

        ItemMeta im = is.getItemMeta();

        if (IllegalStack.isHasAttribAPI() && im.hasAttributeModifiers()) {
            StringBuilder attribs = new StringBuilder();
            HashSet<Attribute> toRemove = new HashSet<>();
            for (Attribute a : im.getAttributeModifiers().keySet()) {
                for (AttributeModifier st : im.getAttributeModifiers(a)) {
                    attribs.append(" ").append(st.getName()).append(" value: ").append(st.getAmount());
                }

                toRemove.add(a);
            }
            if (sendToPlayer) {
                p.sendMessage(Msg.CustomAttribsRemoved.getValue(p, is, attribs.toString()));
            } else {
                fListener.getLog().append(
                        Msg.CustomAttribsRemoved.getValue(p, is, attribs.toString()),
                        Protections.RemoveCustomAttributes
                );
            }
            for (Attribute remove : toRemove) {
                im.removeAttributeModifier(remove);
            }

            for (ItemFlag iFlag : im.getItemFlags()) {
                im.removeItemFlags(iFlag);
            }

            is.setItemMeta(im);

        } else if (IllegalStack.isNbtAPI()) {
            NBTApiStuff.hasBadCustomDataOnArmorLegacy(p);
            NBTApiStuff.checkForBadCustomDataLegacy(is, p, sendToPlayer);

        } else {
            Msg.StaffNoNBTAPI.getValue(Protections.RemoveCustomAttributes.name());
        }

    }

    //should only ever be used on 1.15+ servers no need for legacy
    public static void addNBTTag(Entity entity, String value) {
        if (hasSpigotNBT()) {
            PersistentDataContainer data = entity.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(IllegalStack.getPlugin(), value);

            data.set(key, PersistentDataType.STRING, value);
            entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        }
    }

    //should only ever be used on 1.15+ servers no need for legacy
    public static boolean hasNbtTag(Entity entity, String tag) {
        if (entity == null) {
            return false;
        }

        PersistentDataContainer data = entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(IllegalStack.getPlugin(), tag);
        return data.has(key, PersistentDataType.STRING);

    }

}
