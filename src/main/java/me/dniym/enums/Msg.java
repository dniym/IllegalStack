package main.java.me.dniym.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public enum Msg {

    //@  Location of offense
    //~name~ name of player or entity
    //~contents~ contents of an item frame
    //~removedblocks~ blocked removed
    //~author~    book author
    //~blockType  block type
    //~item~

    PluginPrefix("[IllegalStack] -"),
    PluginTeleportText(ChatColor.GOLD + "Click to teleport here"),

    ChestRemoved("removed a chest from a ~entity~ @"),
    ChestPrevented("prevented ~player~ from putting a chest on a ~entity~ @"),
    StaffChestPunishment(
            "punished ~player~ for continuing to try to put chests on a creature.  A ~entity~ was removed, the player's inventory has been cleared and the player has been kicked from the server. @"),
    BookRemoved("Found a book with characters that do NOT match the specified character set and removed it.. Author was: ~author~"),
    TooManyPages("Found a book with too many pages and removed it from the inventory of ~name~"),
    SignRemoved("Found a sign not matching the allowed character set in the world @"),
    SignRemovedOnPlace(
            "detected a sign placed by ~name~ that contained characters not in the allowed character set.  Possible dupe machine exploit! @"),
    SignKickPlayerMsg("Placing signs with unicode characters is NOT permitted."),
    ShulkerClick("Removed a shulker box from inside another shulker box clicked by: ~name~"),
    ShulkerPlace("Removed a shulker box containing illegally stacked, or enchanted items on player ~player~  @"),

    ItemFrameRemoveOnExtend("removed an item frame containing - ~contents~ on the back of a retracting piston @"),
    ItemFrameRemoveOnRetract("removed an item frame containing - ~contents~ pulled by a sticky piston"),
    PistonRetractionDupe("Stopped Rail/Carpet Dupe & Removed Piston @ ~removedblocks~"),
    NetherPortalBlock("Blocked an entity from entering or leaving the nether: ~name~ @"),
    EndPortalBlock("Blocked an entity from entering or leaving the end: ~name~ @"),
    MinecartGlitch1("Located and removed a minecart glitched inside another block : ~blocktype~ @"),
    MinecartGlitch2("Stopped a minecart from being glitched into a block @"),
    HeadInsideSolidBlock("Stopped ~player~ 's head from being inside a solid block while in a ~vehicle~ @"),
    HeadInsideSolidBlock2("Stopped ~player~ 's head from being inside a ~block~ Reason: potential Xray Glitch"),
    MinecartMount("Prevented a ~entity~ from being able to mount a ~vehicle~ @"),
    ZeroTickGlitch("Stopped Zero tick crop growth glitch and removed (~removedblocks~) @"),
    NamedItemRemovalHopper("Found an item named ~item~ in a hopper, it has been removed @"),
    NamedItemRemovalPlayer("Found an item named: ~item~ in ~name~'s inventory, it has been removed. @"),
    ItemTypeRemovedPlayer("Found a blacklisted item type: ~item~ in ~name~'s inventory, it has been removed"),
    ItemTypeRemovedPlayerOnPlace("~name~ attempted to place a blacklisted item type: ~item~, it has been removed"),
    ItemTypeRemovedPlayerOnDrop("~name~ attempted to drop a blacklisted item type: ~item~, it has been removed"),
    ItemTypeRemoved("Found a blacklisted item type: ~item~ in ~player~'s inventory, it has been removed"),
    SilkTouchBookBlocked("Stopped  ~name~ from breaking ~block~ using a silk touch book @"),

    PistonHeadRemoval("A piston head was exploded.. removing an orphan piston base @"),
    IllegalStackLogin("Illegal stack of items removed: ~item~ (~amount~) from player: ~name~ at login."),
    IllegalStackOffhand("Removed an illegal stack of items from the off hand of ~name~, ~item~ ( ~amount~)"),
    IllegalStackPlayerBody("Illegal stack of items removed: ~item~ (~amount~) from ~name~'s body during item scan."),
    IllegalStackItemScan("Illegal stack of items removed: ~item~ (~amount~) from player: ~name~ at item scan."),
    IllegalStackOnClick("Illegal stack of items removed: ~item~ (~amount~) from player: ~name~ on click in inventory."),
    IllegalStackShorten("Fixed an illegal stack of: ~item~ (~amount~) triggered by player: ~name~."),
    IllegalStackDurability("Fixed durability on: ~item~ triggered by player: ~name~."),
    IllegalStackUnstack(
            "Unstacked an illegal stack of: ~item~ (~amount~) triggered by player: ~name~.  ~lost~ items were unable to fit and lost."),
    InvalidPotionRemoved("Removed invalid potion from ~name~ had the following effects: ~effects~"),
    UnbreakableItemCleared("Removed Unbreakable flag from ~item~ found on player ~name~"),
    CustomAttribsRemoved("Removed Custom Attributes on ~item~ held by ~name~ (~attributes~)"),
    CustomAttribsRemoved2("Removed ~item~ with Custom Attributes worn by ~name~ (~attributes~)"),
    CustomAttribsRemoved3("Removed ~item~ with Custom attributes found in ~name~'s inventory (~attributes~)"),
    GlideActivateMaxBuild("Prevented ~name~ from activating an elytra while above the max build height. @"),
    GlideAboveMaxBuild("Player ~name~ was using an elytra above the max build height, disabling glide. @"),
    CorrectedPlayerLocation("(possible pearl glitch into block) Corrected an enderpearl teleport location for ~player~ @"),
    StoppedPushableArmorStand("Prevented an armor stand from being lifted straight up via a piston @"),
    StoppedPushableEntity("Prevented an entity (armor stand/end crystal) from being pushed into another entity @"),
    RemovedRenamedItem("Removed a renamed item ~item~ from the inventory of ~name~"),
    BlockedTripwireDupe(
            "Player ~name~ attempted to place a tripwire hook on a trap door, it has been removed (PreventTripwireDupe = true)"),
    GenericItemRemoval("~item~ removed by protection ~protection~, found on source ~source~"),
    PlayerTrappedPortalMsg(
            "&cSorry ~name~ but that portal appears to not have a valid exit!  You would be trapped if you went through it!"),
    PlayerCommandSleepMsg("&cSorry but all commands are disabled while sleeping!"),
    PlayerDisabledBookMsg("&cSorry but player book editing is disabled on this server!"),
    PlayerKickMsgFishing("&cAuto Clicker Fishing is not allowed on this server!"),
    PlayerKickMsgFishMod(
            "&cAttention! it looks like you may be using an Auto Fishing Mod...  Please change the spot you're fishing to avoid getting kicked!"),
    PlayerSwimExploitMsg("&cThat villager is too busy swimming to trade with you right now!"),
    PlayerCMIShulkerNest("&cSorry you can not put a shulker into another shulker!"),
    PlayerDisabledHorseChestMsg("&cSorry, chests on horses, llamas, mules etc are disabled!"),
    PlayerDisabledRidingChestedMsg("&cSorry, taming creatures that can carry chests is disabled!"),
    PlayerKickForChestMsg("&cYou were warned about chests on mobs, stop trying."),

    PlayerNearbyNotification("a nearby exploit was detected ~prot~"),
    PlayerNetherBlock("&cSorry ~name~ players are not allowed on top of the nether!"),
    PlayerEnchantBlocked("&cSorry ~name~ Enchanting this item is not permitted."),
    PlayerRepairBlocked("&cSorry ~name~ Repairing this item is not permitted."),
    PlayerSpawnEggBlock("&cSorry you can not use spawn eggs to change spawner types!"),

    StaffMsgChangedSpawnerType("Player ~player~ used ~type~ to change a spawner type @"),
    StaffMsgEndGatewayVehicleRemoved("player ~name~ attempted to take a ~vehicle~ through an end gateway, it has been removed."),
    StaffMsgBlockedPortalLogin("broke a trapped nether portal @"),
    StaffMsgBlockedPortal("~player~ was prevented from going through a blocked/trapped nether portal @"),
    StaffMsgDropperExploit("Detected a hopper/dropper loop exploit and removed a dispenser/dropper @"),
    StaffMsgDispenerFlint("Detected a downward facing dispenser from dispensing flint and steel which would crash the server @"),
    StaffMsgSpawnerReset("A ~type~ spawner was reset to a pig spawner when broken by ~name~ @"),
    StaffMsgCreativeBlock("~name~ was prevented from loading in an illegal item via the creative saved toolbar."),
    StaffMsgNetherBlock("~name~ was prevented from accessing the top of the nether @"),
    StaffMsgNetherFix("~name~ has been teleported down from above the nether ceiling @"),
    StaffMsgUnderNether("~name~ has been killed for flying under the nether floor @"),
    StaffMsgNetherCart("Stopped ~name~ riding in a vehicle above the nether ceiling @"),
    StaffMsgBookRemoved("Removed a writable book from player: ~name~ because player book creation is disabled!"),
    StaffProtectionToggleMsg("Protection: ~protection~ has been turned ~status~ by ~name~"),
    StaffInvalidProtectionMsg("&cYou must supply a valid protection name to add values to:"),
    StaffOptionUpdated("&aOption successfully updated!"),
    StaffSingleWordsOnly("&cThat protection does not support multiple word arguments!"),
    StaffStringUpdated("&aValue updated with string value: ~value~"),
    StaffEnchantBypass(
            "&cPlease left click with the item in hand you wish to add to the EnchantedItemWhitelist, left click with nothing in hand or use /istack cancel to cancel."),
    StaffEnchantBypassCancel("&cEnchantedItemWhitelist add item mode DISABLED."),
    StaffEnchantBypassAdded("&aAdded ~itemdata~ to the EnchantedItemWhitelist!"),
    StaffSpamFishingNotice("&a~player~ is spam fishing! ~casts~ casts without a 2 second break! @"),
    StaffAutoFishingNotice("&a~player~ appears to be using an autofishing mod..  ~count~ caught within 0.3 blocks of each other @"),
    StaffBadShulkerRemoved("Removed a hacked shulker box with an excessive amount of items in it (~size~) from ~name~ @"),
    StaffBadShulkerInWorld("Removed a dropped shulker box with an excessive amount of items in it (~size~) from the world: @"),
    StaffNoItem("You must be holding an item in your main hand to force fix it's enchantment!"),
    StaffNoEnchants("This item has no enchants to fix!"),
    StaffEnchantFixed("Corrected ~amount~ enchants on ~item~"),
    StaffNoNBTAPI(
            "NBT-Api was not found on your server and is needed for ~prot~ since it is enabled! Please download and install it from:  https://www.spigotmc.org/resources/nbt-api.7939/"),
    StaffEnchantNotFixed("IllegalStack did not detect any invalid enchantments on this item."),
    StaffEndPortalProtected("Prevented the end portal from being broken using a dispenser @"),
    StaffMsgNoPerm("You do not have permission to use that IllegalStack feature, node required: ~perm~"),
    StaffMsgBedExplosion("Prevented a bed from being used as an explosive @"),
    DestroyedEnchantedItem("Destroyed an Illegally Enchanted Item ~item~ ~enchant~.(~lvl) found on player: ~player~"),
    IllegalEnchantLevel("&aFixed Enchantment Level ~enchant~.(~lvl~) on ~item~ found on player: ~player~"),
    IllegalEnchantType(
            "&aCould not fix Enchantment ~enchant~.(~lvl~) on ~item~ found on player: ~player~ this enchantment is not valid for this item type!");

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Msg.class.getSimpleName());

    private String value;

    Msg(String val) {
        this.setValue(val);
    }

    public String getValue(String variable) {
        String val = value;
        val = val.replace("~perm~", variable);
        val = val.replace("~prot~", variable);
        val = val.replace("~author~", variable);
        val = val.replace("~name~", variable);
        val = val.replace("~contents~", variable);
        val = val.replace("~removedblocks~", variable);
        val = val.replace("@", "@" + variable);
        val = val.replace("~value~", variable);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Location location, String types) {
        String val = value;
        val = val.replace("@", "@ " + location.toString());
        val = val.replace("~removedblocks~", types);
        val = val.replace("~contents~", types);
        val = val.replace("~item~", types);
        val = val.replace("~name~", types);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(ItemStack is, Protections prot, Player plr, String source) {
        String val = value;

        val = val.replace("~item~", is.getType().name());
        val = val.replace("~protection~", prot.getDisplayName());
        val = val.replace("~source~", source);
        if (plr != null) {
            val = plr.getName() + " - " + val;
        }

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player player, String displayName) {
        String val = value;
        val = val.replace("@", "@ " + player.getLocation());
        val = val.replace("~item~", displayName);
        val = val.replace("~name~", player.getName());
        val = val.replace("~player~", player.getName());
        val = val.replace("~type~", displayName);
        val = val.replace("~block~", displayName);
        val = val.replace("~effects~", displayName);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(HumanEntity le, String value) {
        String val = value;

        val = val.replace("~author~", value);
        val = val.replace("~name~", le.getName());
        return val;
    }

    public String getValue(Player p, Location loc) {
        String val = value;

        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + loc.toString());
        return val;
    }

    public String getValue(String source, Location loc) {
        String val = value;

        val = val.replace("~player~", source);
        val = val.replace("@", "@ " + loc.toString());
        return val;
    }

    public String getValue(Player p, Integer count, Location loc) {
        String val = value;

        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~count~", count.toString());
        return val;
    }

    public String getValue(Player p, Entity ent) {
        String val = value;

        val = val.replace("~name~", p.getName());
        val = val.replace("~player~", p.getName());
        val = val.replace("~entity~", ent.getName());
        val = val.replace("~vehicle~", ent.getType().name());
        val = val.replace("@", "@ " + ent.getLocation());
        return val;
    }

    public String getValue(Entity ent1, Entity ent2) {
        String val = value;

        if (ent1 instanceof Player) {
            val = val.replace("~entity~", ent1.getName());
        } else {
            val = val.replace("~entity~", ent1.getType().name());
        }

        val = val.replace("~vehicle~", ent2.getType().name());
        val = val.replace("@", "@ " + ent1.getLocation());
        return val;
    }

    public String getValue(Location loc, ItemStack is) {
        String val = value;


        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Object obj, ItemStack is, Enchantment en) {


        String val = value;

        val = val.replace("~item~", is.getType().name());
        if (en == null) {
            val = val.replace("~enchant~", "");
        } else {
            val = val.replace("~enchant~", en.getName());
            val = val.replace("~lvl~", is.getEnchantmentLevel(en) + "");
        }
        if (obj instanceof BlockState) {
            val = val.replace("~player~", "a " + ((BlockState) obj).getBlock().getType().name() + "'s inventory");
        } else if (obj instanceof Player) {
            val = val.replace("~player~", ((Player) obj).getName());
        } else if (obj instanceof Inventory) {
            val = val.replace("~player~", ((Inventory) obj).getType().name() + " - ");
        } else if (obj instanceof Location) {
            val = val.replace("~player~", "a shulker box");
        } else if (obj instanceof Container) {
            val = val.replace("~player~", ((Container) obj).getType().name());
        }

        Location loc = null;

        if (obj instanceof BlockState) {
            loc = ((BlockState) obj).getLocation();
        }
        if (obj instanceof Player) {
            loc = ((Player) obj).getLocation();
        } else if (obj instanceof Block) {
            loc = ((Block) obj).getLocation();
        } else if (obj instanceof Inventory) {
            loc = ((Inventory) obj).getLocation();
        } else if (obj instanceof Location) {
            loc = ((Location) obj);
        } else if (obj instanceof Container) {
            loc = ((Container) obj).getLocation();
        }

        if (loc != null) {
            val = val.replace("@", "@ " + loc);
        } else {
            val = val.replace("@", "@ UNKNOWN ");
        }
        return val;

    }

    public String getValue(Player p, ItemStack is, Enchantment en) {
        String val = value;

        val = val.replace("~item~", is.getType().name());
        val = val.replace("~enchant~", en.getName());
        val = val.replace("~lvl~", is.getEnchantmentLevel(en) + "");
        val = val.replace("~player~", p.getName());
        val = val.replace("@", "@ " + p.getLocation());
        return val;

    }

    public String getValue(Player p, EntityType et) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~type~", et.name());
        val = val.replace("~name~", p.getName());
        return ChatColor.translateAlternateColorCodes('&', val);
    }


    public String getValue(Object obj, ItemStack is) {
        if (obj instanceof Inventory) {
            if (((Inventory) obj).getHolder() instanceof Player) {
                Inventory inv = (Inventory) obj;
                return getValue((Player) inv.getHolder(), is);
            } else {
                return getValue(((Inventory) obj).getLocation(), is);
            }
        } else if (obj instanceof Container) {
            return getValue(((Container) obj), is);
        }
        LOGGER.error(
                "An unknown object {} was passed to IllegalStack during a logging operation please report this to dNiym at the spigot forums or on the IllegalStack Discord.",
                obj.toString()
        );
        return "???";
    }

    public String getValue(Container c, ItemStack is) {
        String val = value;

        val = val.replace("@", "@ " + c.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", c.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(ItemStack is, Object obj, StringBuilder list) {
        String val = value;

        if (obj instanceof Player) {
            val = val.replace("@", "@ " + ((Player) obj).getLocation());
            val = val.replace("~name~", ((Player) obj).getName());
        } else if (obj instanceof Inventory) {
            Inventory inv = null;
            inv = (Inventory) obj;
            if (inv.getHolder() instanceof Container) {
                val = val.replace(
                        "~name~",
                        ((Container) inv.getHolder())
                                .getLocation()
                                .getBlock()
                                .getType()
                                .name() + " @" + ((Container) inv.getHolder()).getLocation()
                );
            } else if (inv.getHolder() instanceof DoubleChest) {
                val = val.replace(
                        "~name~",
                        ((DoubleChest) inv.getHolder()).getLocation().getBlock().getType().name() + " @" + inv
                                .getLocation()
                                .toString()
                );
            } else if (inv.getHolder() instanceof Player) {
                val = val.replace("~name~", ((Player) inv.getHolder()).getName() + " @" + inv.getLocation().toString());
            } else {
                LOGGER.error(
                        "IllegalStack was supposed to send a message detailing an inventory but could not determine its type!  Please contact dNiym at the IllegalStack discord or on Spigot with this message: {} ",
                        obj.toString()
                );
            }
        }


        val = val.replace("~item~", is.getType().name());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~attributes~", list);

        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is, String list) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~attributes~", list);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getConfigVal() {
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(Protections p, String name, String status) {
        String val = value;

        val = val.replace("~protection~", p.name());
        val = val.replace("~name~", name);
        val = val.replace("~status~", "" + status);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, int size) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~size~", "" + size);
        val = val.replace("~name~", p.getName());


        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Location loc, int size) {
        String val = value;

        val = val.replace("@", "@ " + loc.toString());
        val = val.replace("~size~", "" + size);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

    public String getValue(Player p, ItemStack is, int lostItems) {
        String val = value;

        val = val.replace("@", "@ " + p.getLocation());
        val = val.replace("~item~", is.getType().name());
        val = val.replace("~name~", p.getName());
        val = val.replace("~amount~", "" + is.getAmount());
        val = val.replace("~lost~", "" + lostItems);
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}
