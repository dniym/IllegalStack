
package timers;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;


import enums.Msg;
import enums.Protections;
import io.netty.util.internal.ThreadLocalRandom;
import listeners.mcMMOListener;
import main.IllegalStack;
import main.fListener;
import utils.NBTStuff;
import utils.SlimefunCompat;
import utils.SpigMethods;

public class fTimer implements Runnable {

	private long scanDelay = 1;
	private long nextScan = 0L;
	private long longScan = 0L;
	private long endScan = 0L;
	private static long endScanFinish = 0L;
	private static World dragon = null;
	
	private IllegalStack plugin;
	private boolean is1_8 = false;
	private static HashMap<FallingBlock,Long> fbTracker = new HashMap<>();
	private static HashMap<Projectile,Long> projTracker = new HashMap<>();
	private static HashSet<Location> endFountains = new HashSet<>();
	private static HashMap<Player,Entity> punish = new HashMap<>();	

	public fTimer(IllegalStack illegalStack) {
		this.plugin = illegalStack;
		this.nextScan = System.currentTimeMillis() + (scanDelay * 6000);

		
		String version = IllegalStack.getPlugin().getServer().getClass().getPackage().getName().replace(".", ",")
				.split(",")[3];
		is1_8 = version.equalsIgnoreCase("v1_8_R3") || version.contains("v1_8");

		if (is1_8)
			System.out.println(
					"[IllegalStack] - Minecraft 1.8 detected not checking offhand slot for overstacked items.");
		if (is1_8 || version.equalsIgnoreCase("v1_9_R4") || version.equalsIgnoreCase("v1_10_R2")) {
			System.out.println("[IllegalStack] version < 1.11 found, not checking for shulker boxes");
		}
		this.longScan = System.currentTimeMillis() + 10000l;

	}

