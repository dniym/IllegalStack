package main.java.me.dniym.checks;


import main.java.me.dniym.actions.IllegalStackAction;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.NBTStuff;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.dniym.IllegalStack;

public class CheckUtils {

    public static boolean CheckEntireContainer(Container container) {
        boolean added = false;
        for (ItemStack itemStack : container.getInventory()) {
            if (itemStack != null && IllegalStack.hasShulkers() && itemStack.getType().name().contains("SHULKER_BOX")) {
                int tagSize = NBTStuff.isBadShulker(itemStack);
                if (tagSize > 0 && IllegalStackAction.isCompleted(
                        Protections.DestroyInvalidShulkers,
                        itemStack,
                        container.getInventory()
                )) {
                    fListener.getLog().append(
                            Msg.StaffBadShulkerRemoved.getValue(container.getLocation(), tagSize),
                            Protections.DestroyInvalidShulkers
                    );
                    itemStack.setType(Material.AIR);
                    return true;
                } else {
                    if (itemStack.getItemMeta() instanceof BlockStateMeta) {
                        BlockStateMeta im = (BlockStateMeta) itemStack.getItemMeta();
                        boolean remove = false;
                        if (im.getBlockState() instanceof ShulkerBox) {
                            BlockState shulk = im.getBlockState();
                            ShulkerBox shulker = (ShulkerBox) shulk;
                            Inventory inv = Bukkit.createInventory(null, shulker.getInventory().getSize(), "IScontainerCheck");
                            for (ItemStack itemStack1 : shulker.getInventory().getContents()) {
                                if (itemStack1 == null) {
                                    continue;
                                }
                                tagSize = NBTStuff.isBadShulker(itemStack1);
                                if (tagSize > 0 && IllegalStackAction.isCompleted(
                                        Protections.DestroyInvalidShulkers,
                                        itemStack1,
                                        shulker.getInventory()
                                )) {
                                    fListener.getLog().append(
                                            Msg.StaffBadShulkerRemoved.getValue(container.getLocation(), tagSize),
                                            Protections.DestroyInvalidShulkers
                                    );
                                    itemStack1.setType(Material.AIR);
                                    remove = true;
                                }

                                if (!remove) {
                                    remove = OverstackedItemCheck.CheckContainer(itemStack1, container);
                                }
                                if (!remove) {
                                    remove = IllegalEnchantCheck.isIllegallyEnchanted(itemStack1, container);
                                }
                                if (!remove) {
                                    remove = RemoveItemTypesCheck.shouldRemove(itemStack1, container);
                                }

                                if (remove) {
                                    inv.removeItem(itemStack1);
                                    added = true;
                                }
                            }
                            if (added) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        shulker.getInventory().setContents(inv.getContents());
                                        im.setBlockState(shulker);
                                        itemStack.setItemMeta(im);
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

            if (OverstackedItemCheck.CheckContainer(itemStack, container)) {
                return true;
            }

            if (IllegalEnchantCheck.isIllegallyEnchanted(itemStack, container)) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckEntireInventory(Inventory inv) {

        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack is = inv.getContents()[i];
            if (is == null) {
                continue;
            }
            if (RemoveItemTypesCheck.shouldRemove(is, inv)) {
                return true;
            } else if (IllegalEnchantCheck.isIllegallyEnchanted(is, inv)) {
                return true;
            } else if (BadAttributeCheck.hasBadAttributes(is, inv)) {
                return true;
            } else if (OverstackedItemCheck.CheckContainer(is, inv)) {
                return true;
            }

        }

        if (!fListener.is18() && IllegalStack.hasStorage()) {
            for (ItemStack is : inv.getStorageContents()) {
                if (OverstackedItemCheck.CheckContainer(is, inv)) {
                    return true;
                }
            }
        }

        return false;
    }

}
