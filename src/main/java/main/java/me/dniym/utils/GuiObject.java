/* Example Custom GUI Object
 *
 * I borrowed this code from the spigot wiki there are many advantages to doing this vs using the plain old inventory object from bukkit.
 * https://www.spigotmc.org/wiki/creating-a-gui-inventory/
 *
 * The great thing about this is you can easily identify YOUR gui and handle events for YOUR gui rather than trying to identify it in a regular listener
 * class by name.
 */
package main.java.me.dniym.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiObject implements InventoryHolder, Listener {

    private final Inventory inv;

    public GuiObject() {
        // Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
        inv = Bukkit.createInventory(this, 9, "Example");

        // Put the items into the inventory
        initializeItems();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in or use your existing gui code
    public void initializeItems() {
        inv.addItem(createGuiItem(
                Material.DIAMOND_SWORD,
                "Example Sword",
                "�aFirst line of the lore",
                "�bSecond line of the lore"
        ));
        inv.addItem(createGuiItem(
                Material.IRON_HELMET,
                "�bExample Helmet",
                "�aFirst line of the lore",
                "�bSecond line of the lore"
        ));
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    /*
     * Highly recommend using this method as you can easily identify YOUR custom inventory object by simply doing
     * if (inventory.getHolder() instanceof GuiObject)
     */

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().getHolder() != this) {
            return;
        }
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        final Player p = (Player) e.getWhoClicked();
        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

}
