package me.dniym.checks;

import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

	public static boolean CheckForOverstackedItems (ItemStack is, Object obj) {
		
		if(is != null && (obj instanceof Inventory || obj instanceof Container))
			return CheckContainer(is,obj);
		
		return false;
	}
	
    public static boolean CheckContainer(ItemStack is, Object obj) {
    	
        if (is == null)
            return false;
        
        
        if (is.getAmount() > is.getMaxStackSize()) {

            if (!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
            {
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                	fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                    is.setAmount(is.getType().getMaxStackSize());
                    return true;
                } else {
                	fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
                	if(obj instanceof Inventory)
                		((Inventory)obj).remove(is);
                	else
                		((Container)obj).getInventory().remove(is);
                    return true;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
                return true;

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
            	fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                is.setAmount(is.getType().getMaxStackSize());
                
                return true;
            } else {
            	fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
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
