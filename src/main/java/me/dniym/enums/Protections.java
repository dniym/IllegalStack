package main.java.me.dniym.enums;


import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.utils.MagicHook;
import main.java.me.dniym.utils.NBTStuff;
import me.jet315.minions.MinionAPI;
import me.jet315.minions.minions.Minion;
import net.brcdev.shopgui.gui.gui.OpenGui;
import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public enum Protections {

    DisableInWorlds(
            1,
            new String[]{},
            "Disable In Worlds",
            "ALL",
            "Misc.DisableInWorlds",
            "Disables IllegalStack in the specified worlds.   Warning... this completely disables ALL protections in that world, meaning if a dupe exploit exists then it is up to you to make sure that players can not transfer items from a unprotected world, into a protected one!  Use at your own risk!",
            "",
            0,
            false
    ),
    //MISC SETTINGS
    InGameNotifications(
            3,
            true,
            "In Game Notifications",
            "ALL",
            "Misc.InGameOffenseNotifications",
            "Send notifications to staff members in chat whenever an exploit is detected, this permission defaults to OP but can be granted by giving a player the: illegalstack.notify permission node. ",
            "",
            0
    ),
    LogOffensesInSeparateFile(
            3,
            true,
            "Logging Offenses To File",
            3,
            "Misc.LogOffensesInSeparateFile",
            "Records all offenses and locations caught by IllegalStack into a separate file located in plugins/IllegalStack/OffenseLog.txt",
            "",
            0
    ),
    PlayerOffenseNotifications(
            3,
            false,
            "Notify Nearby Players of Offenses",
            3,
            "Misc.NotifyNearbyPlayers",
            "Notify players in a 10 block radius of a offense or any that directly were caused by them",
            "",
            0
    ),
    BreakExploitMachines(
            22,
            true,
            "Break Exploit Machines",
            "ALL",
            "Misc.BreakMachinesInsteadOfDroppingItems",
            "If this setting is set to FALSE, instead of removing pistons etc that are detected in an exploit, IllegalStack will break the block and drop the item instead.",
            "",
            0
    ),
    ItemScanTimer(
            45,
            10,
            "Item Scanner Timer Delay",
            "ALL",
            "Misc.ItemScanTimer",
            "Allows you to set the time (in ticks) between scans for bad items, this defaults to 10 ticks, (twice per second).  Increasing this value too much could result in items being missed adjust with caution.  NOTE:  If you adjust this value you MUST restart the server before the setting will be updated.",
            "",
            0
    ),
    //ALL VERSION EXPLOITS
    FixNegativeDurability(
            64,
            true,
            "Fix negative durability",
            "ALL",
            "Exploits.NegativeDurability",
            "Fixes durability on items that is zero or less.",
            "",
            0
    ),
    PreventMinecartsInBoats(
            52,
            true,
            "Prevent Minecarts In Boats",
            "ALL",
            "Exploits.Other.PreventMinecartsInBoats",
            "Prevent players from putting minecarts into boats, creating the possibility for a dupe.",
            "",
            0
    ),
    PreventBedrockDestruction(
            7,
            true,
            "Prevent Bedrock Destruction",
            "ALL",
            "Exploits.Other.PreventBedrockDestruction",
            "Prevent bedrock destruction via players blowing the heads off pistons with TNT",
            "",
            0
    ),
    PreventEndPortalDestruction(
            51,
            true,
            "Prevent End Portal Destruction",
            "ALL",
            "Exploits.Other.PreventEndPortalDestruction",
            "Prevent players from using dispensers to break the end portal blocks.",
            "",
            0
    ),
    PreventPortalTraps(
            48,
            true,
            "Prevent Nether Portal Traps",
            "ALL",
            "Exploits.NetherPortal.PreventPortalTrap",
            "Prevents players from entering a portal that has no valid exit, meaning if the player cant walk out of the portal the teleport is blocked and a message is sent.",
            "",
            0
    ),
    BlockNonPlayersInNetherPortal(
            16,
            true,
            "Prevent Nether Portal Dupe",
            "ALL",
            "Exploits.PortalDupe.BlockNonPlayersInNetherPortal",
            "This setting prevents non player entities from travelling through a nether portal (typically horses and donkeys).  This exploit is used to duplicate the contents of a entities inventory by killing them just as they enter the portal, this setting also defeats sand / falling block dupers.",
            "",
            0
    ),
    NetherWhiteList(
            16,
            new String[]{},
            "Nether Portal Whitelist",
            16,
            "Exploits.PortalDupe.NetherWhiteList",
            "Add any entities you would like to be able to travel to the nether.   Be warned if the creature is able to pick up items or have an inventory players WILL be able to dupe on your server.",
            "",
            0,
            false
    ),
    NetherWhiteListMode(
            16,
            true,
            "Whitelist Mode",
            16,
            "Exploits.PortalDupe.NetherWhiteListMode",
            "If TRUE this list is a whitelist, if FALSE this list is a blacklist.",
            "",
            0
    ),
    BlockNonPlayersInEndPortal(
            17,
            true,
            "Prevent End Portal Dupe",
            "ALL",
            "Exploits.PortalDupe.BlockNonPlayersInEndPortal",
            "This setting prevents non player entities from travelling through the end portal (typically horses and donkeys).  This exploit is used to duplicate the contents of a entities inventory by killing them just as they enter the portal, this setting also defeats sand / falling block dupers.",
            "",
            0
    ),
    EndWhiteList(
            17,
            new String[]{},
            "End Portal Whitelist",
            17,
            "Exploits.PortalDupe.EndWhiteList",
            "Add any entities you would like to be able to travel to the end.   Be warned if the creature is able to pick up items or have an inventory players WILL be able to dupe on your server.",
            "",
            0,
            false
    ),
    EndWhiteListMode(
            17,
            true,
            "Whitelist Mode",
            17,
            "Exploits.PortalDupe.EndWhiteListMode",
            "If TRUE this list is a whitelist, if FALSE this list is a blacklist.",
            "",
            0
    ),
    NotifyBlockedPortalAttempts(
            17,
            false,
            "Notify Staff on Blocked Portal Attempts",
            17,
            "Exploits.PortalDupe.NotifyBlockedPortalAttempts",
            "Notify staff whenever a portal attempt is blocked, this option is OFF by default as sand duper setups can be very spammy.",
            "",
            0
    ),
    PreventEndGatewayCrashExploit(
            49,
            true,
            "Prevent End Gateway Crash Exploit",
            "> 1.9",
            "Exploits.EndGateway.PreventEndGatewayCrashExploit",
            "Prevents players from riding entities through a end gateway, this can be used to crash the server.",
            "",
            0
    ),
    PreventHoppersToUnloadedChunks(
            11,
            true,
            "Prevent Hopper Unload Exploit",
            "ALL",
            "Exploits.Other.PreventHoppersToUnloadedChunks",
            "Prevents a dupe glitch when two hoppers are used across a chunk border.",
            "",
            0
    ),
    PreventMinecartGlitch(
            15,
            true,
            "Prevent Minecart Glitching",
            "ALL",
            "Exploits.MineCart.PreventMinecartGlitch",
            "Prevents players using pistons to push a block or push a mincart into a block, this exploit is typically used in collection systems.",
            "",
            0
    ),
    MinecartBlockWhiteList(
            15,
            new String[]{},
            "Allow Minecart Glitch Into",
            15,
            "Exploits.MineCart.MinecartBlockWhiteList",
            "Any block types (Materials) added here will be ignored if a minecart is glitched into them.",
            "",
            0,
            false
    ),
    RemoveExistingGlitchedMinecarts(
            15,
            false,
            "Remove Old Glitched Minecarts",
            15,
            "Exploits.MineCart.RemoveExistingGlitchedMinecarts",
            "This setting will detect minecarts that have been previously glitched into a block, It is recommended to only turn this setting on if you know players have glitched minecarts in your worlds, once you're sure it's been removed turn this feature back off for performance reasons.",
            "",
            0
    ),
    KickForAutoClickerFishing(
            31,
            true,
            "Prevent Auto Clicker Fishing",
            "ALL",
            "Exploits.Fishing.KickForAutoClickerFishing",
            "Detects autoclickers that spam fish using a block where the player spams the right mouse button (usually automatically) and fish are reeled in the instant they bite.",
            "",
            0
    ),
    WatchForAutoFishMod(
            20,
            true,
            "Prevent Auto Fish Mods",
            "ALL",
            "Exploits.FishMod.WatchForAutoFishMod",
            "Detects automatic fishing mods that cast and reel in fish that mimic regular player fishing, this is not an instantaneous detection and does not detect auto main.java.me.dniym.fishing on the first attempt..",
            "",
            0
    ),
    MaxFishAllowedBeforeKick(
            20,
            5,
            "Max fish before kick",
            20,
            "Exploits.FishMod.MaxFishAllowedBeforeKick",
            "This is the number of fish a player is allowed to catch before they are kicked (once detected for auto fishing)",
            "",
            0
    ),
    WarnPlayerThenKickInsteadOfNotify(
            20,
            false,
            "Notify Instead of Kick",
            20,
            "Exploits.FishMod.WarnPlayerThenKickInsteadOfNotify",
            "This setting defaults to false, and staff is notified when a player is suspected of using an auto fishing mod.  If set to true the player will be warned right before they're about to be kicked after the plugin has detected they are using an auto-me.dniym.fishing mod.",
            "",
            0
    ),
    MaxFishToNotifyStaffThenBlock(
            20,
            5,
            "Max fish before notify",
            20,
            "Exploits.FishMod.MaxFishToNotifyStaffThenBlock",
            "If Notify instead of kick is set to true, then staff will be notified once the plugin has detected a player using an auto fish mod after they catch this many fish.",
            "",
            0
    ),

    PreventIndirectTNTPowerDupe(
            21,
            true,
            "Prevent Tnt Dupers",
            "ALL",
            "Exploits.TNTDupe.PreventIndirectTNTPowerDupe",
            "Prevents tnt duping methods that exploit an indirect power bug that causes tnt to ignite and fall away / be launched but leave an unlit block of tnt behind.",
            "",
            0
    ),
    BlockPlayersAboveNether(
            29,
            true,
            "Block Players Above Nether",
            "ALL",
            "Exploits.Nether.BlockPlayersAboveNether",
            "Prevents players from teleporting / walking on top of the nether.",
            "",
            0
    ),
    EnsureSafeTeleportLocationIfAboveCeiling(
            29,
            true,
            "Ensure Safe Teleport Below Ceiling",
            29,
            "Exploits.Nether.EnsureSafeTeleportLocationIfAboveCeiling",
            "Makes sure the player wont be teleported back into solid netherrack or mid-air if teleport back down from above the nether ceiling.",
            "",
            0
    ),
    BlockBuildingAboveNether(
            29,
            true,
            "Block Building Above Nether",
            29,
            "Exploits.Nether.BlockBuildingAboveNether",
            "Prevents players from placing/breaking blocks when above the NehterYLevel setting.",
            "",
            0
    ),
    KillPlayersBelowNether(
            29,
            false,
            "Kill Players Under Nether",
            29,
            "Exploits.Nether.KillPlayersBelowNether",
            "Kills players who fly under the nether floor.",
            "",
            0
    ),
    NetherYLevel(
            29,
            128,
            "Nether Y Level Override",
            29,
            "Exploits.Nether.NetherYLevel",
            "Adjust the Y Level of the nether, useful for custom world generators.",
            "",
            0
    ),
    ExcludeNetherWorldFromHeightCheck(
            29,
            new String[]{},
            "Exclude These Worlds From Height Check",
            29,
            "Exploits.Nether.ExcludeNetherWorldFromHeightCheck",
            "Adding a nether world here will exclude it from the height check..  This should only ever be used if you have a nether world that has a non vanilla nether ceiling height, eg maybe bSkyblockNether's.  This will ONLY affect a nether world, no effect if the world is not a nether!",
            "",
            0,
            false
    ),

    RemoveBooksNotMatchingCharset(
            4,
            true,
            "Invalid Book Protection",
            "< 1.15",
            "Exploits.BookExploit.RemoveBooksNotMatchingCharset",
            "Automatically delete any written book that does not match the charset specified in the configuration (and if the author is NOT on the whitelist).  This exploit is used to create books full of garbage characters that will increase the size of a chunk past what the server will save.  It prevents a chunk from being properly saved and allows players to dupe.",
            "",
            0
    ),
    ValidCharset(
            4,
            "US-ASCII",
            "Valid Charset",
            4,
            "Exploits.BookExploit.ValidCharset",
            "This is the currently set character set that IllegalStack compares sign and book text to, if you have book/sign protections enabled.   Any character that is NOT part of this charset is considered illegal and helps find exploited books and signs.  If you need to change this value a list of valid character sets can be found here: https://docs.oracle.com/javase/7/docs/technotes/guides/intl/encoding.doc.html",
            "",
            0
    ),
    BookAuthorWhitelist(
            4,
            new String[]{},
            "Book Author Whitelist",
            4,
            "Exploits.BookExploit.BookAuthorWhitelist",
            "Any player names added to this list will bypass all book creation restrictions.",
            "",
            0,
            false
    ),

    PageCountThreshold(
            4,
            5,
            "Page Count Threshold",
            4,
            "Exploits.BookExploit.PageCountThreshold",
            "Number of pages containing Illegal Characters that can be found per book before it is flagged as illegal",
            "",
            0
    ),
    LimitNumberOfPages(
            4,
            0,
            "Page Number Limit",
            4,
            "Exploits.BookExploit.LimitNumberOfPages",
            "Maximum number of pages a book can contain, if this is set to any number above ZERO then the book will be removed if it has more than the value set.  Eg setting this to 5 will remove any book with 6 or more pages regardless of the book's contents.",
            "",
            0
    ),
    DestroyBadSignsonChunkLoad(
            5,
            false,
            "Destroy Bad Signs on Chunk Load",
            "ALL",
            "Exploits.SignExploit.DestroyBadSignsOnChunkLoad",
            "Check chunks when they're first loaded for signs that have non standard characters (usually from a hacked client) which are used to exploit the save state glitch like the book dupe, and to prevent players from being able to log in while in that chunk (sign banning)..   You should ONLY enable this protection if you know you have chunks with bad signs, as it will use resources checking every block for signs.",
            "",
            0
    ),

    RemoveOverstackedItems(
            6,
            true,
            "Remove Overstacked Items",
            "ALL",
            "Exploits.OverStack.RemoveOverstackedItems",
            "Detects and removes items that have amounts larger than the vanilla stack size.",
            "",
            0
    ),
    IllegalStackMode(
            6,
            true,
            "Whitelist Mode",
            6,
            "Exploits.OverStack.StackWhiteListMode",
            "If TRUE this list is a whitelist, if FALSE this list is a blacklist.",
            "",
            0
    ),
    AllowStack(
            6,
            new String[]{"POTION"},
            "Overstackable Items",
            6,
            "Exploits.OverStack.AllowStack",
            "Items added to this list can be larger than the vanilla stack sizes, ie Potions/Ender Pearls",
            "",
            0,
            false
    ),
    PreventOverStackedItemInHoppers(
            6,
            true,
            "Remove Overstacked Items in Hoppers",
            6,
            "Exploits.OverStack.PreventOverStackedItemInHoppers",
            "Removes overstacked items if they are found inside hoppers, this can be turned off safely if you do not have players with large stored amounts of overstacked items... If off players can use hoppers to extract items from a large stack one by one.",
            "",
            0
    ),
    FixOverstackedItemInstead(
            6,
            false,
            "Fix Illegal Stacks",
            6,
            "Exploits.OverStack.FixOverstackedItemInstead",
            "Instead of removing the entire stack, set the stack to the maximum stack size for that item type.",
            "",
            0
    ),
    AllowStackForGroup(
            6,
            new String[]{},
            "Overstackable Items Per Group",
            6,
            "Exploits.OverStack.GroupStack",
            "Items added to this list will be allowed for players with the IllegalStack.Overstack permission. (You can add a * here to allow players with this permission to overstack ANY item.",
            "",
            0,
            false
    ),
    RemoveItemTypes(
            6,
            new String[]{},
            "Remove items of Type",
            6,
            "Exploits.OverStack.RemoveItemsOfType",
            "Item types (eg STRUCTURE_BLOCK) added to this list will be removed if found in a players inventory, useful if players on your server have obtained blocks you do not wish for them to have such as Bedrock",
            "",
            0,
            false
    ),

    FixIllegalEnchantmentLevels(
            25,
            false,
            "Fix Illegal Enchantment Levels",
            "< 1.17",
            "Exploits.Enchants.FixIllegalEnchants",
            "Will correct any enchantment level found that is larger than vanilla minecraft allows.",
            "",
            0
    ),
    CustomEnchantOverride(
            25,
            new String[]{},
            "Enchantment Override",
            25,
            "Exploits.Enchants.CustomEnchantOverride",
            "Allows for adjusting of a given enchantments max level.  Meaning if you add Sharpness.10 to this list then only items with sharpness 11 and above will be removed.  *NOTE* this has no other affect on other enchants.",
            "",
            0,
            false
    ),
    EnchantedItemWhitelist(
            25,
            new String[]{},
            "Item Whitelist",
            25,
            "Exploits.Enchants.EnchantedItemWhitelist",
            "Will skip fixing any item exactly matching the type,name and lore.",
            "",
            2,
            false
    ),
    OnlyFunctionInWorlds(
            25,
            new String[]{},
            "Worlds To Check",
            25,
            "Exploits.Enchants.OnlyFunctionInWorlds",
            "Will ONLY check for IllegalEnchantments in the worlds listed, if this list is empty all worlds are checked by default.",
            "",
            2,
            false
    ),
    AllowBypass(
            25,
            false,
            "Allow Permission Bypass",
            25,
            "Exploits.Enchants.AllowBypass",
            "Will allow any player with the illegalstack.enchantbypass permission to bypass the enchantment level check (defaults to OP)..  Note if a player is given the item and does not have the permission it's enchantments will still be removed.",
            "",
            2
    ),
    RemoveUnbreakableFlag(
            25,
            false,
            "Remove Unbreakable flag from items",
            25,
            "Exploits.Enchants.RemoveUnbreakableFlag",
            "Will remove the Unbreakable flag from items held by players (unless they have the bypass permission).",
            "",
            0
    ),
    RemoveCustomAttributes(
            25,
            false,
            "Remove Custom Attribute Modifiers",
            25,
            "Exploits.Enchants.RemoveCustomAttributes",
            "Will remove ALL custom attributes found on items held by players (unless they have the bypass permission).  This is useful for getting rid of sticks with +1000 damage or insta death helmets that have been previously cheated in.  *NOTE*  If you're running < 1.13 this protection requires NbtAPI 2.1.0+ ",
            "",
            2
    ),
    DestroyIllegallyEnchantedItemsInstead(
            25,
            false,
            "Destroy Items Instead of Fixing",
            25,
            "Exploits.Enchants.DestroyIllegallyEnchantedItemsInstead",
            "Instead of fixing the item destroy it instead",
            "",
            0
    ),

    BlockLoopedDroppers(
            32,
            false,
            "Block Looped Droppers",
            "ALL",
            "Exploits.DropperDupe.BlockLoopedDroppers",
            "Prevent Dropper/Dispensers from feeding items back and forth",
            "",
            0
    ),
    PreventRNGEnchant(
            34,
            true,
            "Prevent RNG Enchant",
            "> 1.9",
            "Exploits.RNGEnchant.PreventRNGEnchant",
            "Prevents an exploit that allowed players to crack the random enchantment seed, allowing them to pick exactly which enchantments they want on an item.",
            "",
            0
    ),
    PreventLootingExploit(
            50,
            true,
            "Prevent Looting Exploit",
            "ALL",
            "Exploits.Looting.PreventLootingExploit",
            "Prevents an exploit that allows players to use ranged weapons such as bows or crossbows to enable looting by holding a looting sword in their offhand.",
            "",
            0
    ),
    PreventLavaDupe(
            55,
            true,
            "Prevent Lava Dupe",
            "ALL",
            "Exploits.LavaDupe.PreventLavaDupe",
            "Prevents an exploit using lava and hoppers to dupe on multiple versions",
            "",
            0
    ),
    PreventRecordDupe(
            61,
            true,
            "Prevent Record Dupe",
            "ALL",
            "Exploits.RecordDupe",
            "Prevents an exploit using tnt, a skeleton and a pit full of creepers, used to mass farm records all at once.",
            "",
            0
    ),

    //PacketAttackWindowClick(33,false,"Prevent Packet Crasher 1", "ALL", "Exploits.PacketAttack.PacketCrasher1", "Prevents Oversized packets and packet spam that  ")
    //MULTI VERSION EXPLOITS
    PreventRailDupe(
            8,
            true,
            "Destroy Rail / Carpet Dupers",
            "1.12/1.13/1.14/1.15/1.16/1.17",
            "Exploits.Other.PreventRailDupe",
            "Prevent redstone machines designed to dupe carpets and rails, these items are usually duped to provide infinite fuel for furnaces or to sell for in game money in shops.",
            "",
            0
    ),

    PreventNestedShulkers(
            10,
            true,
            "Prevent Nested Shulker Boxes",
            "> 1.11",
            "Exploits.Other.PreventNestedShulkers",
            "Prevent players from putting shulker boxes inside other shulker boxes, this exploit leads to pretty much infinte storage.",
            "",
            0
    ),

    DisableChestsOnMobs(
            27,
            true,
            "Disable Chests on Mobs",
            "ALL",
            "Exploits.Other.DisableChestsOnMobs",
            "Prevents players from using or adding chests to Llamas, Donkeys, Horses etc.  Used to prevent players with hacked clients from duping useing these creatures.",
            "",
            0
    ),
    DisableRidingExploitableMobs(
            27,
            true,
            "Disable Riding/Taming of Mobs with chests",
            27,
            "Exploits.Other.DisableRidingExploitableMobs",
            "Prevents players from being able to ride or tame a creature that can be equipped with a chest.",
            "",
            0
    ),
    PunishForChestsOnMobs(
            27,
            false,
            "Violently Punish Repeat Offenders",
            27,
            "Exploits.Other.PunishForChestsOnMobs",
            "Enabling this option will punish any player who attempts to place a chest on a chested animal, the animal will be destroyed, the player's inventory will be cleared and the player will be kicked from the server.",
            "",
            0
    ),

    PreventInvalidPotions(
            35,
            true,
            "Prevent Invalid Potions",
            "> 1.11",
            "Exploits.Other.PreventInvalidPotions",
            "Prevents non-opped players from possessing invalid / uncraftable potions.  Typically these are used for malicious purposes on creative servers such as potions of instant death",
            "",
            0
    ),
    PreventInfiniteElytraFlight(
            36,
            true,
            "Prevent Infinite Elytra Flight",
            "> 1.9",
            "Exploits.Other.PreventInvalidElytraFlight",
            "Prevents players from using a glitch that grants unlimited elytra flight time without rockets,  This exploit allows the player to ascend vertically starting at the max build height until they decide to start descending or their elytra breaks.",
            "",
            0
    ),

    PreventItemSwapLagExploit(
            37,
            true,
            "Prevent Item Swap Lag Exploit",
            "> 1.9",
            "Exploits.Other.PreventItemSwapLagExploit",
            "Prevents players from spamming the server with held item swaps creating lag on the server.",
            "",
            0
    ),
    PreventPearlGlassPhasing(
            38,
            true,
            "Prevent Enderpearl Glass Phasing",
            "> 1.9",
            "Exploits.Teleport.PearlPhasing",
            "Prevents players from using enderpearls to phase through glass blocks.",
            "",
            0
    ),
    TeleportCorrectionNotify(
            38,
            false,
            "Notify of teleport corrections",
            38,
            "Exploit.Teleport.CorrectionNotify",
            "If set to true the plugin will notify staff whenever a correction to a teleport is made due to ender pearl phasing detection, this is off by default as it can be spammy.",
            "",
            0
    ),
    PreventArmorStandLagMachine(
            39,
            true,
            "Prevent Armor Stand Lag Machine",
            "> 1.11",
            "Exploit.LagMachines.ArmorStand",
            "Prevents pistons from lifting armor stands straight up then dropping them down, typically used to construct lag machines.",
            "",
            0
    ),
    PreventEndCrystalLagMachine(
            43,
            true,
            "Prevent End Crystal Lag Machine",
            "> 1.11",
            "Exploit.LagMachines.End Crystal",
            "Prevents pistons pushing end crystals into a huge pile, typically used to construct lag machines.",
            "",
            0
    ),
    PreventProjectileExploit(
            41,
            true,
            "Prevent Projectile Lag Exploit",
            "1.14",
            "Exploits.1_14_Exploits.Entities.PreventProjectileExploit",
            "Prevents Projectiles such as arrows from getting trapped inside bubble columns, creating lag when lots of these items are floating and falling constantly.",
            "",
            0
    ),
    PreventCommandsInBed(
            47,
            true,
            "Prevent Commands While In Bed",
            "ALL",
            "Exploits.Other.PreventCommandsInBed",
            "Prevent players from being able to use main.java.me.dniym.commands while in bed.  This has been linked to a serious exploit where not all events fire properly while a player is in bed, one huge exploit with this is players can get any item out of a GUI if they can open it while sleeping.",
            "",
            0
    ),
    PreventBedExplosions(
            56,
            true,
            "Prevent Bed Explosions",
            "ALL",
            "Exploits.Other.PreventBedExplosions",
            "Prevent players from using beds in the end/nether as cheap explosives for PVP and destroying blocks.",
            "",
            0
    ),
    PreventSpawnEggsOnSpawners(
            59,
            true,
            "Prevent Using Spawn Eggs On Spawners",
            "> 1.13",
            "Misc.Spawners.PreventSpawnEggsOnSpawners",
            "Prevent non opped players from being able to use spawn eggs on spawners to change the spawned creature type.",
            "",
            0
    ),
    //3rd Party Plugins
    BlockCMIShulkerStacking(
            10,
            true,
            "CMI Shulker Box Fix",
            "ALL",
            "Exploits.3rdParty.BlockCMIShulkerStacking",
            "The CMI plugin offers a feature that allows a shulker box to be opened without being placed.  Since this is not an actual shulker box it allows players to put shulkers inside shulkers without any exploit.  This setting prevents that behavior.",
            "",
            0
    ),

    //1.12 ONLY
    PreventItemFramePistonDupe(
            13,
            true,
            "Prevent Item Frame / Piston Dupe",
            "1.12",
            "Exploits.1_12_Exploits.PreventItemFramePistonDupe",
            "Prevents item frames from duping items when broken with pistons.",
            "",
            0
    ),
    PreventRecipeDupe(
            9,
            true,
            "Prevent Recipe Book Dupe",
            "1.12",
            "Exploits.1_12_Exploits.PreventRecipeDupe",
            "This dupe was around when the recipe book was first introduced to minecraft, it involved dropping an item then spam crafting an item (like a crafting bench), would result in huge over stacks of items",
            "",
            0
    ),
    PreventShulkerCrash(
            62,
            true,
            "Prevent 1.12 Shulker Crash",
            "1.12",
            "Exploits.ShulkerCrash",
            "Prevents players from using a dispenser to place a shulker above the max build height, crasing the server",
            "",
            0
    ),
    //1.13 ONLY
    PreventVillagerSwimExploit(
            18,
            true,
            "Prevent Villager Trade Swim Exploit",
            "1.13",
            "Exploits.1_13_Exploits.PreventVillagerSwimExploit",
            "Prevents players from exploiting a bug with the new villager trade mechanics that would cause them to constantly reduce their prices just by the player opening/closing the trade menu while the merchant was swimming.",
            "",
            0
    ),
    PreventExcessiveFireworkExploit(
            32,
            true,
            "Prevent Firework from having an excessive number of effects",
            "1.13",
            "Exploits.1_13_Exploits.PreventExcessiveFireworkExploit",
            "Detects fireworks which have an excessive number of effects.",
            "",
            0
    ),
    //1.14 ONLY
    SilkTouchBookExploit(
            28,
            true,
            "Block Silk Touch Book Exploit",
            "1.14",
            "Exploits.1_14_Exploits.Misc.BlockSilkTouchBookExploit",
            "Prevents players from using a Silk Touch book (in hand) to break blocks as if they were using a silk touch tool.",
            "",
            0
    ),
    PreventFoodDupe(
            31,
            true,
            "Prevent Food/Consumable Dupe Glitch",
            "1.14.4",
            "Exploits.1_14_Exploits.Misc.PreventFoodDupe",
            "Stops the 1.14.4 consumable / food dupe exploit.",
            "",
            0
    ),
    PreventVibratingBlocks(
            40,
            true,
            "Prevent Vibrating Block Exploit",
            "1.14",
            "Exploits.1_14_Exploits.Entities.VibratingBlockExploit",
            "Prevents Falling blocks from getting trapped in a state where they constantly update causing crops to grow like a zero tick farm.",
            "",
            0
    ),
    //1.16 ONLY
    PreventPiglinDupe(
            57,
            true,
            "Prevent Piglin Dupe",
            "1.16 / 1.16.1 / 1.16.2",
            "Exploits.1_16_Exploits.Dupes.PreventPiglinDupe",
            "Prevents piglins from being abused to duplicate items while bartering (does not affect paper)",
            "",
            0
    ),
    PreventShulkerCrash2(
            63,
            true,
            "Prevent Shulker Crash w/ Flint and steel",
            "1.16",
            "Exploits.ShulkerCrash2",
            "Prevents players from using a downward facing dispenser with flint and steel to crash the server.",
            "",
            0
    ),
    //1.14 / 1.15 ONLY

    VillagerTradeCheesing(
            19,
            true,
            "Prevent Villager Trade Cheesing",
            "1.14 / 1.15 / 1.16 / 1.17",
            "Exploits.1_14_Exploits.Traders.BlockVillagerTradeCheesing",
            "Prevents players from placing / breaking a villagers work station over and over which forces them to get new trades, typically people abuse this to make sure they get specific enchantments or items from a villager rather than it being a random mechanic.",
            "",
            0
    ),
    VillagerRestockTime(
            19,
            10,
            "Minimum Restock Time (Minutes)",
            19,
            "Exploits.1_14_Exploits.Traders.VillagerRestockTime",
            "Sets the minimum number of minutes that a villager is allowed to restock trades.. NOTE* This is in real life minutes, and any changes to the in game time will be ignored, meaning if players trade with a villager then go to sleep to advance the time, they will not normally restock the next morning.",
            "",
            0
    ),
    ZombieVillagerTransformChance(
            19,
            65,
            "Villager Zombification Chance",
            19,
            "Exploits.1_14_Exploits.Traders.ZombieVillagerTransformChance",
            "Allows you to lower the chance a Villager will become a Zombie Villager if infected.   This is 100% on Difficulty Hard in vanilla..  This allows players to infect/cure villagers over and over to cheapen their trades.   Setting this to a value less than 100 will cause such players to risk loosing the villager instead of being able to cheese the trades easily. **(Only really matters if your server difficulty is set to HARD)** *If set to zero this setting will totally prevent conversion.",
            "",
            0
    ),

    PreventCactusDupe(
            12,
            true,
            "Prevent Zero Tick Exploits",
            "1.13 / 1.14 / 1.15",
            "Exploits.Other.PreventZeroTickExploit",
            "Breaks redstone machines that eploit a game mechanic that causes cacti and other growable blocks grow much faster than normal.",
            "",
            0
    ),

    PreventTripwireDupe(
            46,
            true,
            "Prevent Tripwire Dupe",
            "1.15 / 1.16 / 1.17",
            "Exploits.1_15_Exploits.Dupes.PreventTripwireDupe",
            "Prevents players from using trapdoors to dupe tripwire hooks.",
            "",
            0
    ),
    //User Requested | Obscure Features
    PreventZombieItemPickup(
            14,
            false,
            "Prevent Item Pick By Zombies",
            "ALL",
            "UserRequested.Mobs.PreventZombieItemPickup",
            "Prevents zombies from picking up items normally, this was used to prevent the drowned dupe and is off by default, left in as it was requested by a user.",
            "",
            2
    ),
    PreventCobbleGenerators(
            45,
            false,
            "Prevent Cobblestone Generators",
            "ALL",
            "UserRequested.Cobble.PreventCobbleGenerators",
            "Prevents lava and water from creating cobblestone when they flow into each other.",
            "",
            2
    ),
    ResetSpawnersOfType(
            33,
            new String[]{},
            "Force Spawner Reset",
            "1.13/1.14",
            "UserRequested.Spawners.ResetSpawnersOfType",
            "Resets a spawner of a given <Entity_Type> to a pig spawner if mined, only really useful if you have a silk spawner plugin and some specific spawners in the world that you don't want to allow players to mine.",
            "",
            2,
            false
    ),

    RemoveItemsMatchingName(
            23,
            false,
            "Remove Items With Specific Names",
            "ALL",
            "UserRequested.ItemRemoval.RemoveItemsMatchingName",
            "If this setting is set to TRUE, any item matching the name will be destroyed, useful if you have items taken out of GUI's due to another plugins bug.   OFF by default (User Requested Feature)",
            "",
            2
    ),
    ItemNamesToRemove(
            23,
            new String[]{},
            "Item Names To Match",
            23,
            "UserRequested.ItemRemoval.ItemNamesToRemove",
            "Add item names to this list and if RemoveItemsMatchingName is true, they will be removed like an illegal stacked item would be.",
            "",
            2,
            false
    ),
    ItemLoresToRemove(
            23,
            new String[]{},
            "Item Lores To Match",
            23,
            "UserRequested.ItemRemoval.ItemLoresToRemove",
            "Add any lores that identify items you wish to have removed, such as if players were able to get items out of a shop plugin illegally and IllegalStack will remove these items once detected.",
            "",
            2,
            false
    ),
    NameLoreStrictMatchMode(
            23,
            false,
            "Match lore | name exactly",
            23,
            "UserRequested.ItemRemoval.NameLoreStrictMatchMode",
            "If this value is true, then the item name or lore line must EXACTLY match including color codes, when set to false as long as the text is included in the lore or name a match will be detected.",
            "",
            2
    ),
    BlockEnchantingInstead(
            23,
            false,
            "Block Enchants Instead",
            23,
            "UserRequested.ItemRemoval.BlockEnchantingInstead",
            "If this value is true then instead of removing the named/lore matched item prevent players from enchanting this item.",
            "",
            2
    ),
    BlockRepairsInstead(
            23,
            false,
            "Block Repairs Instead",
            23,
            "UserRequested.ItemRemoval.BlockRepairsInstead",
            "If this value is true then instead of removing the named/lore matched item prevent players from repairing this item.",
            "",
            2
    ),

    NotifyInsteadOfBlockExploits(
            26,
            new String[]{},
            "Notify ONLY Instead Of Block",
            "ALL",
            "UserRequested.Misc.NotifyInsteadOfBlock",
            "Any protections added to this list will NOT be blocked, however notifications will still be sent.   Caution: This may produce a fair amount of spam for some protections",
            "",
            2,
            false
    ),
    BlockBadItemsFromCreativeTab(
            30,
            false,
            "Block Bad Items From Creative Tab",
            "> 1.12",
            "UserRequested.Obscure.BlockBadItemsFromCreativeTab",
            "Prevents players from giving themselves items in a single player world with metadata into a server if they have access to GMC.   This exploit allows players to create items not normally obtainable through regular /GMC such as sticks that give 5000 health when held... NOTE: this fix requires ProtocolLib to be installed to work!",
            "",
            2
    ),
    DestroyInvalidShulkers(
            31,
            false,
            "Destroy Client Crashing Shulkers",
            "> 1.11",
            "UserRequested.Obscure.HackedShulker.DestroyInvalidShulkers",
            "Destroys Shulker boxes created by hacked clients that are used to ban players (Book ban variation) **NOTE** Requires NBT API 2.0.0 (in all spigot versions) to work!!",
            "",
            2
    ),
    CheckGroundForBadShulkerAtLogin(
            31,
            false,
            "Remove Bad Shulkers on Ground",
            31,
            "UserRequested.Obscure.HackedShulker.CheckGroundForBadShulkerAtLogin",
            "Removes any dropped shulker boxes in the world found when a player logs in that contain invalid client crashing data.  You should ONLY enable this protection if you know you have areas with bad shulkers in a chunk.  This protection will turn itself off each time the server restarts. **NOTE** Requires NBT API 2.0.0 to work!!",
            "",
            2
    ),
    //PreventHeadBan(44, true, "Prevent Head Chunk Ban", "ALL","Exploits.HeadChunkBan.PreventHeadBan","Destroys player heads that when placed in the world ban players who try to log in near them.  NOTE* This is useful for preventing them from being placed originally, to fix existing placed heads see the next protection.", "", 0),
    //CheckGroundForBadHeadsAtLogin(44, false, "Remove Bad Heads On Ground", 44,"Exploits.HeadChunkBan.CheckGroundForBadHeadsAtLogin","Destroys player heads placed in the world that ban players who try to log in near them.  You should ONLY enable this protection if you know you have areas with bad heads in a chunk.  This protection will turn itself off each time the server restarts. **NOTE** Requires NBT API 2.0.0 to work!!","",0),
    IgnoreAllHopperChecks(
            42,
            false,
            "Ignore ALL Hopper Checks",
            "ALL",
            "UserRequested.Obscure.HopperCheck.IgnoreAllHopperChecks",
            "Forces the plugin to Ignore any item or exploit involving a hopper.. WARNING this should only ever be enabled if you are absolutely sure you know what you are doing as it could open up the door to big problems with players being able to xfer duped items, or even allowing them to dupe in specific instances.  Please contact the plugin's author (dNiym) if you even THINK you need to turn this on.",
            "",
            2
    ),
    RemoveAllRenamedItems(
            44,
            false,
            "Remove ALL renamed items",
            "ALL",
            "UserRequested.Obsure.Misc.RemoveAllRenamedItems",
            "Removes any item that has been renamed found on any user without the IllegalStack.RenameBypass permission.",
            "",
            2
    ),
    DisableBookWriting(
            53,
            false,
            "Disable ALL Book Editing",
            "ALL",
            "Exploits.BookExploit.DisableBookWriting",
            "Disable ALL player book writing, any book and quill that is edited (by a player not on the BookAuthorWhiteList) will be removed and a message sent to the player.  This option is off by default and was a user requested feature.",
            "",
            2
    ),
    PreventHeadInsideBlock(
            54,
            false,
            "PreventHeadInsideBlock",
            "ALL",
            "Exploits.MineCart.PreventHeadInsideBlocks",
            "Kicks a user off/out of a vehicle if they enter a block while inside a vehicle.",
            "",
            2
    ),
    AlsoPreventHeadInside(
            54,
            new String[]{"COMPOSTER"},
            "Also Prevent Players Heads Inside",
            54,
            "Exploits.Minecart.AlsoPreventHeadInside",
            "Breaks a block if a players head gets glitched into it, typically used for blocks that can be abused to xray.  EG leaves/composters",
            "",
            2,
            false
    ),
    IgnoreAllShulkerPlaceChecks(
            58,
            false,
            "Ignore ALL Shulker Place Checks",
            "> 1.11",
            "UserRequested.Obscure.IgnoreAllShulkerPlaceChecks",
            "Forces the plugin to Ignore any shulker place event. This will disable removal of stacked items when a shulker is placed",
            "",
            2
    ),
    DamagePlayersAboveNether(
    		60,
    		false,
    		"Damage Players Above Nether",
    		"ALL",
    		"UserRequested.NetherDamage.DamagePlayersAboveNether",
    		"If this option is enabled, players will be allowed on top of the nether however they will take damage over time as long as they remain on top of the nether.",
    		"",
    		2
    		),
    AboveNetherDamageDelay(
    		60,
    		2,
    		"Damage Delay",
    		60,
    		"UserRequested.NetherDamage.DamageDelay",
    		"Amount of time in seconds to apply damage to a player who is above the nether ceiling.",
    		"",
    		2
    		),
    AboveNetherDamageAmount(
    		60,
    		2,
    		"Damage Amount",
    		60,
    		"UserRequested.NetherDamage.DamageAmount",
    		"Amount of damage to give to a player who is above the nether ceiling.",
    		"",
    		2
    		),
    ;

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + Protections.class.getSimpleName());
    ///OPTIONS///
    private Object defaultValue = null;
    private boolean enabled = false;
    private String txtValue = "";
    private int intValue = -1;
    private HashSet<String> txtSet = new HashSet<>();
    private String serverVersion = "";
    private int protId = -1;
    private int parentId = 0;
    private boolean nukeApples = false;
    private String displayName;
    private String version = "";
    private String description = "";
    private String configPath;
    private String command = "";
    private boolean isList = false;
    private int catId = 0;
    private boolean relevant = false;

    Protections(
            int id,
            String[] array,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId,
            boolean relevant
    ) {
        this.defaultValue = new HashSet<String>();
        for (final String value : array) {
            ((HashSet<String>) this.defaultValue).add(value);
        }

        this.isList = true;
        this.setCatId(catId);
        this.setTxtSet(new HashSet<String>());

        for (String s : array) {
            this.getTxtSet().add(s);
        }

        setBasics(id, ver, dname, desc, path, cmd);

    }

    Protections(
            int id,
            Integer intVal,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = intVal;
        this.setCatId(catId);
        this.setIntValue(intVal);
        setBasics(id, ver, dname, desc, path, cmd);
    }

    Protections(
            int id,
            Boolean value,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = value;
        this.setDefaultValue(value);
        this.setCatId(catId);
        this.enabled = value;
        setBasics(id, ver, dname, desc, path, cmd);

    }

    Protections(
            int id,
            String setting,
            String dname,
            Object ver,
            String path,
            String desc,
            String cmd,
            int catId
    ) {
        this.defaultValue = setting;
        this.setCatId(catId);
        this.setTxtValue(setting);
        setBasics(id, ver, dname, desc, path, cmd);
    }

    public static Protections getProtection(String enumName) {

        for (Protections p : Protections.values()) {
            if (p.name().equalsIgnoreCase(enumName)) {
                return p;
            } else if (p.getConfigPath().contains(enumName)) {
                return p;
            }
        }

        return null;
    }

    public static void update() {

        for (Protections p : Protections.values()) {


            if (p.isList) {
                List<String> cVal = IllegalStack.getPlugin().getConfig().getStringList(p.getConfigPath());
                p.txtSet.clear();
                p.txtSet.addAll(cVal);
            } else if (p.intValue >= 0) {
                p.intValue = IllegalStack.getPlugin().getConfig().getInt(p.getConfigPath());
            } else if (p.txtValue != null && !p.txtValue.isEmpty()) {
                p.txtValue = IllegalStack.getPlugin().getConfig().getString(p.getConfigPath());
            } else {
                p.enabled = IllegalStack.getPlugin().getConfig().getBoolean(p.getConfigPath());

            }

            if ((p == Protections.DestroyBadSignsonChunkLoad || p == Protections.RemoveExistingGlitchedMinecarts || p == Protections.CheckGroundForBadShulkerAtLogin)) //p == Protections.CheckGroundForBadHeadsAtLogin ||
            {
                if (p.enabled) {

                    LOGGER.warn(
                            "You have the protection {} set to TRUE in your configuration.  This protection is intended to be a temporary setting and should not be left enabled!  Doing so causes IllegalStack to check all chunks whenever they are loaded which can create un-needed server load, and potentially cause other server issues.",
                            p.configPath
                    );
                    IllegalStack.getPlugin().getConfig().set(p.getConfigPath(), false);

                }
            }
        }

    }

    public static Protections findByConfig(String key) {
        for (Protections p : Protections.values()) {
            if (p.getConfigPath().equals(key)) {
                return p;
            }
        }

        return null;
    }

    public static Protections getParentByChild(Protections child) {
        for (Protections p : Protections.values()) {
            if (p.getProtId() == child.getParentId()) {
                return p;
            }
        }

        return null;
    }

    public static HashMap<Protections, Boolean> getRelevantTo(String ver) {
        HashMap<Protections, Boolean> relevant = new HashMap<>();

        for (Protections p : Protections.values()) {
            if (!p.getCommand().isEmpty()) {
                continue;
            }
            if (p.isRelevantToVersion(ver)) {
                relevant.put(p, true);
                for (Protections child : p.getChildren()) {
                    relevant.put(child, true);
                }

            } else {  //isn't relevant to this version
                if (p.version.isEmpty()) { //skip random child nodes
                    continue;
                }
                relevant.put(p, false);
                for (Protections child : p.getChildren()) {
                    relevant.put(child, false);
                }
            }
        }
        return relevant;
    }

    public static void fixEnchants(Player player) {


        ItemStack itemStack = player.getInventory().getItemInMainHand();
        NBTStuff.checkForBadCustomData(itemStack, player, true);
        if (itemStack == null) {
            player.sendMessage(Msg.StaffNoItem.getValue());
            return;
        }
        if (itemStack.getEnchantments().isEmpty()) {
            player.sendMessage(Msg.StaffNoEnchants.getValue());
            return;
        }

        HashSet<Enchantment> replace = new HashSet<>();
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (itemStack.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel()) {

                if (enchantment.canEnchantItem(itemStack)) {
                    player.sendMessage(Msg.IllegalEnchantLevel.getValue(player, itemStack, enchantment));
                } else {
                    player.sendMessage(Msg.IllegalEnchantType.getValue(player, itemStack, enchantment));
                }
                replace.add(enchantment);

            } else {
                if (!enchantment.canEnchantItem(itemStack)) {
                    replace.add(enchantment);
                    player.sendMessage(Msg.IllegalEnchantType.getValue(player, itemStack, enchantment));
                }
            }
        }

        for (Enchantment removeEnchantment : replace) {
            itemStack.removeEnchantment(removeEnchantment);
            if (removeEnchantment.canEnchantItem(itemStack)) {
                itemStack.addEnchantment(removeEnchantment, removeEnchantment.getMaxLevel());
                player.sendMessage(Msg.StaffEnchantFixed.getValue(player, itemStack, removeEnchantment));
            }
        }
        if (replace.isEmpty()) {
            player.sendMessage(Msg.StaffEnchantNotFixed.getValue());
        }

    }

    public Object getConfigValue() {
        if (isList) {
            return getTxtSet();
        } else if (txtValue != null && !txtValue.isEmpty()) {
            return txtValue;
        } else if (intValue != -1) {
            return intValue;
        } else {
            return enabled;
        }

    }

    public Object getDefaultValue() {

        if (defaultValue instanceof HashSet) {
            return defaultValue;
        } else if (defaultValue instanceof String) {
            return defaultValue;
        } else if (defaultValue instanceof Integer) {
            return defaultValue;
        } else {
            return defaultValue;
        }

    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String findValue() {

        String retVal = "[ ]";

        if (isList) {
            retVal = "[";
            for (String v : getTxtSet()) {
                retVal = retVal + v + ", ";
            }

            if (getTxtSet().isEmpty()) {
                retVal = retVal + "NONE SET";
            }

            retVal = retVal.trim() + "]";
        } else if (intValue != -1) {
            retVal = "" + intValue;
        } else if (!txtValue.isEmpty()) {
            retVal = txtValue;
        } else if (enabled) {
            retVal = ChatColor.GREEN + " ENABLED ";
        } else if (!enabled) {
            retVal = ChatColor.DARK_RED + "DISABLED ";
        }


        return retVal;
    }

    private void setBasics(int id, Object ver, String dname, String desc, String path, String cmd) {
        if (ver instanceof String) {
            this.setVersion((String) ver);
        } else {
            this.setParentId((Integer) ver);
        }
        this.setDisplayName(dname);
        this.setDescription(desc);
        this.setConfigPath(path);
        this.protId = id;
        this.setCommand(cmd);
        this.relevant = isRelevantToVersion(getServerVersion());
    }

    public boolean isEnabled() {
        if (this.getVersion().isEmpty()) //child node
        {
            return this.enabled;
        }
        return this.relevant && this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private String getServerVersion() {
        if (serverVersion == "") {
            String version = IllegalStack
                    .getPlugin()
                    .getServer()
                    .getClass()
                    .getPackage()
                    .getName()
                    .replace(".", ",")
                    .split(",")[3];


            version = IllegalStack.getString(version);
            if (version.equalsIgnoreCase("v1_15_R1")) {

                version = IllegalStack.getPlugin().getServer().getVersion().split(" ")[2];
                if (version.contains(" ")) {
                    version = version.replace(")", "");
                    version = version.replace(".", "_");
                    String[] ver = version.split("_");
                    version = "v" + ver[0] + "_" + ver[1] + "_R" + ver[2];
                }

            }

            serverVersion = version;

        }
        return serverVersion;
    }

    public boolean notifyOnly() {
        return Protections.NotifyInsteadOfBlockExploits.getTxtSet().contains(this.name());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRelevantToVersion(String serverVersion) {

        if (serverVersion.contains("_")) {
            serverVersion = serverVersion.replace("_", ".");
        }
        if (this.getVersion().isEmpty()) {
            return false; //must be a child node
        }


        if (this.getVersion().contains("< 1.17")) {
            return !serverVersion.contains("1.17");
        }

        if (this.getVersion().contains("< 1.15")) {
            return !serverVersion.contains("1.15") && !serverVersion.contains("1.16") && !serverVersion.contains("1.17");
        }

        if (this.getVersion().equalsIgnoreCase("1.14.4") && !serverVersion.contains("1.14.R4")) {
            return false;
        }

        if (this.getVersion().equalsIgnoreCase("1.14.3") && !serverVersion.contains("1.14.R3")) {
            return false;
        }

        if (this.getVersion().contains("ALL")) {
            return true;
        }

        if (this.getServerVersion().equalsIgnoreCase("1.16") && serverVersion.contains("1.16")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.17") && serverVersion.contains("1.17")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.16") && serverVersion.contains("1.16")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.15") && serverVersion.contains("1.15")) {
            return true;
        }

        if (this.getVersion().equalsIgnoreCase("1.14") && serverVersion.contains("1.14")) {
            return true;
        }

        if (this.getVersion().contains("1.14") && serverVersion.contains("1.14")) {
            return true;
        }

        if (this.getVersion().contains("1.15") && serverVersion.contains("1.15")) {
            return true;
        }

        if (this.getVersion().contains("1.16") && serverVersion.contains("1.16")) {
            return true;
        }

        if (this.getVersion().contains("1.17") && serverVersion.contains("1.17")) {
            return true;
        }

        if (this.getVersion().contains("> 1.12")) {
            if (serverVersion.contains("1.17") || serverVersion.contains("1.16") || serverVersion.contains("1.15") || serverVersion
                    .contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12")) {
                return true;
            }
        }

        if (this.getVersion().contains("> 1.9")) {
            if (serverVersion.contains("1.17") || serverVersion.contains("1.16") || serverVersion.contains("1.15") || serverVersion
                    .contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12") || serverVersion.contains(
                    "1.11") ||
                    this.serverVersion.contains("1.10") || this.serverVersion.contains("1.9")) {
                return true;
            }
        }

        if (this.getVersion().contains("> 1.11")) {
            if (serverVersion.contains("1.17") || serverVersion.contains("1.16") || serverVersion.contains("1.15") || serverVersion
                    .contains("1.14") || serverVersion.contains("1.13") || serverVersion.contains("1.12") || serverVersion.contains(
                    "1.11")) {
                return true;
            }
        }
        if (this.getVersion().contains("1.12")) {
            if (serverVersion.contains("1.12")) {
                return true;
            }
        }
        if (this.getVersion().contains("1.13") && serverVersion.contains("1.13")) {
            return true;
        }

        if (this.getVersion().contains("> 1.13")) {
            return Material.matchMaterial("CAVE_AIR") != null;
        }
        if (this.getVersion().contains("< 1.13")) {
            return Material.matchMaterial("CAVE_AIR") == null;
        }


        return false;

    }

    public boolean isVersionSpecific(String serverVersion) {
        if (this.version.isEmpty()) {
            return false;
        }

        if (this.getVersion().equals("1.17")) {
            if (serverVersion.contains("v1_17")) {
                return true;
            }
        }

        if (this.getVersion().equals("1.16")) {
            if (serverVersion.contains("v1_16")) {
                return true;
            }

        }
        if (this.getVersion().equals("1.15")) {
            if (serverVersion.contains("v1_15_R1")) {
                return true;
            } else {
                return serverVersion.contains("v1_15_R2");
            }
        }
        if (this.getVersion().equals("1.14.4")) {
            return serverVersion.contains("v1_14_R4");
        }


        if (this.getVersion().equals("1.14.3")) {
            return serverVersion.contains("v1_14_R3");
        }
        if (serverVersion.contains("v1_17") && this.getVersion().contains("1.17")) {
            return true;
        }
        if (serverVersion.contains("v1_16") && this.getVersion().contains("1.16")) {
            return true;
        }
        if (serverVersion.contains("v1_15") && this.getVersion().contains("1.15")) {
            return true;
        }
        if (serverVersion.contains("v1_14") && this.getVersion().contains("1.14")) {
            return true;
        }
        if (serverVersion.contains("v1_13") && this.getVersion().contains("1.13")) {
            return true;
        }
        if (serverVersion.contains("v1_12") && this.getVersion().contains("1.12")) {
            return true;
        }
        if (serverVersion.contains("v1_11") && this.getVersion().contains("1.11")) {
            return true;
        }
        if (serverVersion.contains("v1_10") && this.getVersion().contains("1.10")) {
            return true;
        }
        if (serverVersion.contains("v1_9") && this.getVersion().contains("1.9")) {
            return true;
        }
        return serverVersion.contains("v1_8") && this.getVersion().contains("1.8");
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public int getProtId() {
        return protId;
    }

    public void setProtId(int protId) {
        this.protId = protId;
    }

    public String getCommand() {

        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void toggleProtection() {

        this.enabled = !this.enabled;

    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean isList) {
        this.isList = isList;
    }

    public boolean remTxtSet(String value, CommandSender sender) {
        for (String v : this.getTxtSet()) {

            if (v.equalsIgnoreCase(value.trim())) {
                sender.sendMessage(ChatColor.GREEN + "Successfully removed " + value + " from " + this.name());
                this.getTxtSet().remove(value.trim());

                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + value + " was not in the list of items for " + this.name() + " please make sure you spelled it correctly! ");
        return false;
    }

    public boolean setTxtValue(String value, CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Successfully updated " + this.name() + " to: " + value);
        this.txtValue = value;
        return true;
    }

    public boolean addTxtSet(String value, CommandSender sender) {

        if (sender != null) {
            sender.sendMessage(ChatColor.GREEN + "Successfully added " + value + " to " + this.name());
        }
        this.getTxtSet().add(value.trim());


        return true;
    }

    public boolean addIntValue(int value, CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Successfully set the value of " + this.name() + " to " + value);
        this.intValue = value;
        return true;
    }

    public boolean validate(String value, CommandSender sender) {

        if (this == Protections.AlsoPreventHeadInside) {
            return addTxtSet(value, sender);
        }

        if (this == Protections.BookAuthorWhitelist) {
            OfflinePlayer op = IllegalStack.getPlugin().getServer().getOfflinePlayer(value);
            if (op == null || !op.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "Sorry! " + value + " does not appear to have ever joined the server!");
                return false;
            }
            return addTxtSet(value, sender);
        }

        if (this == Protections.CustomEnchantOverride) {

            if (!value.contains(".")) {
                sender.sendMessage(ChatColor.RED + "Command Usage:   /istack  value add CustomEnchantOverride <ENCHANTMENT.LEVEL>");

                return false;
            }
            String[] val = value.split("\\.");
            if (val.length < 2) {
                sender.sendMessage(ChatColor.RED + "Command Usage:   /istack  value add CustomEnchantOverride <ENCHANTMENT.LEVEL>");

                return false;
            }

            Enchantment enc = Enchantment.getByName(val[0]);
            if (enc == null) {
                String vals = "";
                for (Enchantment en : Enchantment.values()) {
                    vals = vals + en.getName() + ", ";
                }

                sender.sendMessage(ChatColor.RED + "You must specify a valid minecraft enchantment.   Valid enchants are: " + ChatColor.DARK_GRAY + vals);
                return false;
            }

            try {
                Integer.parseInt(val[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "You must specify number as a valid level..   " + val[1] + " is not a number!");
                return false;
            }

            return addTxtSet(value, sender);
        }
        if (this == Protections.ValidCharset) {

            try {
                if (Charset.forName(value.trim()).newEncoder().canEncode(ChatColor.stripColor(value))) {
                    return setTxtValue(value.trim(), sender);
                } else {
                    LOGGER.error("Could not encode? {}", value);
                }

            } catch (IllegalCharsetNameException ex) {
                sender.sendMessage(ChatColor.RED + "Sorry! " + value + " does not appear to be a valid charset!  For a list of valid character sets please see: https://docs.oracle.com/javase/7/docs/api/java/nio/charset/CharsetEncoder.html");
                return false;

            } catch (UnsupportedCharsetException ex) {
                sender.sendMessage(ChatColor.RED + "Sorry! " + value + " does not appear be a supported charset!  For a list of valid character sets please see: https://docs.oracle.com/javase/7/docs/api/java/nio/charset/CharsetEncoder.html");
                return false;
            }

        }
		/*
		if(this == Protections.FishingKickMessage || this == Protections.FishModKickMessage || this == Protections.SwimExploitMessage) {

			return setTxtValue(value,sender);
		}
		*/
        if (this == Protections.ItemNamesToRemove || this == Protections.ItemLoresToRemove) {
            return addTxtSet(ChatColor.translateAlternateColorCodes('&', value), sender);
        }

        if (this == Protections.EnchantedItemWhitelist) {

            return addTxtSet(value, sender);

        }
        if (this == Protections.NotifyInsteadOfBlockExploits) {
            Protections p = Protections.getProtection(value.trim());
            if (p != null) {
                return addTxtSet(value, sender);
            }

            StringBuilder vals = new StringBuilder();
            for (Protections pa : Protections.values()) {
                if (pa.findValue().contains("ENABLED") && pa.relevant) //pa.isRelevantToVersion(IllegalStack.getVersion()))
                {
                    vals.append(pa.name()).append(" ,");
                }
            }
            sender.sendMessage(ChatColor.DARK_RED + "Sorry! " + value + " does not appear to be a valid Protection name, please use one of these values: " + ChatColor.GRAY + vals);
            return false;
        }

        if (this == Protections.ResetSpawnersOfType || this == Protections.NetherWhiteList || this == Protections.EndWhiteList) {
            EntityType et = null;
            StringBuilder types = new StringBuilder();
            for (EntityType e : EntityType.values()) {
                if (e.name().equalsIgnoreCase(value)) {
                    return addTxtSet(value, sender);
                }
                types.append(e.name()).append(", ");
            }

            sender.sendMessage(ChatColor.DARK_RED + "Sorry! " + value + " does not appear to be a valid Entity Type!");
            sender.sendMessage(ChatColor.DARK_AQUA + "Valid Entity Types Are: " + ChatColor.GRAY + types);
            return false;
        }
        if (this == Protections.AllowStack || this == Protections.MinecartBlockWhiteList || this == Protections.AllowStackForGroup || this == Protections.RemoveItemTypes) {

            if (this == Protections.AllowStackForGroup) {
                if (value.equals("*")) {
                    return addTxtSet(value, sender);
                }
            }
            Material m = Material.matchMaterial(value);
            if (m == null) {

                int id = -1;
                int data = 0;

                if (value.contains(":")) {
                    String[] magicNumber = value.split(":");
                    try {
                        id = Integer.parseInt(magicNumber[0]);
                        data = Integer.parseInt(magicNumber[1]);
                        return addTxtSet(value, sender);
                    } catch (NumberFormatException ignored) {
                        sender.sendMessage(ChatColor.DARK_RED + "When using data values, you must use the numeric value for both the itemid and the data.. EG:  397:3");
                        return false;
                    }


                }

                sender.sendMessage(ChatColor.DARK_RED + "Sorry! " + value + " does not appear to be a valid Item Type!");
                return false;
            }

            return addTxtSet(value, sender);
        }

        if (this == Protections.OnlyFunctionInWorlds || this == Protections.DisableInWorlds || this == Protections.ExcludeNetherWorldFromHeightCheck) {
            World w = IllegalStack.getPlugin().getServer().getWorld(value.trim());
            if (w == null) {
                sender.sendMessage(ChatColor.DARK_RED + "Sorry! " + value + " does not appear to be a valid world name!");
                return false;
            }
            return addTxtSet(value, sender);
        }
        if (this == Protections.LimitNumberOfPages || this == Protections.NetherYLevel || this == Protections.VillagerRestockTime || this == Protections.ZombieVillagerTransformChance || this == Protections.PageCountThreshold || this == MaxFishAllowedBeforeKick || this == MaxFishToNotifyStaffThenBlock || this == AboveNetherDamageDelay || this == AboveNetherDamageAmount) {
            try {
                int intCheck = Integer.parseInt(value.trim());
                if (intCheck < 0) {
                    sender.sendMessage(ChatColor.RED + "Sorry! the value of this protection can NOT be less than zero.");
                    return false;
                }
                if (this == AboveNetherDamageAmount) {
                	if(intCheck < 1) {
                		sender.sendMessage(ChatColor.DARK_RED + "The minimum value for this protection must be greater than 1.");
                		return false;
                	}
                }
                if (this == AboveNetherDamageDelay) {
                	if(intCheck < 1) {
                		sender.sendMessage(ChatColor.DARK_RED + "The minimum value for this protection must be greater than 1 second.");
                		return false;
                	}
                }
                if (this == Protections.ZombieVillagerTransformChance) {
                    if (intCheck < 0 || intCheck > 100) {
                        sender.sendMessage(ChatColor.DARK_RED + "Sorry! the value of this protection must be between 1 and 100.");
                        return false;
                    }
                }


                return addIntValue(intCheck, sender);

            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "Sorry! " + value + " must be a whole number for " + this.name());
                return false;
            }

        }
        LOGGER.error("Protection {} had no validation steps.. Unable to verify user input: please report to dNiym.", this.name());
        return false;
    }

    public HashMap<String, Boolean> getLoreNameList() {
        HashMap<String, Boolean> target = new HashMap<>();
        if (this == Protections.RemoveItemsMatchingName) {
            for (String s : Protections.ItemLoresToRemove.getTxtSet()) {
                target.put(s, true);
            }
            for (String s : Protections.ItemNamesToRemove.getTxtSet()) {
                target.put(s, false);
            }
        }
        return target;
    }

    public HashSet<String> getTxtSet() {
        return txtSet;
    }

    public void setTxtSet(HashSet<String> txtSet) {
        this.txtSet = txtSet;
    }

    public boolean isWhitelisted(String name) {
        return isWhitelisted(name, null);
    }

    public boolean isWhitelisted(String name, Player player) {

        if (this == Protections.RemoveItemTypes && player != null) {
            if (player.hasPermission("illegalstack.removeitemsoftypebypass")) {
                return true;
            }
        }

        if (this == Protections.AllowStack && player != null) {
            if (player.hasPermission("illegalstack.overstack") || player.isOp()) {
                for (String s : Protections.AllowStackForGroup.getTxtSet()) {
                    if (s.contains("*")) {
                        return true;
                    }
                }
            }
        }

        for (String s : this.txtSet) {
            if (s.equalsIgnoreCase(name.trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean loreNameMatch(ItemMeta itemMeta) {
        HashMap<String, Boolean> target = Protections.RemoveItemsMatchingName.getLoreNameList();
        boolean found = false;
        for (String s : target.keySet()) {

            if (target.get(s) && itemMeta.hasLore()) //lore matching
            {
                for (String line : itemMeta.getLore()) {
                    if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                        if (line.equals(s)) {
                            found = true;
                        }
                    } else if (ChatColor.stripColor(line).contains(s)) {
                        found = true;
                    }
                }


            } else {
                if (Protections.NameLoreStrictMatchMode.isEnabled()) {
                    if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(s)) {
                        found = true;
                    }
                } else if (itemMeta.hasDisplayName() && ChatColor.stripColor(itemMeta.getDisplayName()).contains(s)) {
                    found = true;
                }
            }
        }
        return found;
    }

    public boolean isWhitelisted(Material type) {
        for (String s : this.getTxtSet()) {
            Material m = Material.matchMaterial(s);
            if (m != null && type == m) {
                return true;
            }

            if (m == null && type.name().contains(s.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhitelisted(ItemStack is) {

        if (this == Protections.RemoveItemTypes && IllegalStack.hasIds()) {  //check for magic number type values
            int id = -1;
            int data = 0;

            for (String s : this.getTxtSet()) {
                if (s.contains(":")) {
                    String[] splStr = s.split(":");
                    try {
                        id = Integer.parseInt(splStr[0]);
                        data = Integer.parseInt(splStr[1]);
                    } catch (NumberFormatException ignored) {

                    }
                }

            }
            if (id == is.getType().getId() && data == is.getDurability()) {
                return true;
            }

        }
        if (this == Protections.RemoveItemTypes && this.nukeApples && !is
                .getEnchantments()
                .isEmpty() && is.getType() == Material.matchMaterial("GOLDEN_APPLE")) {
            return true;
        }

        if (this != Protections.RemoveItemTypes && IllegalStack.isHasMagicPlugin() && MagicHook.isMagicItem(is)) {
            return true;
        }

        for (String s : this.getTxtSet()) {
            Material m = Material.matchMaterial(s);
            if (m != null && is.getType() == m) {
                return true;
            }
            if (is.serialize().toString().equalsIgnoreCase(s)) {
                return true;
            }
        }


        return false;
    }


    public boolean isThirdPartyInventory(InventoryView inv) {

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("CraftingStore") != null) {
            if (inv.getTopInventory().getHolder() instanceof CraftingStoreInventoryHolder) {
                return true;
            }

        }

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("DynamicShop") != null) {
            String name = "TRADE_TITLE";
            if (inv.getTitle().equals(name)) {
                return true;
            }
        }

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("ShopGUIPlus") != null) {
            return inv.getTopInventory().getHolder() instanceof OpenGui;
        }

        return false;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public HashSet<Protections> getChildren() {
        HashSet<Protections> children = new HashSet<>();
        for (Protections p : Protections.values()) {
            if (p.getParentId() == this.protId) {
                children.add(p);
            }
        }

        return children;
    }

    public boolean isAllowedEnchant(Enchantment en, int lvl) {


        for (String s : this.getTxtSet()) {
            String[] val = s.split("\\.");
            if (val.length < 2) {
                LOGGER.error(
                        "Unable to translate an enchantment/level from the {} path please check the config.yml!",
                        this.getConfigPath()
                );
                return false;
            }
            Enchantment ench = Enchantment.getByName(val[0]);
            int level = Integer.parseInt(val[1]);
            if (ench == null) {
                LOGGER.error(
                        "Unable to locate enchantment: {} please check your config.yml at section: {} and verify that you are using a valid enchantment.",
                        val[0],
                        this.getConfigPath()
                );
                return false;
            }

            if (en != ench) //not a overridden enchantment
            {
                continue;
            }

            //level higher than override.
            return lvl <= level;

            //otherwise enchant is good
        }
        return false;
    }

    public boolean isNukeApples() {
        return nukeApples;
    }

    public void setNukeApples(boolean nukeApples) {
        this.nukeApples = nukeApples;
    }

    public boolean isThirdPartyObject(Entity entity) {

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("JetsMinions") != null) {
            boolean minion = MinionAPI.isMinion(entity);
            if (!minion) {
                return false;
            }
        }
        return false;
    }


}
