package me.dniym.checks;




import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dniym.IllegalStack;
import me.dniym.enums.Protections;
import me.dniym.utils.SlimefunCompat;

public class IrritaingLegacyChecks {

	public static boolean isIllegallyEnchanted(ItemStack is) {
    	
        if (is == null)
            return false;

        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !is.getEnchantments().isEmpty()) 
        {
            
            HashSet<Enchantment> replace = new HashSet<>();
            for (Enchantment en : is.getEnchantments().keySet())
                if (is.getEnchantmentLevel(en) > en.getMaxLevel()) {

                    if (SlimefunCompat.isValid(is, en))
                        continue;

                    if (IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(en) == 4341))
                        continue;
                    if (Protections.EnchantedItemWhitelist.isWhitelisted(is))
                        break;

                    if (Protections.CustomEnchantOverride.isAllowedEnchant(en, is.getEnchantmentLevel(en)))
                        continue;
                    return true;
                } else {
                    if (!en.canEnchantItem(is)) {
                        if (SlimefunCompat.isValid(is, en))
                            continue;

                        return true;
                    }
                }
            
        }

        return false;
    }
	public static boolean CheckItem(ItemStack item, Location loc) {
		boolean cancel = false;

		if(item.hasItemMeta() && IllegalStack.hasShulkers()) { //check for shulkers
			final BlockStateMeta sbm = (BlockStateMeta)item.getItemMeta();
			if (sbm.getBlockState() instanceof ShulkerBox) 
			{
				final ShulkerBox shulker = (ShulkerBox)sbm.getBlockState();
				for(ItemStack is:shulker.getInventory().getContents()) 
				{
					boolean overstacked = false;
					boolean illegalEnchanted = false;
					if (Protections.RemoveOverstackedItems.isEnabled()) {
						overstacked = IrritaingLegacyChecks.CheckContainer(is, sbm, loc);

					}
					if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
						illegalEnchanted = isIllegallyEnchanted(is);

					}
					if(overstacked || illegalEnchanted)
					{
						cancel = true;
						break;
					}
				}
			}
		} else {  //should be a single item.
			cancel =  CheckForOverstackedItems(item);
			
		}
		return cancel;
	}
	private static boolean CheckForOverstackedItems(ItemStack item) {
		return item.getAmount() > item.getType().getMaxStackSize();
		
	}
	public static boolean CheckContainer(ItemStack is, BlockStateMeta sb, Location loc) {


		if (is == null)
			return false;

		if (is.getAmount() > is.getMaxStackSize()) 
		{
			if (Protections.AllowStack.isWhitelisted(is.getType().name(), null))
				return false;

			return true;

		}

		return false;
	}
	public static boolean CheckContainer(Block block) {
		BlockState bs = block.getState();
		
		Boolean invalid = false;
		
		if(bs instanceof InventoryHolder)
			for(ItemStack is:((InventoryHolder)bs).getInventory())
				if(is != null && CheckItem(is,block.getLocation())) {
					
					  new BukkitRunnable() {
					        
				            @Override
				            public void run() {
				                ((InventoryHolder)bs).getInventory().removeItem(is);
				            }
				            
				        }.runTaskLater(IllegalStack.getPlugin(), 2);
					invalid = true;
				}
		
				
		return invalid;
	}

}
