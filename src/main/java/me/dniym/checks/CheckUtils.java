package me.dniym.checks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import me.dniym.enums.Msg;
import me.dniym.listeners.fListener;
import me.dniym.utils.NBTStuff;

public class CheckUtils {

	public static boolean CheckEntireContainer(Container c) {
		
		for(ItemStack is:c.getInventory()) {
			
			if(is != null && fListener.getCheckShulker() && is.getType().name().contains("SHULKER_BOX")) 
			{
				
				int tagSize =NBTStuff.isBadShulker(is); 
				if (tagSize > 0) {
                    fListener.getLog().append(Msg.StaffBadShulkerRemoved.getValue(c.getLocation(), tagSize));
                    is.setType(Material.AIR);
                    return true;
                } else {
				
		        if(is.getItemMeta() instanceof BlockStateMeta){
		            BlockStateMeta im = (BlockStateMeta)is.getItemMeta();
		            if(im.getBlockState() instanceof ShulkerBox) 
		            {
		            	BlockState shulk = im.getBlockState();
		                ShulkerBox shulker = (ShulkerBox) shulk;
		                Inventory inv = Bukkit.createInventory(null, shulker.getInventory().getSize(), "IScontainerCheck");
		                for(ItemStack is2:shulker.getInventory().getContents())
		                {
		                	if(is2 == null)
		                		continue;
		                	tagSize =NBTStuff.isBadShulker(is2);
		                	if (tagSize > 0) {
		                        fListener.getLog().append(Msg.StaffBadShulkerRemoved.getValue(c.getLocation(), tagSize));
		                        is2.setType(Material.AIR);
		                	}
		                	
		                	OverstackedItemCheck.CheckContainer(is2, c);
		                	IllegalEnchantCheck.isIllegallyEnchanted(is2, c);
		                	inv.addItem(is2);
		                }
		                if(inv.getContents().length > 0) {
		                	shulker.getInventory().setContents(inv.getContents());
		                	is.setItemMeta((BlockStateMeta)shulk.getData());
		                	
		                } 
		            } 
		            
		        }
                }
			}
			
			
			if(OverstackedItemCheck.CheckContainer(is,c))
				return true;
			
			if(IllegalEnchantCheck.isIllegallyEnchanted(is, c))
				return true;
		}
		return false;
	}
	
	public static boolean CheckEntireInventory(Inventory inv) {
		
		for(ItemStack is:inv.getContents())
			if(OverstackedItemCheck.CheckContainer(is, inv))
				return true;

		for(ItemStack is:inv.getStorageContents())
			if(OverstackedItemCheck.CheckContainer(is, inv))
				return true;
		
		return false;
	}
}
