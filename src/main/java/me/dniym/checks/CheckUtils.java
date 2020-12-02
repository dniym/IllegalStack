package me.dniym.checks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dniym.IllegalStack;
import me.dniym.actions.IllegalStackAction;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.fListener;
import me.dniym.utils.NBTStuff;

public class CheckUtils {

	public static boolean CheckEntireContainer(Container c) {
		boolean added = false;
		for(ItemStack is:c.getInventory()) {

			if(is != null && IllegalStack.hasShulkers() && is.getType().name().contains("SHULKER_BOX")) 
			{

				int tagSize =NBTStuff.isBadShulker(is); 

				if (tagSize > 0 && IllegalStackAction.isCompleted(Protections.DestroyInvalidShulkers,is,c.getInventory())) {
					fListener.getLog().append(Msg.StaffBadShulkerRemoved.getValue(c.getLocation(), tagSize),Protections.DestroyInvalidShulkers);
					is.setType(Material.AIR);
					return true;
				} else {

					if(is.getItemMeta() instanceof BlockStateMeta){
						BlockStateMeta im = (BlockStateMeta)is.getItemMeta();
						Boolean remove = false;
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
								if (tagSize > 0 && IllegalStackAction.isCompleted(Protections.DestroyInvalidShulkers, is2,shulker.getInventory())) {
									fListener.getLog().append(Msg.StaffBadShulkerRemoved.getValue(c.getLocation(), tagSize),Protections.DestroyInvalidShulkers);
									is2.setType(Material.AIR);
									remove = true;
								}


								if(!remove)
									remove = OverstackedItemCheck.CheckContainer(is2, c);
								if(!remove) 
									remove =IllegalEnchantCheck.isIllegallyEnchanted(is2, c);
								if(!remove)
									remove =RemoveItemTypesCheck.shouldRemove(is2, c);

								if(remove) {
									inv.removeItem(is2);
									added = true;
								}
							}
							if(added) {
								new BukkitRunnable() {

									@Override
									public void run() {

										shulker.getInventory().setContents(inv.getContents());
										im.setBlockState(shulker);
										is.setItemMeta(im);

									}

								}.runTaskLater(IllegalStack.getPlugin(), 4);

								return true;
								//shulk.update(true);
								//shulk.setBlockData(shulker.getBlockData());
								//im.setBlockState(shulk);




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

		for(int i = 0; i < inv.getContents().length; i++) {
			ItemStack is = inv.getContents()[i];
			if(is == null)
				continue;
			if(RemoveItemTypesCheck.shouldRemove(is, inv)) 
				return true;
			else if(IllegalEnchantCheck.isIllegallyEnchanted(is, inv))
				return true;
			else if(BadAttributeCheck.hasBadAttributes(is,inv))
				return true;
			else if(OverstackedItemCheck.CheckContainer(is, inv)) 
				return true;
			
			
			
		}

		if(!fListener.is18() && IllegalStack.hasStorage())
			for(ItemStack is:inv.getStorageContents())
				if(OverstackedItemCheck.CheckContainer(is, inv))
					return true;

		return false;
	}
}
