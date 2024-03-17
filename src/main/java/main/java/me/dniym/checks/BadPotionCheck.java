package main.java.me.dniym.checks;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.utils.NBTStuff;

public class BadPotionCheck {

	public static void checkPotion(ItemStack is, Player p) {
		if(!is.hasItemMeta())
			return;
		ItemMeta im = is.getItemMeta();
        if (Protections.PreventInvalidPotions.isEnabled() && im instanceof PotionMeta) {
            if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
                return;
            

            if(IllegalStack.isHasMCMMO() && NBTStuff.hasNbtTag("IllegalStack", is, "mcmmoitem", Protections.PreventInvalidPotions)) 
                 return;
              
            
            PotionMeta potion = (PotionMeta) is.getItemMeta();
            PotionData pd = potion.getBasePotionData();
            if (pd.getType() == PotionType.UNCRAFTABLE || (potion.hasCustomEffects() && !potion
                    .getCustomEffects()
                    .isEmpty())) {

                if (pd.getType() == PotionType.UNCRAFTABLE && potion.getCustomEffects().isEmpty()) 
                    return;
                

                p.getInventory().remove(is);
                StringBuilder efx = new StringBuilder();
                for (PotionEffect ce : potion.getCustomEffects()) {
                    efx
                            .append(ce.getType().getName())
                            .append(" amplifier: ")
                            .append(ce.getAmplifier())
                            .append(
                                    " duration: ")
                            .append(ce.getDuration())
                            .append(",");
                }
                fListener.getLog().append(Msg.InvalidPotionRemoved.getValue(p, efx.toString()), Protections.PreventInvalidPotions);
                
            }

        }
    

		
	}

	public static boolean isInvalidPotion(@NotNull Projectile proj) {
		
		if(proj instanceof ThrownPotion) {
			ThrownPotion tp = (ThrownPotion)proj;
			Player p = null;
			if(proj.getShooter() instanceof Player)
				p = ((Player)proj.getShooter());
			
			 if (p != null && Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
	                return false;
	            
			 
			 PotionMeta potion = (PotionMeta) tp.getPotionMeta();
	         PotionData pd = potion.getBasePotionData();
	         if (pd.getType() == PotionType.UNCRAFTABLE || (potion.hasCustomEffects() && !potion
	                    .getCustomEffects()
	                    .isEmpty())) {

	                if (pd.getType() == PotionType.UNCRAFTABLE && potion.getCustomEffects().isEmpty()) 
	                    return false;
	                
	                
	                //p.getInventory().remove(is);
	                StringBuilder efx = new StringBuilder();
	                for (PotionEffect ce : potion.getCustomEffects()) {
	                    efx
	                            .append(ce.getType().getName())
	                            .append(" amplifier: ")
	                            .append(ce.getAmplifier())
	                            .append(
	                                    " duration: ")
	                            .append(ce.getDuration())
	                            .append(",");
	                }
	                
	                fListener.getLog().append(Msg.InvalidThrownPotionRemoved.getValue(p, efx.toString()), Protections.PreventInvalidPotions);
	                return true;
	            }
	         
		}
		return false;
		
	}

	
}
