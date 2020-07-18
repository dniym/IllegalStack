package me.dniym.checks;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;

import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

	public static boolean CheckForOverstackedItems (ItemStack is, Object obj) {
		
		if(is != null && (obj instanceof Inventory || obj instanceof Container))
			return CheckContainer(is,obj);
		
		return false;
	}
	
	public static boolean CheckStorageInventory(Inventory inv,Player plr) {
		boolean invalid = false;
		if(!IllegalStack.hasStorage())
			return false;
		
		for(ItemStack is:inv.getStorageContents())
			if(CheckContainer(is,inv,true)) 
				fListener.getLog().append(Msg.GenericItemRemoval.getValue(is,Protections.RemoveOverstackedItems, plr, "Crafting Inventory"));
			
		
		return invalid;
	}
	
	public static boolean CheckContainer(ItemStack is, Object obj) {
		if(CheckContainer(is,obj,false)) 
			return true;

		return false;
	}
    public static boolean CheckContainer(ItemStack is, Object obj, Boolean silent) {
    	
        if (is == null)
            return false;

        
        if (is.getAmount() > is.getMaxStackSize()) {

            if (!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
            {
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                	if(!silent) fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                    is.setAmount(is.getType().getMaxStackSize());
                    return true;
                } else {
                	if(!silent) fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
                	if(obj instanceof Inventory)
                		((Inventory)obj).remove(is);
                	else
                		((Container)obj).getInventory().remove(is);
                    return true;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
                return false;

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
            	if(!silent) fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                	is.setAmount(is.getType().getMaxStackSize());
                
                return true;
            } else {
            	if(!silent) fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
            	if(obj instanceof Inventory)
            		((Inventory)obj).remove(is);
            	else
            		((Container)obj).getInventory().remove(is);
                return true;
            }
        }

        return false;
    }
    
}
