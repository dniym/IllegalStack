package me.dniym.checks;

import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;

import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

	public static boolean CheckForOverstackedItems (ItemStack is, Object obj) {
		
		if(is != null && (obj instanceof Container || obj instanceof Inventory))
			return CheckContainer(is,obj);
		
		return false;
	}
    public static boolean CheckContainer(ItemStack is, Object obj) {
    	
    	if (!(obj instanceof Inventory) && !(obj instanceof Container))
    		return false;
    	
        if (is == null)
            return false;
        
        
        if (is.getAmount() > is.getMaxStackSize()) {

            if (!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
            {
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                    is.setAmount(is.getType().getMaxStackSize());
                    fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                    return true;
                } else {
                	if(obj instanceof Container)
                		((Container)obj).getInventory().remove(is);
                	else
                		((Inventory)obj).remove(is);
                    fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
                    return true;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
                return true;

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                is.setAmount(is.getType().getMaxStackSize());
                fListener.getLog().append(Msg.IllegalStackShorten.getValue(obj, is));
                return true;
            } else {
            	if(obj instanceof Container)
            		((Container)obj).getInventory().remove(is);
            	else
            		((Inventory)obj).remove(is);
            	
                fListener.getLog().append(Msg.IllegalStackItemScan.getValue(obj, is));
                return true;
            }
        }

        return false;
    }
    
}
