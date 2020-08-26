package me.dniym.checks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
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
	
	

}
