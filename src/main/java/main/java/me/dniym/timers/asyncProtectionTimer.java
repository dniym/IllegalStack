package main.java.me.dniym.timers;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.netty.util.internal.ThreadLocalRandom;
import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.actions.Actions;
import main.java.me.dniym.checks.BadAttributeCheck;
import main.java.me.dniym.checks.IllegalEnchantCheck;
import main.java.me.dniym.checks.OverstackedItemCheck;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.events.playerAboveNetherEvent;

import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.listeners.mcMMOListener;
import main.java.me.dniym.utils.NBTStuff;
import main.java.me.dniym.utils.SlimefunCompat;
import main.java.me.dniym.utils.SpigotMethods;

public class asyncProtectionTimer implements Runnable {

	private static final HashMap<FallingBlock, Long> fbTracker = new HashMap<>();
    private static final HashMap<Projectile, Long> projTracker = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + asyncProtectionTimer.class.getSimpleName());
    private static long endScanFinish = 0L;
    private static World dragon = null;
    private static HashMap<Player, Entity> punish = new HashMap<>();
    private final long scanDelay = 1;
    private final IllegalStack plugin;
    private final boolean is1_8;
    private long nextScan;
    private long longScan;
    private long endScan = 0L;
    private long nextNetherDamage = 0L;
	public asyncProtectionTimer(IllegalStack illegalStack) {
        this.plugin = illegalStack;
        this.nextScan = System.currentTimeMillis() + (scanDelay * 6000);
        
        if(Protections.DamagePlayersAboveNether.isEnabled()) {
        	if(Protections.AboveNetherDamageAmount.getIntValue() < 1)
        		Protections.AboveNetherDamageAmount.setIntValue(1);
        	if(Protections.AboveNetherDamageDelay.getIntValue() < 1)
        		Protections.AboveNetherDamageDelay.setIntValue(1);
        	
        	this.nextNetherDamage = System.currentTimeMillis() + (Protections.AboveNetherDamageDelay.getIntValue() * 1000);
        }
        


        String version = IllegalStack.getPlugin().getServer().getClass().getPackage().getName().replace(".", ",")
                .split(",")[3];
        is1_8 = version.equalsIgnoreCase("v1_8_R3") || version.contains("v1_8");

        if (is1_8) {
            LOGGER.info("Minecraft 1.8 detected not checking offhand slot for overstacked items.");
        }
        if (is1_8 || version.equalsIgnoreCase("v1_9_R4") || version.equalsIgnoreCase("v1_10_R2")) {
            LOGGER.info("Version < 1.11 found, not checking for shulker boxes");
        }
        LOGGER.info("Starting asyncProtectionTimer - " + version.toString());
        
        this.longScan = System.currentTimeMillis() + 10000L;

	}

	@Override
	public void run() {
		
		//handle player punishments.
        for (Player p : punish.keySet()) {
            fListener.punishPlayer(p, punish.get(p));
        }
        punish.clear();

        
        if (Protections.DamagePlayersAboveNether.isEnabled() && System.currentTimeMillis() >= nextNetherDamage) {
        	 nextNetherDamage = System.currentTimeMillis() + (Protections.AboveNetherDamageDelay.getIntValue() * 1000);
        	for(World w:plugin.getServer().getWorlds())
        		if(w.getName().toLowerCase().contains("nether") || w.getEnvironment() == Environment.NETHER)
        			for(Player p:w.getPlayers())
        				if(p.isOp() || p.hasPermission("illegalstack.notify"))
        					continue;
        				else if (p.getLocation().getY() >= Protections.NetherYLevel.getIntValue())
        					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getPluginManager().callEvent(new playerAboveNetherEvent(p)), 2);
        }
        

            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (SpigotMethods.isNPC(p)) 
                    continue;

                if (Protections.PreventHeadInsideBlock.isEnabled() && p.getGameMode() == GameMode.SURVIVAL) {
                    Material type = p.getEyeLocation().getBlock().getType();
                    if (Protections.AlsoPreventHeadInside.isWhitelisted(type) || type.isSolid()) 
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleHeadInBlock(p), 1);
                    
                    	
                }
                      
            
                for (ItemStack is : p.getInventory().getContents()) {
                	if(is == null)
                		continue;
                	
                	if (Protections.FixNegativeDurability.isEnabled() && NBTStuff.hasNegativeDurability(is,p)) 
                      if(NBTStuff.hasNegativeDurability(is, p))
                    	  Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.fixNegativeDurability(p,is), 1);

                    if (is != null && !p.isOp()) 
                        if (Protections.RemoveItemTypes.isWhitelisted(is)) 
                        	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeItemOfType(p,is), 1);
                        	
                    if (Protections.RemoveAllRenamedItems.isEnabled()) 
                        if (!p.hasPermission("IllegalStack.RenameBypass")) 
                            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) 
                            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeRenamedItem(p,is), 1);
                                
                    if (Protections.RemoveItemsMatchingName.isEnabled() && (!Protections.BlockEnchantingInstead.isEnabled() && !Protections.BlockRepairsInstead
                            .isEnabled())) {
                        if (is != null && is.hasItemMeta()) 
                        {
                            ItemMeta im = is.getItemMeta();
                            for (String ignored : Protections.ItemNamesToRemove.getTxtSet()) 
                                if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) 
                                {
                                    if (!Protections.RemoveItemsMatchingName.notifyOnly()) 
                                    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removePlayerItem(p,is,Msg.NamedItemRemovalPlayer, Protections.RemoveItemsMatchingName), 1);
                                    break;
                                }
                            
                        }
                    }

                    if (is.hasItemMeta()) {  //checks that depend on item meta
                        ItemMeta im = is.getItemMeta();

                        if (Protections.RemoveUnbreakableFlag.isEnabled() && IllegalStack.hasUnbreakable()) 
                            if (im.isUnbreakable()) 
                            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleUnrebakableFlag(p,is,Msg.UnbreakableItemCleared, Protections.RemoveUnbreakableFlag), 1);

                        if (Protections.RemoveCustomAttributes.isEnabled()) {
                        	HashSet<Attribute> badAttributes = BadAttributeCheck.getBadAttributes(is, p);
                        	if(!badAttributes.isEmpty())
                        		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleBadAttributes(p,is,badAttributes), 1);
                        }
                        
                        if (Protections.PreventInvalidPotions.isEnabled() && im instanceof PotionMeta) 
                        	if(!isIllegalPotion(p, is, ((PotionMeta)im)))
                        		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleBadPotion(p,is,Msg.InvalidPotionRemoved, Protections.PreventInvalidPotions), 1);                        		
                    
                    }
                        
                                       
                    
                    if(OverstackedItemCheck.isOverstacked(is))
                    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleOverstackedItem(p, is,false),1);
                    
                    HashMap<Enchantment,String> illegalEnchants = IllegalEnchantCheck.checkEnchants(is,p);
                    if(!illegalEnchants.isEmpty()) {
                    	if(Protections.FixIllegalEnchantmentLevels.isEnabled()) 
                    		if(Protections.DestroyIllegallyEnchantedItemsInstead.isEnabled())
                   				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removePlayerItem(p,is,Msg.DestroyedEnchantedItem,Protections.DestroyIllegallyEnchantedItemsInstead), 1);
                    		else
                    			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleIllegallyEnchantedItem(p,is,illegalEnchants), 1);
                    }
                       
                }  //end of normal inventory loop
                
                    //check offhand items needs to check enchants and all that crap
                
                	checkOffhandItems(p);
                    //end offhand item checking

                	//check worn items and handle them
                    for (ItemStack is : p.getInventory().getArmorContents()) {
                    	if(is == null)
                    		continue;
                    	
                    	if (Protections.FixNegativeDurability.isEnabled() && NBTStuff.hasNegativeDurability(is,p)) 
                          if(NBTStuff.hasNegativeDurability(is, p))
                        	  Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.fixNegativeDurability(p,is), 1);

                        if (!p.isOp()) 
                            if (Protections.RemoveItemTypes.isWhitelisted(is)) 
                            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeItemOfType(p,is), 1);
                            	
                        if (Protections.RemoveAllRenamedItems.isEnabled()) 
                            if (!p.hasPermission("IllegalStack.RenameBypass")) 
                                if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) 
                                	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeRenamedItem(p,is), 1);
                                    
                        if (Protections.RemoveItemsMatchingName.isEnabled() && (!Protections.BlockEnchantingInstead.isEnabled() && !Protections.BlockRepairsInstead
                                .isEnabled())) {
                            if (is != null && is.hasItemMeta()) 
                            {
                                ItemMeta im = is.getItemMeta();
                                for (String ignored : Protections.ItemNamesToRemove.getTxtSet()) 
                                    if (Protections.RemoveItemsMatchingName.loreNameMatch(im)) 
                                    {
                                        if (!Protections.RemoveItemsMatchingName.notifyOnly()) 
                                        	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removePlayerItem(p,is,Msg.NamedItemRemovalPlayer, Protections.RemoveItemsMatchingName), 1);
                                        break;
                                    }
                                
                            }
                        }
                        
                        
                        if(OverstackedItemCheck.isOverstacked(is))
                        	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleOverstackedItem(p, is,false),1);
                        
                        HashMap<Enchantment,String> illegalEnchants = IllegalEnchantCheck.checkEnchants(is,p);
                        if(!illegalEnchants.isEmpty()) {
                        	if(Protections.FixIllegalEnchantmentLevels.isEnabled()) 
                        		if(Protections.DestroyIllegallyEnchantedItemsInstead.isEnabled())
                       				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removePlayerItem(p,is,Msg.DestroyedEnchantedItem,Protections.DestroyIllegallyEnchantedItemsInstead), 1);
                        		else
                        			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleIllegallyEnchantedItem(p,is,illegalEnchants), 1);
                        }

                    }
                        
            }


	}

	
    private boolean isIllegalPotion(Player p, ItemStack is, PotionMeta potionMeta) {

        if (Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
            return false;
        

        if (IllegalStack.isHasMCMMO()) {
            if (NBTStuff.hasNbtTag("IllegalStack", is, "mcmmoitem", Protections.PreventInvalidPotions)) {
                return false;
            }
        }
        
        PotionMeta potion = (PotionMeta) is.getItemMeta();
        PotionData pd = potion.getBasePotionData();
        if (pd.getType() == PotionType.UNCRAFTABLE || (potion.hasCustomEffects() && !potion
                .getCustomEffects()
                .isEmpty())) {

            if (pd.getType() == PotionType.UNCRAFTABLE && potion.getCustomEffects().isEmpty()) 
                return false;
            
            return true;
        }

		return false;
	}

	private void checkOffhandItems(Player p) {
    	
        if (is1_8 || Protections.DisableInWorlds.getTxtSet().contains(p.getWorld().getName()))  //no offhand items in 1.8 
        	return;
        
            
        ItemStack is = p.getInventory().getItemInOffHand();
        if(is == null)
        	return;
        
    	if (Protections.FixNegativeDurability.isEnabled() && NBTStuff.hasNegativeDurability(is, p)) 
    		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.fixNegativeDurability(p,is), 1);
    
    	//overstacked item check
    	if(OverstackedItemCheck.isOverstacked(is))
    		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.handleOverstackedItem(p,is, true), 1);
    	
        if (!p.isOp()) 
            if (Protections.RemoveItemTypes.isWhitelisted(is)) 
            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeItemOfType(p,is), 1);
            	
        if (Protections.RemoveAllRenamedItems.isEnabled()) 
            if (!p.hasPermission("IllegalStack.RenameBypass")) 
                if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) 
                	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Actions.removeRenamedItem(p,is), 1);

		
	}


	public static void trackBlock(FallingBlock fb) {
        fbTracker.put(fb, System.currentTimeMillis() + 4000L);
    }

    public static void trackProjectile(Projectile proj) {
        projTracker.put(proj, System.currentTimeMillis() + 14000L);
    }

    public static long getEndScanFinish() {
        return endScanFinish;
    }

    public static void setEndScanFinish(long endScan) {
        endScanFinish = endScan;
    }

    public static HashMap<Player, Entity> getPunish() {
        return punish;
    }

    public static void setPunish(HashMap<Player, Entity> toPunish) {
        punish = toPunish;
    }
    
    public World getDragon() {
        return dragon;
    }

    public static void setDragon(World world) {
        dragon = world;
    }

}
