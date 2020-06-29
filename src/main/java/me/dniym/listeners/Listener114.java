package me.dniym.listeners;

import io.netty.util.internal.ThreadLocalRandom;
import me.dniym.IllegalStack;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.timers.fTimer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class Listener114 implements Listener {

    private static final BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
    HashMap<UUID, Long> lastTrade = new HashMap<>();
    HashSet<Material> consumables = new HashSet<>();

    public Listener114() {
        Material[] consume = new Material[]{Material.APPLE, Material.BAKED_POTATO, Material.BEETROOT, Material.BEETROOT_SOUP, Material.BREAD, Material.CARROT,
                Material.CHORUS_FRUIT, Material.COOKED_CHICKEN, Material.COOKED_COD, Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_SALMON,
                Material.COOKED_RABBIT, Material.COOKIE, Material.DRIED_KELP, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE, Material.GOLDEN_CARROT,
                Material.MELON_SLICE, Material.MUSHROOM_STEW, Material.POISONOUS_POTATO, Material.POTATO, Material.PUFFERFISH, Material.PUMPKIN_PIE, Material.RABBIT_STEW,
                Material.BEEF, Material.CHICKEN, Material.COD, Material.MUTTON, Material.PORKCHOP, Material.RABBIT, Material.SALMON, Material.ROTTEN_FLESH,
                Material.SPIDER_EYE, Material.COOKED_BEEF, Material.SUSPICIOUS_STEW, Material.SWEET_BERRIES, Material.TROPICAL_FISH, Material.MILK_BUCKET,
                Material.POTION};
        consumables.addAll(Arrays.asList(consume));
    }
    
    @EventHandler
    public void onChestDispense(BlockDispenseArmorEvent e) {
    	//System.out.println("Checking dispense??");
    	if (Protections.DisableChestsOnMobs.isEnabled()) {
    		
    		if(e.getItem().getType() == Material.CHEST) {
    		//	System.out.println("Prevented a dispenser from spitting out a chest");
    			e.setCancelled(true);
    		}
    	}
    }

    /*
    @EventHandler
    public void onVillagerInteract(PlayerInteractAtEntityEvent e)
    {

        if(e.getRightClicked() instanceof Villager) {

            if(NBTStuff.hasNbtTag(e.getRightClicked(), "iStackVillager")) {
                System.out.println("Villager already patched");
                return;
            }

                Location location = e.getRightClicked().getLocation();
                iStackVillager iStackVillager = new iStackVillager(location.getWorld());
                iStackVillager.setPosition(location.getX(), location.getY(), location.getZ());
                ((CraftWorld)location.getWorld()).getHandle().addEntity(iStackVillager, CreatureSpawnEvent.SpawnReason.CUSTOM);
                NBTStuff.copyNbt(e.getRightClicked(), iStackVillager);
                e.getRightClicked().remove();
                System.out.println("Patching villager");
        }
    }
    */

    @EventHandler
    public void OnVillagerTransform(EntityTransformEvent e) {

        if (Protections.ZombieVillagerTransformChance.getIntValue() < 100) {
            for (Entity ent : e.getTransformedEntities()) {

                if (ent instanceof ZombieVillager) {
                    if (Protections.ZombieVillagerTransformChance.getIntValue() <= 0) {
                        e.setCancelled(true);
                        return;
                    }

                    int roll = ThreadLocalRandom.current().nextInt(1, 100);

                    if (roll > Protections.ZombieVillagerTransformChance.getIntValue()) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void OnVillagerInteract(PlayerInteractEntityEvent e) {
        if (Protections.DisableInWorlds.getTxtSet().contains(e.getPlayer().getWorld().getName()))
            return;
        if (Protections.PreventVillagerSwimExploit.isEnabled()) {
            if (e.getRightClicked() != null && e.getRightClicked() instanceof Villager && e.getRightClicked() instanceof Merchant) {
                boolean cancel = false;

                Villager v = (Villager) e.getRightClicked();

                if (v.getLocation().getBlock().getType() == Material.WATER) {
                    cancel = true;
                }
                Block down = v.getLocation().getBlock();
                if (down.getBlockData() instanceof Waterlogged) {
                    Waterlogged wl = (Waterlogged) down.getBlockData();
                    if (wl.isWaterlogged()) {
                        cancel = true;
                    }
                }
                if (cancel) {
                    e.setCancelled(cancel);
                    e.getPlayer().sendMessage(Msg.PlayerSwimExploitMsg.getValue());

                }
            }
        }

    }
    /*
        @EventHandler
        public void onVillagerSpawn(EntitySpawnEvent e) {
            if(e.getEntity() instanceof NMSEntityVillager)
            {
                System.out.println("Spawned in custom villager");
                return;
            }
            if(e.getEntity() instanceof CraftVillager )
            {

                e.getEntity().remove();
                System.out.println("Replacing vanilla villager");
                NMSGarbage.spawnVillager(e.getLocation());

            }
        }
        */

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {


        if (Protections.SilkTouchBookExploit.isEnabled()) {
            Player p = e.getPlayer();
            if (p != null && p.getInventory().getItemInMainHand() != null) {
                ItemStack is = p.getInventory().getItemInMainHand();
                if (is.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta bm = (EnchantmentStorageMeta) is.getItemMeta();
                    for (Enchantment en : bm.getStoredEnchants().keySet())
                        if (en.equals(Enchantment.SILK_TOUCH)) {
                            if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.TRAPPED_CHEST)
                                return;

                            e.setCancelled(true);
                            fListener.getLog().append(Msg.SilkTouchBookBlocked.getValue(p, e.getBlock().getType().name()));

                        }
                }
            }
        }
    }

    @EventHandler
    public void LockMerchantTrades(InventoryOpenEvent e) {


        if (Protections.DisableInWorlds.getTxtSet().contains(e.getPlayer().getWorld().getName()))
            return;
        if (Protections.PreventCommandsInBed.isEnabled() && e.getPlayer().isSleeping()) {
            e.setCancelled(true);
            return;
        }
        if (!Protections.VillagerTradeCheesing.isEnabled())
            return;
        if (e.getInventory() instanceof MerchantInventory) {
            MerchantInventory mi = (MerchantInventory) e.getInventory();
            if (mi.getHolder() instanceof WanderingTrader)
                return;
            if (mi.getHolder() instanceof Villager) {
                Villager v = (Villager) mi.getHolder();

                if (v.getVillagerExperience() == 0)
                    v.setVillagerExperience(1);

                //	System.out.println("Verifying trades?");
                //NBTStuff.verifyTrades(v);

            }
        }
    }

    @EventHandler
    public void PreventVillagerRestock(VillagerReplenishTradeEvent e) {
        if (Protections.DisableInWorlds.getTxtSet().contains(e.getEntity().getWorld().getName()))
            return;
        if (!Protections.VillagerTradeCheesing.isEnabled())
            return;
        if (Protections.VillagerRestockTime.getIntValue() <= 0 || !fListener.getIs114())
            return;
        //if the villager is not in the list, add them with a new empty timestamp.
        if (!lastTrade.containsKey(e.getEntity().getUniqueId()))
            lastTrade.put(e.getEntity().getUniqueId(), 0l);
        if (System.currentTimeMillis() >= lastTrade.get(e.getEntity().getUniqueId())) {
            //timestamp is either empty or has expired, allow a restock.
            lastTrade.put(e.getEntity().getUniqueId(), System.currentTimeMillis() + 60000 * Protections.VillagerRestockTime.getIntValue());
        } else {
            //timestamp has not expired do not allow restocking.
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHandSwap(PlayerSwapHandItemsEvent e) {
        if (!Protections.PreventFoodDupe.isEnabled())
            return;
        ItemStack itm1 = e.getMainHandItem();
        ItemStack itm2 = e.getOffHandItem();
        if (itm1 == null || itm2 == null)
            return;
        if (consumables.contains(itm1.getType()) && consumables.contains(itm2.getType()) && itm1.getType() == itm2.getType()) {
            if (itm1.getAmount() == 1 && itm2.getAmount() == 1)
                return;
            e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            e.setCancelled(true);
            new BukkitRunnable() {

                @Override
                public void run() {
                    e.getPlayer().getInventory().setItemInMainHand(itm1);
                    e.getPlayer().getInventory().setItemInOffHand(itm2);
                }

            }.runTaskLater(IllegalStack.getPlugin(), 3);
        }
    }

    @EventHandler
    public void onPearlHit(ProjectileHitEvent e) {

        if (Protections.PreventPearlGlassPhasing.isEnabled()) {
            if (!(e.getEntity() instanceof EnderPearl) || e.getHitBlock() == null || !(e.getEntity().getShooter() instanceof Player) || e.getHitBlockFace() != BlockFace.UP)
                return;
            for (BlockFace face : faces) {
                Block next = e.getHitBlock().getRelative(face);
                Block above = next.getRelative(BlockFace.UP);
                Player p = ((Player) e.getEntity().getShooter());
                if (!fListener.getAirBlocks().contains(above.getType())) {
                    if (!fListener.getInstance().getGlassBlocks().contains(above.getType()))
                        continue;
                    Location l = e.getHitBlock().getRelative(e.getHitBlockFace()).getLocation().add(0.5, 0.5, 0.5);
                    fListener.getInstance().getTeleGlitch().put(p.getUniqueId(), l);
                }
            }
        }
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e) {
        if (!Protections.PreventFoodDupe.isEnabled())
            return;
        ItemStack itm1 = e.getPlayer().getInventory().getItem(e.getPreviousSlot());
        ItemStack itm2 = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (itm1 == null || itm2 == null)
            return;

        if (consumables.contains(itm1.getType()) && consumables.contains(itm2.getType()) && itm1.getType() == itm2.getType()) {
            if (itm1.getAmount() == 1 && itm2.getAmount() == 1)
                return;
            int tSlot = e.getNewSlot();
            ItemStack tFood = e.getPlayer().getInventory().getItem(tSlot);
            if (tFood == null) {
                tSlot = e.getPreviousSlot();
                tFood = e.getPlayer().getInventory().getItem(tSlot);
            }
            final int slot = tSlot;
            final ItemStack food = tFood;
            e.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
            new BukkitRunnable() {

                @Override
                public void run() {
                    e.getPlayer().getInventory().setItem(slot, food);
                }
            }.runTaskLater(IllegalStack.getPlugin(), 3);
                    }
    }

    /*
    @EventHandler
    public void onBlockGrow(StructureGrowEvent e) {

        if(e.getSpecies() == TreeType.BROWN_MUSHROOM || e.getSpecies() == TreeType.RED_MUSHROOM) {
            for(BlockState bs:e.getBlocks()) {
                if(bs.getType() == Material.END_PORTAL_FRAME)
                    System.out.println("Mushroom replaced portal frame");
                else
                    System.out.println("Replaced: " + bs.getType().name());
                bs.
            }

        }

    }

    */
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (Protections.PreventProjectileExploit.isEnabled())
            fTimer.trackProjectile(e.getEntity());
    }

    @EventHandler// (ignoreCancelled = false, priority=EventPriority.LOWEST)
    public void onFallingBlockSpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Projectile && Protections.PreventProjectileExploit.isEnabled())
            fTimer.trackProjectile((Projectile) e.getEntity());

        if (!(e.getEntity() instanceof FallingBlock) || !Protections.PreventVibratingBlocks.isEnabled())
            return;

        if (Protections.DisableInWorlds.isWhitelisted(e.getEntity().getWorld().getName()))
            return;

        fTimer.trackBlock((FallingBlock) e.getEntity());

    }
}
