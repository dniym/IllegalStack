package main.java.me.dniym.actions;


import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import main.java.me.dniym.IllegalStack;

import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.NBTApiStuff;

public class Actions {

	public static void handleHeadInBlock(Player p) {
		
    	fListener.getLog().append(Msg.HeadInsideSolidBlock2.getValue(p, p.getEyeLocation().getBlock().getType().name()), Protections.PreventHeadInsideBlock);
    	p.getEyeLocation().getBlock().breakNaturally();
		
	}

	public static void fixNegativeDurability(Player p, ItemStack is) {
		Damageable dmg = (Damageable) is.getItemMeta();
        fListener.getLog().append(Msg.IllegalStackDurability.getValue(p, is), Protections.FixNegativeDurability);
        dmg.setDamage(is.getType().getMaxDurability());
        is.setItemMeta((ItemMeta) dmg);
	}

	public static void removeItemOfType(Player p, ItemStack is) {
		
        if (Protections.RemoveItemTypes.notifyOnly()) {
            fListener.getLog().notify(
                    Protections.RemoveItemTypes,
                    " Triggered by: " + p.getName() + " with item: " + is.getType().name()
            );
        } else {
            fListener.getLog().append(Msg.ItemTypeRemovedPlayer.getValue(p, is), Protections.RemoveItemTypes);
            p.getInventory().remove(is);
        }
	}

	public static void removeRenamedItem(Player p, ItemStack is) {
		fListener.getLog().append(Msg.RemovedRenamedItem.getValue(p, is), Protections.RemoveAllRenamedItems);
        p.getInventory().removeItem(is);
	}
//

	public static void removePlayerItem(Player p, ItemStack is, Msg msg, Protections prot) {
        fListener.getLog().append(msg.getValue(p, is),prot);
        is.setAmount(0);
        is.setType(Material.AIR);
        
	}
	
	public static void handleOverstackedItem(Player p, ItemStack is, boolean isOffhand) {
		
        if (Protections.RemoveOverstackedItems.notifyOnly()) 
            return;
        		
        if (!Protections.IllegalStackMode.isEnabled() && Protections.AllowStack.isWhitelisted(is.getType().name(), null)) { //in blacklist mode and on the blacklist
            if (Protections.FixOverstackedItemInstead.isEnabled())  
            	Actions.fixPlayerItem(p,is,Msg.IllegalStackShorten,Protections.RemoveOverstackedItems);	
            else 
                 Actions.removePlayerItem(p, is, Msg.IllegalStackItemScan, Protections.RemoveOverstackedItems);
        }

        //Checks to do if we're in whitelist mode
        if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
        	return;
        
        if (Protections.FixOverstackedItemInstead.isEnabled()) 
        	Actions.fixPlayerItem(p, is, Msg.IllegalStackShorten,Protections.RemoveOverstackedItems);
        else if (!isOffhand)
        	Actions.removePlayerItem(p, is, Msg.IllegalStackItemScan,Protections.RemoveOverstackedItems);
        else
        	Actions.removeOffhandItem(p,is,Msg.IllegalStackOffhand, Protections.RemoveOverstackedItems);

        


	}

	private static void removeOffhandItem(Player p, ItemStack is, Msg msg,Protections prot) {
		fListener.getLog().append(msg.getValue(p, is),prot);
        p.getInventory().setItemInOffHand(new ItemStack(Material.ROTTEN_FLESH, 1));
	}

	private static void fixPlayerItem(Player p, ItemStack is, Msg msg, Protections prot) {
		fListener.getLog().append(msg.getValue(p, is),prot);
		is.setAmount(is.getType().getMaxStackSize());
	}

	public static void handleIllegallyEnchantedItem(Player p, ItemStack is, HashMap<Enchantment, String> replace) {
		
        for (Enchantment en : replace.keySet()) {
        	fListener.getLog().append(replace.get(en), Protections.FixIllegalEnchantmentLevels);
            is.removeEnchantment(en);
            p.updateInventory();
            if (en.canEnchantItem(is)) {
                new org.bukkit.scheduler.BukkitRunnable() {
                    @Override
                    public void run() {
                        is.addEnchantment(en, Protections.CustomEnchantOverride.getMaxAllowedEnchantLevel(en));
                    }

                }.runTaskLater(IllegalStack.getPlugin(), 1);
            }
        }

		
	}

	public static void handleUnrebakableFlag(Player p, ItemStack is, Msg msg,	Protections prot) {
		
		ItemMeta im = is.getItemMeta();
        if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
                return;
        
        fListener.getLog().append(msg.getValue(p, is),prot);
        im.setUnbreakable(false);
        is.setItemMeta(im);

	}

	public static void handleBadAttributesNBTAPI(Player p, ItemStack is, HashSet<Attribute> toRemove) {
        NBTItem nbti = new NBTItem(is);
        NBTCompoundList itemTag = nbti.getCompoundList("AttributeModifiers");
        if (itemTag == null) 
            return;
        

        if (itemTag.size() > 0) {
            itemTag.clear();
            nbti.setObject("AttributeModifiers", itemTag);

            StringBuilder attribs = new StringBuilder();
            attribs.append("Custom Attribute Data");
            fListener.getLog().append(Msg.CustomAttribsRemoved3.getValue(is, p.getInventory(), attribs), Protections.RemoveCustomAttributes);
            p.getInventory().remove(is);
        }
	    
	}
	
	public static void handleBadAttributes(Player p, ItemStack is, HashSet<Attribute> toRemove) {
		
		ItemMeta itemMeta = is.getItemMeta();
		 StringBuilder attribs = new StringBuilder();
         attribs.append("Custom Attribute Data");
         
		if (IllegalStack.isHasAttribAPI()) {
			fListener.getLog().append(Msg.CustomAttribsRemoved3.getValue(is, p.getInventory(), attribs),Protections.RemoveCustomAttributes);

			for (Attribute remove : toRemove) 
				itemMeta.removeAttributeModifier(remove);
			

			for (ItemFlag iFlag : itemMeta.getItemFlags()) 
				itemMeta.removeItemFlags(iFlag);

			is.setItemMeta(itemMeta);
    
		} else if (IllegalStack.isNbtAPI()) {
			handleBadAttributesNBTAPI(p,is,toRemove);

		} else {
			fListener.getLog().append(Msg.StaffNoNBTAPI.getValue(Protections.RemoveCustomAttributes.name()), Protections.RemoveCustomAttributes);
		}
	}

	public static void handleBadPotion(Player p, ItemStack is, Msg msg, Protections prot) {
		
		PotionMeta potion = ((PotionMeta)is.getItemMeta());
        StringBuilder efx = new StringBuilder();
        for (PotionEffect ce : potion.getCustomEffects()) {
            efx.append(ce.getType().getName()).append(" amplifier: ").append(ce.getAmplifier()).append(
                    " duration: ").append(ce.getDuration()).append(",");
        }

        fListener.getLog().append(msg.getValue(p, efx.toString()), prot);
        p.getInventory().remove(is);
	}
}
