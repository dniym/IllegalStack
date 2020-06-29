package me.dniym;

import me.dniym.commands.iStackCommand;
import me.dniym.enums.Msg;
import me.dniym.enums.Protections;
import me.dniym.listeners.*;
import me.dniym.timers.fTimer;
import me.dniym.timers.sTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IllegalStack extends JavaPlugin {

    private static IllegalStack plugin;
    private static Plugin ProCosmetics = null;
    private static boolean hasProtocolLib = false;
    private static boolean hasAttribAPI = false;
    private static boolean nbtAPI = false;
    private static boolean SlimeFun = false;
    private static boolean EpicRename = false;
    private static boolean ClueScrolls = false;
    private static boolean Spigot = false;
    private static boolean blockMetaData = false;
    private static boolean hasFactionGUI = false;
    private static boolean SmartInv = false;
    private static boolean SavageFac = false;
    private static boolean CMI = false;
    private static boolean hasMCMMO = false;
    private static boolean hasChestedAnimals = false;
    private static boolean hasMagicPlugin = false;
    private static String version = "";
    private int ScanTimer = 0;
    private int SignTimer = 0;
//	private static NMSEntityVillager nmsTrader= null;

    public static IllegalStack getPlugin() {
        return plugin;
    }

    public void setPlugin(IllegalStack plugin) {
        IllegalStack.plugin = plugin;
    }

    public static boolean isSpigot() {
        return Spigot;
    }

    public static boolean isCMI() {
        return CMI;
    }

    public static void setCMI(boolean cMI) {
        CMI = cMI;
    }

    public static void ReloadConfig(Boolean wasCommand) {
        if (!wasCommand)
            IllegalStack.getPlugin().writeConfig();

        IllegalStack.getPlugin().updateConfig();
        IllegalStack.getPlugin().loadConfig();
        IllegalStack.getPlugin().loadMsgs();
        StartupPlugin();

    }

    private static void StartupPlugin() {

        if (IllegalStack.getPlugin().getServer().getPluginManager().getPlugin("Factions") != null) {
            try {
                Class.forName("com.massivecraft.factions.shade.stefvanschie.inventoryframework.Gui");
                System.out.println("[IllegalStack] Detected SaberFactions gui object, whitelisting items found inside.");
                setHasFactionGUI(true);
            } catch (ClassNotFoundException ignored) {

            }
        }
        
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            System.out.println("[IllegalStack] - Server is NOT spigot, disabling chat components.");
        }

        if (plugin.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled())
                System.out.println("[IllegalStack] - CMI was detected on your server, IllegalStack will block the ability to nest shulkers while shift+right clicking a shulker in your inventory!");
            else
                System.out.println("[IllegalStack] - CMI was detected on your server, however BlockCMIShulkerStacking is set to FALSE in your config, so players can use CMI to put shulkers inside shulkers!   To enable this protection add BlockCMIShulkerStacking: true to your config.yml.");
        }


        if (fListener.getInstance() == null)
            plugin.getServer().getPluginManager().registerEvents(new fListener(plugin), plugin);
        if (Protections.RemoveOverstackedItems.isEnabled()) {
            if (plugin.ScanTimer == 0)
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new fTimer(plugin), 10, 10);

        } else {
            if (plugin.ScanTimer != 0)
                plugin.getServer().getScheduler().cancelTask(plugin.ScanTimer);
        }


        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.getInstance().is18()) {
            if (plugin.SignTimer == 0)
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new sTimer(), 10, 10);
        } else {
            if (plugin.SignTimer != 0)
                plugin.getServer().getScheduler().cancelTask(plugin.SignTimer);
        }

        if (fListener.getInstance().is114() && Protections.VillagerTradeCheesing.isEnabled()) {
            System.out.println("Blocking 1.14 villager trade cheesing.");
            plugin.getServer().getPluginManager().registerEvents(new Listener114(), plugin);

            System.out.println("ZombieVillagerTransformChance is set to " + Protections.ZombieVillagerTransformChance.getIntValue() + " *** Only really matters if the difficulty is set to HARD ***");

        }

    }

    public static boolean isEpicRename() {
        return EpicRename;
    }

    public static void setEpicRename(boolean epicRename) {
        EpicRename = epicRename;
    }

    public static boolean isSlimeFun() {
        return SlimeFun;
    }

    public static void setSlimeFun(boolean slimeFun) {
        SlimeFun = slimeFun;
    }

    public static String getVersion() {
        return version;
    }

    public static boolean isNbtAPI() {
        return nbtAPI;
    }

    public static void setNbtAPI(boolean nbtAPI) {
        IllegalStack.nbtAPI = nbtAPI;
    }

    public static boolean isHasAttribAPI() {
        return hasAttribAPI;
    }

    public static void setHasAttribAPI(boolean hasAttribAPI) {
        IllegalStack.hasAttribAPI = hasAttribAPI;
    }

    public static boolean isClueScrolls() {
        return ClueScrolls;
    }

    public static void setClueScrolls(boolean clueScrolls) {
        ClueScrolls = clueScrolls;
    }

    public static Plugin getProCosmetics() {
        return ProCosmetics;
    }

    public static void setProCosmetics(Plugin proCosmetics) {
        ProCosmetics = proCosmetics;
    }

    public static boolean isHasMCMMO() {
        return hasMCMMO;
    }

    public static void setHasMCMMO(boolean hasMCMMO) {
        IllegalStack.hasMCMMO = hasMCMMO;
    }

    public static boolean hasFactionGUI() {
        return hasFactionGUI;
    }

    public static void setHasFactionGUI(boolean hasFactionGUI) {
        IllegalStack.hasFactionGUI = hasFactionGUI;
    }

    public static boolean hasChestedAnimals() {
        return hasChestedAnimals;
    }

    public static boolean hasProtocolLib() {
        return hasProtocolLib;
    }

    public static void setHasProtocolLib(boolean hasProtocolLib) {
        IllegalStack.hasProtocolLib = hasProtocolLib;
    }

    public static boolean isHasMagicPlugin() {
        return hasMagicPlugin;
    }

    public static void setHasMagicPlugin(boolean hasMagicPlugin) {
        IllegalStack.hasMagicPlugin = hasMagicPlugin;
    }

    public static boolean isBlockMetaData() {
        return blockMetaData;
    }

    public static void setBlockMetaData(boolean blockMetaData) {
        IllegalStack.blockMetaData = blockMetaData;
    }

    @Override
    public void onEnable() {

//    	 new EntityRegistry(this);
        this.setPlugin(this);
        setVersion();
        loadConfig();
        updateConfig();
        loadMsgs();
        this.getCommand("istack").setExecutor(new iStackCommand());


        ProCosmetics = this.getServer().getPluginManager().getPlugin("ProCosmetics");

        if (this.getServer().getPluginManager().getPlugin("EpicRename") != null)
            EpicRename = true;

        if (this.getServer().getPluginManager().getPlugin("ClueScrolls") != null)
            setClueScrolls(true);

        setHasChestedAnimals();
        try {
            Class.forName("com.github.stefvanschie.inventoryframework.Gui");
            System.out.println("Found a plugin using InventoryFramework, these items will be whitelisted while inside their GUI.");
            setHasFactionGUI(true);
        } catch (ClassNotFoundException ignored) {
        }
    	/*
		if(this.getServer().getPluginManager().getPlugin("Factions") != null) {
			try {
				if(Class.forName("com.massivecraft.factions.shade.stefvanschie.inventoryframework.Gui") != null)
				{
					System.out.println("[IllegalStack] Detected SaberFactions gui object, whitelisting items found inside.");
					setHasFactionGUI(true);
				}
			} catch (ClassNotFoundException e) {

			}
		}
		*/
        if (Protections.FixIllegalEnchantmentLevels.isEnabled()) {
            ItemStack test = new ItemStack(Material.DIAMOND_AXE, 1);
            ItemMeta im = test.getItemMeta();
            try {
                im.hasAttributeModifiers();
                setHasAttribAPI(true);

            } catch (NoSuchMethodError e) {
                setHasAttribAPI(false);
            }
        }
        
        try {
            Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            System.out.println("[IllegalStack] Chat Components found! Enabling clickable commands in /istack");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            System.out.println("[IllegalStack] - Spigot chat components NOT found! disabling chat components.");
        }
        
        try {
        	if (Class.forName("fr.minuskube.inv.content.InventoryProvider") != null) 
        		setSavageFac(true);
        		
        } catch (ClassNotFoundException e) {
        	
        }
        try {
        	if (Class.forName("fr.minuskube.inv.SmartInventory") != null) 
        		setSmartInv(true);
        
        	
        } catch (ClassNotFoundException ignored) {
        	
        }

        try {
            Class.forName("org.bukkit.inventory.meta.BlockDataMeta");
            blockMetaData = true;

        } catch (ClassNotFoundException e) {
            System.out.println("[IllegalStack] - Spigot chat components NOT found! disabling chat components.");
        }

        if (this.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled())
                System.out.println("[IllegalStack] - CMI was detected on your server, IllegalStack will block the ability to nest shulkers while shift+right clicking a shulker in your inventory!");
            else
                System.out.println("[IllegalStack] - CMI was detected on your server, however BlockCMIShulkerStacking is set to FALSE in your config, so players can use CMI to put shulkers inside shulkers!   To enable this protection add BlockCMIShulkerStacking: true to your config.yml.");
        }

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null && Protections.BlockBadItemsFromCreativeTab.isEnabled()) {
            System.out.println("ProtocolLib was detected, creative inventory exploit detection enabled.  NOTE*  This protection ONLY needs to be turned on if you have regular (non op) players with access to /gmc");
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            new pLisbListener(this, version);
        }

        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") == null && Protections.DisableChestsOnMobs.isEnabled()) {

            System.out.println(ChatColor.RED + "WARNING ProtocolLib NOT FOUND!!!! and DisableChestsOnMobs protection is turned on.. It may still be possible for players to dupe using horses/donkeys on your server using a hacked client.  It is highly recommended that you install ProtocolLib for optimal protection!");

        } else if (Protections.DisableChestsOnMobs.isEnabled()) {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            new pLisbListener(this, version);
            setHasProtocolLib(true);
        }

        this.getServer().getPluginManager().registerEvents(new fListener(this), this);

        if (!fListener.getInstance().is18())
            this.getServer().getPluginManager().registerEvents(new protListener(this), this);

        if (Protections.RemoveOverstackedItems.isEnabled() || Protections.PreventVibratingBlocks.isEnabled())
            ScanTimer = getServer().getScheduler().scheduleSyncRepeatingTask(this, new fTimer(this), Protections.ItemScanTimer.getIntValue(), Protections.ItemScanTimer.getIntValue());

        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.getInstance().is18())
            SignTimer = getServer().getScheduler().scheduleSyncRepeatingTask(this, new sTimer(), 10, 10);

        if ((fListener.getInstance().is115() || fListener.getInstance().is114())) {
            System.out.println("Blocking 1.14 villager trade cheesing.");
            this.getServer().getPluginManager().registerEvents(new Listener114(), this);
        }

        if (this.getServer().getPluginManager().getPlugin("Magic") != null)
            setHasMagicPlugin(true);

        if (this.getServer().getPluginManager().getPlugin("mcMMO") != null) {
            this.getServer().getPluginManager().registerEvents(new mcMMOListener(this), this);
            setHasMCMMO(true);
        }

        setNbtAPI((this.getServer().getPluginManager().getPlugin("NBTAPI") != null));
        if (!isNbtAPI() && Protections.DestroyInvalidShulkers.isEnabled()) {
            System.out.println("[IllegalStack] - Warning:  DestroyInvalidShulkers protection is turned on but this protection REQUIRES the use of NBTApi 2.0+ please install this plugin if you wish to use this feature: https://www.spigotmc.org/resources/nbt-api.7939/");
        }
        if (this.getServer().getPluginManager().getPlugin("Slimefun") != null) {
            SlimeFun = true;
        }
    }

    private void setHasChestedAnimals() {

        try {
            Class.forName("org.bukkit.entity.ChestedHorse");
            hasChestedAnimals = true;
        } catch (ClassNotFoundException e) {
        }
    }

    private void updateConfig() {
        File conf = new File("plugins/IllegalStack/config.yml");
        FileConfiguration config = this.getConfig();
        HashMap<String, Object> added = new HashMap<>();

        for (Protections p : Protections.values()) {
            if (!p.getCommand().isEmpty())
                continue;
            if (p.isRelevantToVersion(getVersion())) {
                if (config.getString(p.getConfigPath()) == null) {
                    if (p.getConfigValue() instanceof Boolean) {
                        p.setEnabled((Boolean) p.getDefaultValue());
                    }
                    added.put(p.getConfigPath(), p.getDefaultValue());
                }
                for (Protections child : p.getChildren())
                    if (config.getString(child.getConfigPath()) == null) {

                        if (child.getConfigValue() instanceof Boolean) {
                            child.setEnabled((Boolean) child.getDefaultValue());
                        }
                        added.put(child.getConfigPath(), child.getDefaultValue());
                    }
            } else if (config.getString(p.getConfigPath()) != null) {
                if (p.getVersion().isEmpty()) //handling a child node
                {
                    Protections parent = Protections.getParentByChild(p);
                    if (parent == null || !parent.isRelevantToVersion(getVersion())) {
                        added.put(p.getConfigPath(), null);
                    }
                } else {
                    added.put(p.getConfigPath(), null);
                    for (Protections child : p.getChildren())
                        added.put(child.getConfigPath(), null);
                }
            }
        }

        boolean updated = false;
        if (config.getString("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad") != null)
            config.set("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad", null);
        if (config.getString("Exploits.Enchants.AllowOpBypass") != null)
            config.set("Exploits.Enchants.AllowOpBypass", null);

        for (String key : added.keySet()) {
            if (added.get(key) == null) {
                System.out.println("[IllegalStack] - found an old configuration value that is no longer used or not relevant to your server version: " + key + " it has been removed from the config.");
                config.set(key, null);
            } else {
                System.out.println("[IllegalStack] - Found a missing configuration value " + key + " it has been added to the config with the default value of: " + added.get(key));
                config.set(key, added.get(key));
                Protections p = Protections.findByConfig(key);
                if (p != null && (added.get(key) instanceof Boolean))
                    p.setEnabled((Boolean) added.get(key));


            }
            updated = true;
        }

        if (updated) {
            try {
                config.save(conf);
            } catch (IOException e1) {

                // TODO Auto-generated catch block
                System.out.println("failed to update config!");
                e1.printStackTrace();
            }
        }
    }

    private void loadMsgs() {

        YamlConfiguration fc = new YamlConfiguration();
        try {
            fc.load("plugins/IllegalStack/messages.yml");
        } catch (FileNotFoundException e) {
            System.out.println("Creating messages.yml");
            File conf = new File("plugins/IllegalStack/messages.yml");
            for (Msg m : Msg.values()) {
                if (fc.getString(m.name()) == null) {
                    System.out.println("Adding default message to messages.yml for: " + m.name());
                    fc.set(m.name(), m.getConfigVal());
                }
            }
            try {
                fc.save(conf);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (fc != null) {
            boolean update = false;
            for (Msg m : Msg.values()) {
                if (m == Msg.PistonHeadRemoval && fc.getString(m.name()).contains("remoging"))
                    fc.set(m.name(), null);
                if (fc.getString(m.name()) == null) {
                    System.out.println(m.name() + " Was missing from messages.yml, adding it with the default value");
                    fc.set(m.name(), m.getConfigVal());
                    update = true;

                }

                m.setValue(fc.getString(m.name()));
            }
            if (update) {

                try {
                    fc.save("plugins/IllegalStack/messages.yml");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private void loadConfig() {

        try {
            plugin.getConfig().load("plugins/IllegalStack/config.yml");
        } catch (FileNotFoundException e) {
            File conf = new File("plugins/IllegalStack/config.yml");
            System.out.println("[IllegalStack]:  Warning Configuration File Not Found! /plugins/IllegalStack/config.yml - Creating a new one with default values.");
            FileConfiguration config = this.getConfig();
            try {
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("failed to save config?");
                e1.printStackTrace();
            }
            writeConfig();
        } catch (IOException | InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (getConfig().getString("ConfigVersion") == null) { //server is running an old config version, should probably save it.
            File conf = new File("plugins/IllegalStack/config.yml");
            File confOld = new File("plugins/IllegalStack/config.OLD");
            FileConfiguration config = this.getConfig();

            conf.renameTo(confOld);

            try {
                config.set("Settings", null);
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("[IllegalStack] - You are upgrading from an older version, I apologize but we need to regenerate your Config.yml file.  Your old settings have been saved in /plugins/IllegalStack/config.OLD");
            try {
                conf.createNewFile();
                writeConfig();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Protections.update();

        StringBuilder whitelisted = new StringBuilder();


        for (String s : Protections.NetherWhiteList.getTxtSet())
            whitelisted.append(" ").append(s);

        if (whitelisted.length() > 0) {
            String mode = "allowed";
            if (!Protections.NetherWhiteListMode.isEnabled())
                mode = "NOT allowed";

            System.out.println("The following entity's (by name) are " + mode + " to travel through nether portals: " + whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.EndWhiteList.getTxtSet())//this.getConfig().getStringList("Settings.EndWhiteList"))
            whitelisted.append(" ").append(s);

        if (whitelisted.length() > 0) {
            String mode = "allowed";
            if (!Protections.EndWhiteListMode.isEnabled())
                mode = "NOT allowed";
            System.out.println("The following entity's (by name) are " + mode + " to travel through end portals: " + whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.NotifyInsteadOfBlockExploits.getTxtSet())
            whitelisted.append(" ").append(s);

        if (whitelisted.length() > 0)
            System.out.println(ChatColor.RED + " WARNING: IllegalStack will NOT block but will notify for the following exploits: " + whitelisted);

        whitelisted = new StringBuilder();
        for (String s : Protections.DisableInWorlds.getTxtSet()) {//this.getConfig().getStringList("Settings.DisableInWorlds")) {
            World w = this.getServer().getWorld(s);
            if (w == null)
                System.out.println("[IllegalStack] was told to ignore all checks in the world " + s + " in the configuration but this does not appear to be a loaded world...  Please double check your config.yml!");

            whitelisted.append(" ").append(s);

        }
        if (whitelisted.length() > 0)
            System.out.println(ChatColor.RED + " WARNING: IllegalStack will NOT do any exploit checks in the following worlds: " + whitelisted);

        whitelisted = new StringBuilder();
        for (String s : Protections.RemoveItemTypes.getTxtSet()) {
            Material m = null;
            if (s.equalsIgnoreCase("ENCHANTED_GOLDEN_APPLE")) {
                m = Material.matchMaterial("GOLDEN_APPLE");
                Protections.RemoveItemTypes.setNukeApples(true);
                System.out.println("[IllegalStack] - Now removing enchanted golden apples.");
                continue;
            } else {
                m = Material.matchMaterial(s);
            }
            if (m != null)
                whitelisted.append(s).append(" ");
            else
                System.out.println("[IllegalStack] warning unable to find a material matching: " + s + " make sure it is a valid minecraft material type!");
        }
        if (whitelisted.length() > 0)
            System.out.println(ChatColor.RED + "The following materials will be removed from player inventories when found: " + whitelisted);

        whitelisted = new StringBuilder();

        for (String s : Protections.AllowStack.getTxtSet())//this.getConfig().getStringList("Settings.AllowStack"))
        {
            Material m = Material.matchMaterial(s);
            if (m != null)
                whitelisted.append(s).append(" ");
            else
                System.out.println("[IllegalStack] warning unable to find a material matching: " + s + " make sure it is a valid minecraft material type!");

        }
        if (whitelisted.length() > 0)
            System.out.println(ChatColor.RED + "The following materials are allowed to have stacks larger than the vanilla size: " + whitelisted);

        whitelisted = new StringBuilder();
        for (String s : Protections.BookAuthorWhitelist.getTxtSet())
            whitelisted.append(s).append(" ");
        if (whitelisted.length() > 0)
            System.out.println("[IllegalStack] - The following players may create books that do NOT match the specified charset (change in config!): " + whitelisted);

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemNamesToRemove.getTxtSet())
            whitelisted.append(" ").append(s);
        if (whitelisted.length() > 0)
            System.out.println("[IllegalStack] - Items matching the following names will be removed from players inventories: " + whitelisted);

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemLoresToRemove.getTxtSet())
            whitelisted.append(" ").append(s);
        if (whitelisted.length() > 0)
            System.out.println("[IllegalStack] - Items matching the following lore will be removed from players inventories: " + whitelisted);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTask(ScanTimer);
        getServer().getScheduler().cancelTask(SignTimer);
        getServer().getScheduler().cancelTasks(this);
        writeConfig();
    }

    private void writeConfig() {

        File conf = new File("plugins/IllegalStack/config.yml");
        FileConfiguration config = this.getConfig();

        HashMap<Protections, Boolean> relevant = Protections.getRelevantTo(getVersion());

		/* Debugging only, generates FULL config values.
		relevant.clear();
		for(Protections p: Protections.values())
			relevant.put(p,true);
		*/

        config.set("ConfigVersion", "2.0");
        for (Protections p : relevant.keySet()) {
            {
                if (relevant.get(p)) //relevant to this version, check if it exists.
                {
                    if (config.getString(p.getConfigPath()) == null) {
                        config.set(p.getConfigPath(), p.getDefaultValue());
                        System.out.println("[IllegalStack] - Found a missing protection from your configuration: " + p.name() + " it has been added with a default value of: " + p.getDefaultValue());
                    }

                    if (p.isList()) {
                        ArrayList<String> list = new ArrayList<>();
                        for (String s : (HashSet<String>) p.getConfigValue())
                            list.add(s);
                        config.set(p.getConfigPath(), list);
                        continue;
                    }

                    if (p.getConfigValue() instanceof String)
                        config.set(p.getConfigPath(), p.getConfigValue());
                    else if (p.getConfigValue() instanceof Integer)
                        config.set(p.getConfigPath(), p.getConfigValue());
                    else
                        config.set(p.getConfigPath(), p.isEnabled());

                    if ((p == Protections.DestroyBadSignsonChunkLoad || p == Protections.RemoveExistingGlitchedMinecarts) && p.isEnabled()) {
                        p.setEnabled(false);
                        System.out.println(ChatColor.RED + "[IllegalStack] - Automatically disabling " + p.getConfigPath() + " this setting should never be left on indefinitely.");
                        config.set(p.getConfigPath(), false);
                    }
                } else {                //not relevant check to see if it should be deleted.
                    if (config.getString(p.getConfigPath()) != null) {
                        config.set(p.getConfigPath(), null);
                        System.out.println("[IllegalStack] - Found a protection in the config that was not relevant to your server version: " + p.name() + " (" + p.getVersion() + ") it has been removed.");
                    }
                }
            }
        }
        try {
            config.save(conf);
        } catch (IOException e1) {

            System.out.println("failed to save config?");
            e1.printStackTrace();
        }
    }

    private void setVersion() {

        String version = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        version = getString(version);
        IllegalStack.version = version;
    }

    @NotNull
    public static String getString(String version) {
        if (version.equalsIgnoreCase("v1_14_R1")) {

            version = IllegalStack.getPlugin().getServer().getVersion().split(" ")[2];

            version = version.replace(")", "");
            version = version.replace(".", "_");
            String[] ver = version.split("_");
            version = "v" + ver[0] + "_" + ver[1] + "_R" + ver[2];
        }
        return version;
    }

	public static boolean hasSmartInv() {
		return SmartInv;
	}

	public static void setSmartInv(boolean smartInv) {
		SmartInv = smartInv;
	}

	public static boolean hasSavageFac() {
		return SavageFac;
	}

	public static void setSavageFac(boolean savageFac) {
		SavageFac = savageFac;
	}

}