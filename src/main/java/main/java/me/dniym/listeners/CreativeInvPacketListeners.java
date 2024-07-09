package main.java.me.dniym.listeners;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.utils.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreativeInvPacketListeners extends PacketListenerAbstract {

    private final IllegalStack plugin;

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + CreativeInvPacketListeners.class.getSimpleName());

    public CreativeInvPacketListeners(IllegalStack plugin) {
        super(PacketListenerPriority.LOW);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() != PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {
            return;
        }

        if (!Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
            return;
        }

        User user = event.getUser();
        UUID uuid = user.getUUID();
        Player player = plugin.getServer().getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (player.isOp() || player.hasPermission("illegalstack.admin")) {
            return;
        }

        WrapperPlayClientCreativeInventoryAction packetWrapper = new WrapperPlayClientCreativeInventoryAction(event);

        try {
            com.github.retrooper.packetevents.protocol.item.ItemStack wrapperItemStack = packetWrapper.getItemStack();
            ItemStack stack = SpigotConversionUtil.toBukkitItemStack(wrapperItemStack);
            if (stack != null && stack.hasItemMeta()) {
                stack = new ItemStack(Material.AIR);
                Scheduler.runTaskLater(plugin, player::updateInventory, 5L, player);
                event.setCancelled(true);
                Msg.StaffMsgCreativeBlock.getValue(player.getName());
            }
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.error(
                    "An error receiving a CREATIVE_INVENTORY_ACTION packet has occurred, you are probably using paper and have BlockBadItemsFromCreativeTab turned on.   This setting is needed very rarely, and ONLY if you have regular non-op players with access to /gmc.");
        }
    }

}
