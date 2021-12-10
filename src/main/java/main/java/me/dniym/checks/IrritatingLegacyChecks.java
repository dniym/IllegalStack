package main.java.me.dniym.checks;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.utils.SlimefunCompat;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class IrritatingLegacyChecks {

    public static boolean isIllegallyEnchanted(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (Protections.FixIllegalEnchantmentLevels.isEnabled() && !itemStack.getEnchantments().isEmpty()) {

            for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                if (itemStack.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel()) {

                    if (SlimefunCompat.isValid(itemStack, enchantment)) {
                        continue;
                    }

                    if (IllegalStack.isEpicRename() && ((enchantment == Enchantment.LURE || enchantment == Enchantment.ARROW_INFINITE) && itemStack
                            .getEnchantmentLevel(
                                    enchantment) == 4341)) {
                        continue;
                    }
                    if (Protections.EnchantedItemWhitelist.isWhitelisted(itemStack)) {
                        break;
                    }

                    if (Protections.CustomEnchantOverride.isAllowedEnchant(
                            enchantment,
                            itemStack.getEnchantmentLevel(enchantment)
                    )) {
                        continue;
                    }
                    return true;
                } else {
                    if (!enchantment.canEnchantItem(itemStack)) {
                        if (SlimefunCompat.isValid(itemStack, enchantment)) {
                            continue;
                        }

                        return true;
                    }
                }
            }

        }

        return false;
    }

    public static boolean CheckItem(ItemStack item, Location location) {
        boolean cancel = false;

        if (item.hasItemMeta() && IllegalStack.hasShulkers()) { //check for shulkers
            final BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
            if (blockStateMeta.getBlockState() instanceof ShulkerBox) {
                final ShulkerBox shulker = (ShulkerBox) blockStateMeta.getBlockState();
                for (ItemStack itemStack : shulker.getInventory().getContents()) {
                    boolean overstacked = false;
                    boolean illegalEnchanted = false;
                    if (Protections.RemoveOverstackedItems.isEnabled()) {
                        overstacked = IrritatingLegacyChecks.CheckContainer(itemStack, location);

                    }
                    if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
                        illegalEnchanted = isIllegallyEnchanted(itemStack);

                    }
                    if (overstacked || illegalEnchanted) {
                        cancel = true;
                        break;
                    }
                }
            }
        } else {  //should be a single item.
            cancel = CheckForOverstackedItems(item);

        }
        return cancel;
    }

    private static boolean CheckForOverstackedItems(ItemStack item) {
        return item.getAmount() > item.getType().getMaxStackSize();

    }

    public static boolean CheckContainer(ItemStack itemStack, Location loc) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getAmount() > itemStack.getMaxStackSize()) {
            return !Protections.AllowStack.isWhitelisted(itemStack.getType().name(), null);

        }

        return false;
    }

    public static boolean CheckContainer(Block block) {
        BlockState blockState = block.getState();
        boolean invalid = false;

        if (blockState instanceof InventoryHolder) {
            for (ItemStack itemStack : ((InventoryHolder) blockState).getInventory()) {
                if (itemStack != null && CheckItem(itemStack, block.getLocation())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ((InventoryHolder) blockState).getInventory().removeItem(itemStack);
                        }

                    }.runTaskLater(IllegalStack.getPlugin(), 2);
                    invalid = true;
                }
            }
        }


        return invalid;
    }

}
