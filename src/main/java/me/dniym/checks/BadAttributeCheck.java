package me.dniym.checks;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
import me.dniym.utils.NBTApiStuff;
import me.dniym.utils.NBTStuff;


public class BadAttributeCheck {

	public static void CheckStorageInventory(CraftingInventory inventory, Player player) {
		
        if (Protections.RemoveCustomAttributes.isEnabled() && IllegalStack.hasStorage()) {

            if (Protections.AllowBypass.isEnabled() && player.hasPermission("illegalstack.enchantbypass"))
                return;
            
            for(ItemStack is:inventory.getStorageContents()) {
            	if(is != null && is.getType() != Material.AIR && NBTStuff.hasBadCustomData(is)) {
            		fListener.getLog().append2(Msg.GenericItemRemoval.getValue(is,Protections.RemoveCustomAttributes,player,"Crafting Inventory"));
            		inventory.removeItem(is);
            	}
            		
            }
        }	
		
	}
	
	public static boolean hasBadAttributes(ItemStack is, Object obj) {
	 	if(!Protections.RemoveCustomAttributes.isEnabled())
	 		return false;
	 	
		if(is != null && is.getType() != Material.AIR && checkForBadCustomData(is,obj)) 
			return true;
		
		return false;
	}
	
	 public static boolean checkForBadCustomData(ItemStack is, Object obj) {


		 	
	        ItemMeta im = is.getItemMeta();
	       
	        if (IllegalStack.isHasAttribAPI()) {
	        	if(im.hasAttributeModifiers()) {
	        		
	        		StringBuilder attribs = new StringBuilder();
	        		HashSet<Attribute> toRemove = new HashSet<Attribute>();
	        		for (Attribute a : im.getAttributeModifiers().keySet()) {

	        			for (AttributeModifier st : im.getAttributeModifiers(a)) {
	        				attribs.append(" ").append(st.getName()).append(" value: ").append(st.getAmount());
	        			}

	        			toRemove.add(a);
	        		}
	            
	            
	        		fListener.getLog().append(Msg.CustomAttribsRemoved3.getValue(is,obj,attribs),Protections.RemoveCustomAttributes);
	            
	        		for (Attribute remove : toRemove)
	        			im.removeAttributeModifier(remove);

	        		for(ItemFlag iFlag:im.getItemFlags())
	        			im.removeItemFlags(iFlag);
	            
	        		is.setItemMeta(im);
	        		return true;
	        	}
	        } else if (IllegalStack.isNbtAPI()) {
	            return NBTApiStuff.checkForBadCustomDataLegacy(is, obj);

	        } else fListener.getLog().append(Msg.StaffNoNBTAPI.getValue(Protections.RemoveCustomAttributes.name()),Protections.RemoveCustomAttributes);
	        return false;
	    }
	 
}
