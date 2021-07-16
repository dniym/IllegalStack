package main.java.me.dniym.utils;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MagicHook {

    private static MagicAPI magicApi = null;

    public static boolean isMagicItem(ItemStack is) {

        if (magicApi == null) {
            getApi();
        }

        return magicApi.isBrush(is) || magicApi.isSpell(is) || magicApi.isUpgrade(is) || magicApi.isWand(is);
    }

    private static void getApi() {

        Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (!(magicPlugin instanceof MagicAPI)) {
            return;
        }
        magicApi = ((MagicAPI) magicPlugin);

    }

}
