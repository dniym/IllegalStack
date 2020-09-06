package me.dniym.checks;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;

public class RemoveItemTypesCheck {

	public static boolean CheckForIllegalTypes(ItemStack is, Object obj) {

		if(is != null && (obj instanceof Inventory || (IllegalStack.hasContainers() && obj instanceof Container)))
			if(shouldRemove(is,obj))
			{
				if(obj instanceof Inventory)
					((Inventory)obj).remove(is);
				else
					((Container)obj).getInventory().remove(is);
			}

		return false;

	}

	public static boolean shouldRemove(Material type, Player player) {
		return Protections.RemoveItemTypes.isWhitelisted(type); 
			
		
	}
	
	public static boolean shouldRemove(ItemStack is, Object obj) {


		if (Protections.RemoveItemTypes.isWhitelisted(is)) {
			
			if (obj != null && Protections.RemoveItemTypes.notifyOnly()) {
				fListener.getLog().notify(Protections.RemoveItemTypes, " Triggered by: a contianer with item: " + is.getType().name());
				return false;
			} else if (obj != null) 
				fListener.getLog().append2(Msg.ItemTypeRemoved.getValue(obj,is,null));
				return true;
			
		}
		return false;
	}
}