	@Override
	public void run() {


		for(Player p:punish.keySet())
			fListener.punishPlayer(p,punish.get(p));
		punish.clear();
		
		if(Protections.BlockNonPlayersInEndPortal.isEnabled() && getDragon() != null && System.currentTimeMillis() > endScan) 
		{
			endScan = System.currentTimeMillis() + 500l;
			if(getDragon().getEnvironment() == Environment.THE_END)
			{
				
					for(int y=0;y<256;y++)
					{
						Location l = new Location(getDragon(),0,y,0);
						if(l.getBlock().getType() == Material.BEDROCK) //found bottom of portal
						{
							for(Entity ent:l.getWorld().getNearbyEntities(l, 3, 2, 3))
							{
								if(ent instanceof Player)
									continue;
								
								Vector v = ent.getVelocity();
								v.setY(4);
								v.setX(ThreadLocalRandom.current().nextInt(-2, 2));
								v.setZ(ThreadLocalRandom.current().nextInt(-2, 2));
								ent.setVelocity(v.normalize().multiply(2));
							}
							break;
						}
					}
				} 
		}
		
		if(System.currentTimeMillis() >= getEndScanFinish())
			setDragon(null);
		
		if(Protections.PreventPortalTraps.isEnabled() && System.currentTimeMillis() >= this.longScan) 
		{
			this.longScan = System.currentTimeMillis() + 10000l;
			for(Player p:Bukkit.getOnlinePlayers()) 
			{
				Block exit = p.getLocation().getBlock();
				
				if(exit.getType() != fListener.getPortal())
					continue;
				String invalid = "";
				for(BlockFace face:fListener.getFaces()) {
					exit = exit.getRelative(face);
					if(exit.getType() == fListener.getPortal())
						break;

				}

				Boolean valid = false;
				
				

				for(int i = 0; i < 5; i++) {
					for(BlockFace face:fListener.getFaces()) 
					{
						Block next = exit.getRelative(face);
						//System.out.println("checking bottom block: " + next.getType().name() + " valid? " + passThrough.contains(next.getType()));
						if(fListener.getPassThrough().contains(next.getType())) {
							//System.out.println("checking top block: " + next.getRelative(BlockFace.UP).getType().name() + " valid? " + passThrough.contains(next.getRelative(BlockFace.UP).getType()));	
							if(fListener.getPassThrough().contains(next.getRelative(BlockFace.UP).getType())) {
								valid=true;
								break;
							}  
				
						}
						

					} //didn't find a valid exit point at the exit block, lets search and try to find a new valid portal block to check
					if(!valid) {
						p.getLocation().getBlock().breakNaturally();
						fListener.getLog().append(Msg.StaffMsgBlockedPortalLogin.getValue(p, p.getLocation().toString()));
						System.out.println("invalid was: " + invalid);
						return;

					}

				}
			}



		}

		if(System.currentTimeMillis() >= this.nextScan)
		{

			if(Protections.PreventProjectileExploit.isEnabled()) {
				HashSet<Projectile> removed = new HashSet<>();
				for(Projectile p:projTracker.keySet()) {
					if(p == null || projTracker.get(p) >= System.currentTimeMillis()) 
						removed.add(p);

					if(p != null && p.getLocation().getBlock().getType() == Material.BUBBLE_COLUMN) {
						removed.add(p);
						p.remove();

					}

				}
				for(Projectile p:removed)
					projTracker.remove(p);

			}
			if(Protections.PreventVibratingBlocks.isEnabled()) {
				HashSet<FallingBlock> removed = new HashSet<>(); 
				for(FallingBlock fb:fbTracker.keySet()) {

					if(fb == null || fbTracker.get(fb) >= System.currentTimeMillis()) {
						removed.add(fb);
					}

					for(Entity ent:fb.getNearbyEntities(1, 1, 1))
						if(ent instanceof Minecart || ent instanceof Boat) {
							removed.add(fb);
							fb.remove();
							ent.remove();
						}
				}
				for(FallingBlock fb:removed)
					fbTracker.remove(fb);

			}


			for(Player p:plugin.getServer().getOnlinePlayers()) 
			{
				if(SpigMethods.isNPC(p)) continue;

				for(ItemStack is:p.getInventory().getContents()) 
				{
					if(is != null && !p.isOp()) {
						if(Protections.RemoveItemTypes.isWhitelisted(is))
						{
							if(Protections.RemoveItemTypes.notifyOnly()) 
								fListener.getLog().notify(Protections.RemoveItemTypes, " Triggered by: " + p.getName() + " with item: " + is.getType().name());
							else {
								fListener.getLog().append(Msg.ItemTypeRemovedPlayer.getValue(p, is));
								p.getInventory().remove(is);
							}
						}			
					}
					if(Protections.RemoveAllRenamedItems.isEnabled())
					{

						if(!p.hasPermission("IllegalStack.RenameBypass")) {

							if(is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName())
							{
								fListener.getLog().append(Msg.RemovedRenamedItem.getValue(p, is));
								p.getInventory().removeItem(is);
							}
						}

					}
					if(Protections.RemoveItemsMatchingName.isEnabled() && (!Protections.BlockEnchantingInstead.isEnabled() && !Protections.BlockRepairsInstead.isEnabled()))  
					{
						if(is != null && is.hasItemMeta()) 
						{
							ItemMeta im = is.getItemMeta();

							for(String s:Protections.ItemNamesToRemove.getTxtSet()) 
							{
								if(Protections.RemoveItemsMatchingName.loreNameMatch(im)) 
								{
									if(!Protections.RemoveItemsMatchingName.notifyOnly())  
									{

										fListener.getLog().append(Msg.NamedItemRemovalPlayer.getValue(p,is));
										is.setAmount(0);
										is.setType(Material.AIR);
									}
									return;
								}
							}


						}
					}
				}
			}

			this.nextScan = System.currentTimeMillis() + (this.scanDelay * 1000);
			//if(!Protections.RemoveOverstackedItems.isEnabled())
				//return;

			for(Player p:plugin.getServer().getOnlinePlayers()) {
				if(!is1_8) {
					
					if(p.getInventory().getItemInOffHand() != null)
					{
						if(Protections.DisableInWorlds.getTxtSet().contains(p.getWorld().getName()))
							continue;
						ItemStack is = p.getInventory().getItemInOffHand();
						if(is.getAmount() > is.getMaxStackSize()) {

							if(!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
							{
								if(!Protections.AllowStack.isWhitelisted(is.getType().name(),p))
									continue;

								if(Protections.FixOverstackedItemInstead.isEnabled()) {
									is.setAmount(is.getType().getMaxStackSize());
									fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
									continue;
								} else {
									p.getInventory().remove(is);
									fListener.getLog().append(Msg.IllegalStackItemScan.getValue(p,is));
									continue;
								}	

							} 

							if(Protections.AllowStack.isWhitelisted(is.getType().name(),p))
								continue;

							if(Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && p.hasPermission("illegalstack.overstack"))
								continue;

							if(Protections.RemoveOverstackedItems.notifyOnly())  
								continue;
							
							if(Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
								if(!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isnt empty
									if(!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isnt in a checked world
										continue;
								if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass"))
									continue;
								if(is != null && is.getEnchantments().isEmpty()) {


									HashSet<Enchantment> replace = new HashSet<>();
									for(Enchantment en:is.getEnchantments().keySet()) 
										if(is.getEnchantmentLevel(en) > en.getMaxLevel())
										{

											if(SlimefunCompat.isValid(is,en))
												continue;
											if(IllegalStack.isClueScrolls() && en == Enchantment.DURABILITY && is.getType() == Material.PAPER)
												continue;
											if(IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(en) ==  4341))
												continue;
											if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
												break;
											if(Protections.CustomEnchantOverride.isAllowedEnchant(en,is.getEnchantmentLevel(en)))
												continue;
											fListener.getLog().append(Msg.IllegalEnchantLevel.getValue(p, is,en));
											replace.add(en);

										} else {
											if(!en.canEnchantItem(is)) {
												if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
													continue;
												if(SlimefunCompat.isValid(is, en))
													continue;

												replace.add(en);
												fListener.getLog().append(Msg.IllegalEnchantType.getValue(p, is, en));
											}
										}

									for(Enchantment en:replace) {
										is.removeEnchantment(en);
										if(en.canEnchantItem(is))
											is.addEnchantment(en, en.getMaxLevel());
									}
								}

							}
							if(Protections.FixOverstackedItemInstead.isEnabled()) {
								fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
								ItemStack iz = is;
								iz.setAmount(iz.getType().getMaxStackSize());
								p.getInventory().setItemInOffHand(iz);
							} else {
								p.getInventory().setItemInOffHand(new ItemStack(Material.ROTTEN_FLESH,1));
								fListener.getLog().append(Msg.IllegalStackOffhand.getValue(p,is));
							}
							//			    			

						}
					}
				}

				for(ItemStack is:p.getInventory().getArmorContents())
					if(is != null && is.getAmount() > is.getMaxStackSize())
					{
						if(Protections.DisableInWorlds.getTxtSet().contains(p.getWorld().getName()))
							continue;
						if(!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
						{
							if(!Protections.AllowStack.isWhitelisted(is.getType().name(),p))
								continue;

							if(Protections.FixOverstackedItemInstead.isEnabled()) {
								is.setAmount(is.getType().getMaxStackSize());
								fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
								continue;
							} else {
								p.getInventory().remove(is);
								fListener.getLog().append(Msg.IllegalStackPlayerBody.getValue(p,is));
								continue;
							}	

						} 
						
						if(Protections.AllowStack.isWhitelisted(is.getType().name(),p))
							continue;

						if(Protections.RemoveOverstackedItems.notifyOnly())  
							continue;

						if(Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
							if(!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isnt empty
								if(!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isnt in a checked world
									continue;
							if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass"))
								continue;
							if(is != null && is.getEnchantments().isEmpty()) {


								HashSet<Enchantment> replace = new HashSet<>();
								for(Enchantment en:is.getEnchantments().keySet()) 
									if(is.getEnchantmentLevel(en) > en.getMaxLevel())
									{

										if(SlimefunCompat.isValid(is,en))
											continue;
										if(IllegalStack.isClueScrolls() && en == Enchantment.DURABILITY && is.getType() == Material.PAPER)
											continue;
										if(IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(en) ==  4341))
											continue;
										if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
											break;
										if(Protections.CustomEnchantOverride.isAllowedEnchant(en,is.getEnchantmentLevel(en)))
											continue;
										fListener.getLog().append(Msg.IllegalEnchantLevel.getValue(p, is,en));
										replace.add(en);
									} else {
										if(!en.canEnchantItem(is)) {
											if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
												continue;
											if(SlimefunCompat.isValid(is, en))
												continue;

											replace.add(en);
											fListener.getLog().append(Msg.IllegalEnchantType.getValue(p, is, en));
										}
									}

								for(Enchantment en:replace) {
									is.removeEnchantment(en);
									if(en.canEnchantItem(is))
										is.addEnchantment(en, en.getMaxLevel());
								}
							}

						}
						if(Protections.FixOverstackedItemInstead.isEnabled()) {
							fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
							is.setAmount(is.getType().getMaxStackSize());

						} else {

							fListener.getLog().append(Msg.IllegalStackPlayerBody.getValue(p, is));
							is.setAmount(0);
							is.setType(Material.AIR);
							p.getInventory().remove(is);
						}
					}

				for(ItemStack is:p.getInventory().getContents()) {

					if(is == null || Protections.DisableInWorlds.getTxtSet().contains(p.getWorld().getName()))
						continue;

					if(is.hasItemMeta()) {
						ItemMeta im = is.getItemMeta();

						if(Protections.RemoveUnbreakableFlag.isEnabled()) {

							if(im.isUnbreakable()) {
								if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
									continue;

								fListener.getLog().append(Msg.UnbreakableItemCleared.getValue(p, is));
								im.setUnbreakable(false);
								is.setItemMeta(im);

							}
						}

						if(Protections.RemoveCustomAttributes.isEnabled() ) {

							if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
								continue;
							NBTStuff.checkForBadCustomData(is,p,false);

						}

						if(Protections.PreventInvalidPotions.isEnabled() && im instanceof PotionMeta) {
							if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass")) 
								continue;

							if(IllegalStack.isHasMCMMO()) {
								if(NBTStuff.hasNbtTag("IllegalStack", is, "mcmmoitem", Protections.PreventInvalidPotions)) {
									
									continue;
								}
							}
							PotionMeta potion = (PotionMeta) is.getItemMeta();
							PotionData pd = potion.getBasePotionData();
							if(pd.getType() == PotionType.UNCRAFTABLE || (potion.hasCustomEffects() && !potion.getCustomEffects().isEmpty()))
							{

								if(pd.getType() == PotionType.UNCRAFTABLE && potion.getCustomEffects().isEmpty())
									continue;

								p.getInventory().remove(is);
								String efx = "";
								for(PotionEffect ce:potion.getCustomEffects())
									efx = efx + ce.getType().getName() + " amplifier: " + ce.getAmplifier() + " duration: " + ce.getDuration() + ",";

								fListener.getLog().append(Msg.InvalidPotionRemoved.getValue(p, efx));
							}

						}

					}
					if(Protections.FixIllegalEnchantmentLevels.isEnabled() && !mcMMOListener.ismcMMOActive(p)) {
						if(!Protections.OnlyFunctionInWorlds.getTxtSet().isEmpty()) //world list isnt empty
							if(!Protections.OnlyFunctionInWorlds.getTxtSet().contains(p.getWorld().getName())) //isnt in a checked world
								continue;

						if(is != null && !is.getEnchantments().isEmpty()) {
							if(Protections.AllowBypass.isEnabled() && p.hasPermission("illegalstack.enchantbypass"))
								continue;	

							HashSet<Enchantment> replace = new HashSet<>();
							for(Enchantment en:is.getEnchantments().keySet()) 
								if(is.getEnchantmentLevel(en) > en.getMaxLevel())
								{

									if(SlimefunCompat.isValid(is,en))
										continue;
									if(IllegalStack.isClueScrolls() && en == Enchantment.DURABILITY && is.getType() == Material.PAPER)
										continue;
									if(IllegalStack.isEpicRename() && ((en == Enchantment.LURE || en == Enchantment.ARROW_INFINITE) && is.getEnchantmentLevel(en) ==  4341))
										continue;
									if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
										break;
									if(Protections.CustomEnchantOverride.isAllowedEnchant(en,is.getEnchantmentLevel(en)))
										continue;
									fListener.getLog().append(Msg.IllegalEnchantLevel.getValue(p, is,en));
									replace.add(en);

								} else {
									if(!en.canEnchantItem(is)) {
										if(Protections.EnchantedItemWhitelist.isWhitelisted(is))
											continue;
										if(SlimefunCompat.isValid(is, en))
											continue;
										replace.add(en);
										fListener.getLog().append(Msg.IllegalEnchantType.getValue(p, is, en));
									}
								}

							for(Enchantment en:replace) {
								is.removeEnchantment(en);
								if(en.canEnchantItem(is))
									is.addEnchantment(en, en.getMaxLevel());
							}
						}

					}
					if(is != null && is.getAmount() > is.getMaxStackSize())
					{
						if(!Protections.IllegalStackMode.isEnabled())  //in blacklist mode and on the blacklist
						{
							if(!Protections.AllowStack.isWhitelisted(is.getType().name(),p))
								continue;

							if(Protections.FixOverstackedItemInstead.isEnabled()) {
								is.setAmount(is.getType().getMaxStackSize());
								fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
								continue;
							} else {
								p.getInventory().remove(is);
								fListener.getLog().append(Msg.IllegalStackItemScan.getValue(p,is));
								continue;
							}	

						} 
						if(Protections.AllowStack.isWhitelisted(is.getType().name(),p))
							continue;

						if(Protections.AllowStackForGroup.isWhitelisted(is.getType().name()) && p.hasPermission("illegalstack.overstack"))
							continue;

						if(Protections.RemoveOverstackedItems.notifyOnly())  
							continue;


						if(Protections.FixOverstackedItemInstead.isEnabled()) {
							fListener.getLog().append(Msg.IllegalStackShorten.getValue(p,is));
							is.setAmount(is.getType().getMaxStackSize());
						} else {
							fListener.getLog().append(Msg.IllegalStackItemScan.getValue(p, is));
							p.getInventory().remove(is);
						}
					}
				}
			}

		}


	}

	public static void trackBlock(FallingBlock fb) {
		fbTracker.put(fb,System.currentTimeMillis() + 4000l);


	}

	public static void trackProjectile(Projectile proj) {
		projTracker.put(proj,System.currentTimeMillis() + 14000l);

	}

	public World getDragon() {
		return dragon;
	}

	public static void setDragon(World world) {
		fTimer.dragon = world;
	}

	public static long getEndScanFinish() {
		return endScanFinish;
	}

	public static void setEndScanFinish(long endScanFinish) {
		fTimer.endScanFinish = endScanFinish;
	}

	public static HashMap<Player,Entity> getPunish() {
		return punish;
	}

	public static void setPunish(HashMap<Player,Entity> punish) {
		fTimer.punish = punish;
	}



}
