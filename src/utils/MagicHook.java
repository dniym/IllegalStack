package utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.api.magic.MagicAPI;

public class MagicHook {

	private static MagicAPI magicApi = null;
	
	public static boolean isMagicItem(ItemStack is) {
			
		
		if(magicApi == null)
			getApi();
		 
		if(magicApi.isBrush(is) || magicApi.isSpell(is) || magicApi.isUpgrade(is) || magicApi.isWand(is))
			return true;
			
		return false;
	}

	private static void getApi() {
		
		Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (magicPlugin == null || !(magicPlugin instanceof MagicAPI)) {
            return;
        }
        
      magicApi = ((MagicAPI)magicPlugin);
		
	}

}
