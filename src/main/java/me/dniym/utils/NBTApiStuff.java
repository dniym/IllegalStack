package main.java.me.dniym.utils;


import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class NBTApiStuff {

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + NBTApiStuff.class.getSimpleName());

    public static ItemStack checkTimestampLegacy(ItemStack item) {
        //Old backwards compatible version
        NBTItem nbti = new NBTItem(item);

        if (System.currentTimeMillis() >= nbti.getLong("TimeStamp")) {
            nbti.removeKey("TimeStamp");
            nbti.setLong("TimeStamp", System.currentTimeMillis() + 4500L);
        } else {
            //looped item?
            return null;  //return null if the item is looping
        }
        return nbti.getItem();  //return the item with an updated timestamp.
    }

    public static ItemStack addNBTTagLegacy(ItemStack item, String value) {

        NBTItem nbti = new NBTItem(item);
        if (!nbti.hasKey(value)) {
            nbti.setString(value, value);
            return nbti.getItem();
        }
        return null;
    }

    public static ItemStack updateTimeStampLegacy(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        if (!nbti.hasKey("TimeStamp")) {
            nbti.setLong("TimeStamp", (System.currentTimeMillis() + 4500L));
            return nbti.getItem();
        }
        return null;
    }

    public static void getEntityTags(Entity ent) {

        NBTEntity nbtent = new NBTEntity(ent);

        NamespacedKey key = new NamespacedKey(NamespacedKey.MINECRAFT, "gossips");
        PersistentDataContainer data = ent.getPersistentDataContainer();

        NBTCompound tag = nbtent.getCompound("Offers");
        LOGGER.info("offers-> {} - {}", tag.asNBTString(), tag.getType("Recipes"));
        for (NBTListCompound s : nbtent.getCompound("Offers").getCompoundList("Recipes")) {

            if (s.getInteger("specialPrice") < -8) {
                LOGGER.info("Old Price: {}", s.getInteger("specialPrice"));
                s.setInteger("specialPrice", -8);
                LOGGER.info("Updated Price: {}", s.getInteger("specialPrice"));
            }
        }

        //for(NBTListCompound s:nbtent.getCompoundList("Gossips"))
        //for(String s:nbtent.getKeys())
    }

    public static boolean hasNbtTagLegacy(ItemStack item, String tag) {
        NBTItem nbti = new NBTItem(item);
        return nbti.hasKey(tag);
    }

    public static int isBadShulkerLegacy(ItemStack is) {
		/*	
		NBTItem nbti2 = new NBTItem(is);
		for(String key:nbti2.getKeys())
		{
			
			NBTCompoundList nbtList = nbti2.getCompoundList(key);
			
			if(nbti2.getType(key) == NBTType.NBTTagList)
			{
				NBTTagList nestedList = nbti2.getObject(key,  NBTTagList.class);
				if(nestedList != null) {

				} else {
					nbti2.removeKey(key);
				}
			}
		}
		*/

        if (is.getType().name().contains("SHULKER_BOX")) {
            NBTItem nbti = new NBTItem(is);
            NBTCompound tag = nbti.getCompound("BlockEntityTag");
            if (tag == null) {
                return 0;
            }
            NBTCompoundList itemTag = tag.getCompoundList("Items");
            if (itemTag == null) {
                return 0;
            }

            if (itemTag.size() > 27) {
                return itemTag.size();
            }
        }
        return 0;
    }

    public static void hasBadCustomDataOnArmorLegacy(Player p) {
        ItemStack is = p.getInventory().getHelmet();
        String slot = "";
        if (hasBadCustomDataLegacy(p.getInventory().getBoots())) {
            slot = "boots";
            p.getInventory().setBoots(new ItemStack(Material.AIR));
        } else if (hasBadCustomDataLegacy(p.getInventory().getChestplate())) {
            slot = "chestplate";
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
        } else if (hasBadCustomDataLegacy(p.getInventory().getHelmet())) {
            p.getInventory().setHelmet(new ItemStack(Material.AIR));
            slot = "helmet";
        } else if (hasBadCustomDataLegacy(p.getInventory().getLeggings())) {
            p.getInventory().setLeggings(new ItemStack(Material.AIR));
            slot = "leggings";
        }


        if (!slot.isEmpty()) {
            fListener.getLog().append2(Msg.CustomAttribsRemoved2.getValue(p, is, slot));
        }


    }

    public static boolean hasBadCustomDataLegacy(ItemStack is) {
        if (is == null) {
            return false;
        }

        NBTItem nbti = new NBTItem(is);
        NBTCompoundList itemTag = nbti.getCompoundList("AttributeModifiers");

        return (itemTag != null && itemTag.size() > 0);

    }

    public static boolean checkForBadCustomDataLegacy(ItemStack is, Object obj) {
        NBTItem nbti = new NBTItem(is);
        NBTCompoundList itemTag = nbti.getCompoundList("AttributeModifiers");
        if (itemTag == null) {
            return false;
        }

        if (itemTag.size() > 0) {
            itemTag.clear();
            nbti.setObject("AttributeModifiers", itemTag);


            StringBuilder attribs = new StringBuilder();
            attribs.append("Custom Attribute Data");
            fListener.getLog().append(Msg.CustomAttribsRemoved3.getValue(is, obj, attribs), Protections.RemoveCustomAttributes);

            if (obj instanceof Player) {
                ((Player) obj).getInventory().remove(is);
            } else {
                LOGGER.error(
                        "The object type: {} is not accounted for in the legacy NBT Api check.. Please report this to dNiym at the IllegalStack discord or via spigot!",
                        obj.toString()
                );
            }

            return true;

        }
        return false;
    }

    public static void checkForBadCustomDataLegacy(ItemStack is, Player p, boolean sendToPlayer) {
        boolean helmet = false;
        NBTItem nbti = new NBTItem(is);
        NBTCompoundList itemTag = nbti.getCompoundList("AttributeModifiers");
        if (itemTag == null) {
            return;
        }

        if (itemTag.size() > 0) {
            itemTag.clear();
            nbti.setObject("AttributeModifiers", itemTag);

            if (sendToPlayer) {
                p.sendMessage(Msg.CustomAttribsRemoved.getValue(p, is, "Custom Attribute Data"));
            } else {
                fListener.getLog().append2(Msg.CustomAttribsRemoved.getValue(p, is, "Custom Attribute Data"));
            }

            p.getInventory().remove(is);

            // p.getInventory().addItem(nbti.getItem());
        }
    }

    public static void isProCosmeticsLegacy(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        nbti.hasKey("PROCOSMETICS");


    }

}
