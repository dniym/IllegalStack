package main.java.me.dniym;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import main.java.me.dniym.commands.IllegalStackCommand;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.enums.ServerVersion;
import main.java.me.dniym.listeners.CreativeInvPacketListeners;
import main.java.me.dniym.listeners.Listener113;
import main.java.me.dniym.listeners.Listener114;
import main.java.me.dniym.listeners.Listener116;
import main.java.me.dniym.listeners.ProtectionListener;
import main.java.me.dniym.listeners.UseEntityPacketListener;
import main.java.me.dniym.listeners.fListener;
import main.java.me.dniym.listeners.mcMMOListener;
import main.java.me.dniym.timers.fTimer;
import main.java.me.dniym.timers.sTimer;
import main.java.me.dniym.timers.syncTimer;
import main.java.me.dniym.utils.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
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

    private static final Logger LOGGER = LogManager.getLogger("IllegalStack/" + IllegalStack.class.getSimpleName());

    private static IllegalStack plugin;
    private static Plugin ProCosmetics = null;
    private static boolean isHybridEnvironment = false;
    private static boolean isPaperServer = false;
    private static boolean isFoliaServer = false;
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
    private static boolean hasTraders = false;
    private static boolean hasChestedAnimals = false;
    private static boolean hasContainers = false;
    private static boolean hasShulkers = false;
    private static boolean hasAsyncScheduler = false;
    private static boolean hasElytra = false;
    private static boolean hasMagicPlugin = false;
    private static boolean disablePaperShulkerCheck = false;
    private static boolean hasUnbreakable = false;
    private static boolean hasStorage = false;
    private static boolean hasIds = false;
    private static Material lbBlock = null;

    private static String version = "";
    private Scheduler.ScheduledTask ScanTimer = null;
    private Scheduler.ScheduledTask SignTimer = null;
    private Scheduler.ScheduledTask syncTimer = null;
//	private static NMSEntityVillager nmsTrader= null;

    private ServerVersion serverVersion;

    public static IllegalStack getPlugin() {
        return plugin;
    }

    public void setPlugin(IllegalStack plugin) {
        IllegalStack.plugin = plugin;
    }

    public static boolean isIsHybridEnvironment() {
        return isHybridEnvironment;
    }

    public static boolean isPaperServer() {
        return isPaperServer;
    }

    public static boolean isSpigot() {
        return Spigot;
    }

    public static boolean isFoliaServer() {
        return isFoliaServer;
    }

    public static boolean isCMI() {
        return CMI;
    }

    public static void setCMI(boolean cMI) {
        CMI = cMI;
    }

    @Override
    public void onLoad() {
        // Load PacketEventsAPI
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .debug(false);
        PacketEvents.getAPI().load();
    }

    public static void ReloadConfig(Boolean wasCommand) {
        if (!wasCommand) {
            IllegalStack.getPlugin().writeConfig();
        }

        IllegalStack.getPlugin().updateConfig();
        IllegalStack.getPlugin().loadConfig();
        IllegalStack.getPlugin().loadMsgs();
        StartupPlugin();

    }

    private static void checkForHybridEnvironment() {
        try {
            Class.forName("io.izzel.arclight.i18n.ArclightConfig");
            isHybridEnvironment = true;
            LOGGER.info("Server is an ArcLight hybrid environment, enabling hybrid scheduler checks.");
        } catch (ClassNotFoundException e) {
            isHybridEnvironment = false;
            LOGGER.info("Server is NOT an ArcLight hybrid environment, continuing as normal.");
        }

        if (!isIsHybridEnvironment()) {
            try {
                Class.forName("net.minecraftforge.fml.ModList");
                isHybridEnvironment = true;
                LOGGER.info("Server is a Forge hybrid environment, enabling hybrid scheduler checks.");

            } catch (ClassNotFoundException e) {
                isHybridEnvironment = false;
                LOGGER.info("Server is NOT a Forge hybrid environment, continuing as normal.");
            }
        }

        if (!isIsHybridEnvironment()) {
            try {
                Class.forName("net.fabricmc.loader.api.FabricLoader");
                isHybridEnvironment = true;
                LOGGER.info("Server is a Fabric hybrid environment, enabling hybrid scheduler checks.");
            } catch (ClassNotFoundException e) {
                isHybridEnvironment = false;
                LOGGER.info("Server is NOT a Fabric hybrid environment, continuing as normal.");
            }
        }
    }

    private static void checkForPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            isPaperServer = true;
            LOGGER.info("Server is a Paper server, enabling Paper features.");
        } catch (ClassNotFoundException e) {
            isPaperServer = false;
            LOGGER.info("Server is NOT a Paper server, continuing as normal.");
        }
    }

    private static void checkForFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFoliaServer = true;
            LOGGER.info("Server has Folia components, enabling Folia features.");
        } catch (ClassNotFoundException e) {
            isFoliaServer = false;
            LOGGER.info("Server does NOT have Folia components, continuing as normal.");
        }
    }

    private static void StartupPlugin() {

        checkForHybridEnvironment();
        checkForPaperServer();
        checkForFoliaServer();

        try {
            Class.forName("org.spigotmc.SpigotConfig");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("Server is NOT spigot, disabling chat components.");
        }

        if (plugin.getServer().getPluginManager().getPlugin("Logblock") != null) {
            File conf = new File(Bukkit.getPluginManager().getPlugin("Logblock").getDataFolder(), "config.yml");
            final FileConfiguration lbconfig = YamlConfiguration.loadConfiguration(conf);

            if (lbconfig != null) {
                setLbBlock(Material.matchMaterial(lbconfig.getString("tools.toolblock.item")));
                LOGGER.info("Logblock plugin found, if blacklisted the toolblock item: " + getLbBlock().name() + " will not be removed.");
            }
        }
        if (plugin.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled()) {
                LOGGER.info(
                        "CMI was detected on your server, IllegalStack will block the ability to nest shulkers while shift+right clicking a shulker in your inventory!");
            } else {
                LOGGER.info(
                        "CMI was detected on your server, however BlockCMIShulkerStacking is set to FALSE in your config, so players can use CMI to put shulkers inside shulkers!   To enable this protection add BlockCMIShulkerStacking: true to your config.yml.");
            }
        }

        Protections.runReflectionChecks();

        if (fListener.getInstance() == null) {
            plugin.getServer().getPluginManager().registerEvents(new fListener(plugin), plugin);
        }
        if (Protections.RemoveOverstackedItems.isEnabled()) {
            if (plugin.ScanTimer == null) {
                Scheduler.runTaskTimerAsynchronously(plugin, new fTimer(plugin), 10, 10);
                Scheduler.runTaskTimer(plugin, new syncTimer(plugin), 10l, 10l);
            }

        } else {
            if (plugin.ScanTimer != null) {
                plugin.ScanTimer.cancel();
            }
            if (plugin.syncTimer != null) {
                plugin.syncTimer.cancel();
            }
        }


        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.is18()) {
            if (plugin.SignTimer == null) {
                Scheduler.runTaskTimerAsynchronously(plugin, new sTimer(), 10, 10);
            }
        } else {
            if (plugin.SignTimer != null) {
                plugin.SignTimer.cancel();
            }
        }

        if (fListener.getInstance().isAtLeast113()) {
            new Listener113(IllegalStack.getPlugin());
        }

        if (fListener.getInstance().isAtLeast114()) {
            new Listener114(IllegalStack.getPlugin());
            LOGGER.info(
                    "ZombieVillagerTransformChance is set to {} *** Only really matters if the difficulty is set to HARD ***",
                    Protections.ZombieVillagerTransformChance.getIntValue()
            );
        }

        if ((fListener.getInstance().getIs116()) || fListener.getInstance().isIs117()) {
            new Listener116(IllegalStack.getPlugin());
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

    public static boolean hasContainers() {
        return hasContainers;
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

    public static boolean hasTraders() {
        return hasTraders;
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

    public static boolean hasIds() {
        return hasIds;
    }

    public static boolean hasAsyncScheduler() {
        return hasAsyncScheduler;
    }

    public static boolean hasShulkers() {
        return hasShulkers;
    }

    public static boolean hasElytra() {
        return hasElytra;
    }

    public static boolean hasUnbreakable() {
        return hasUnbreakable;
    }

    public static boolean isDisablePaperShulkerCheck() {
        return disablePaperShulkerCheck;
    }

    public static void setDisablePaperShulkerCheck(boolean disablePaperShulkerCheck) {
        IllegalStack.disablePaperShulkerCheck = disablePaperShulkerCheck;
    }

    public static boolean hasStorage() {
        return hasStorage;
    }

    @Override
    public void onEnable() {
        // Register PacketEvents listeners
        LOGGER.info("Registering PacketEvents listeners...");
        PacketEvents.getAPI().getEventManager().registerListeners(new CreativeInvPacketListeners(this));
        PacketEvents.getAPI().getEventManager().registerListeners(new UseEntityPacketListener(this));
        PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();
        packetEventsAPI.init();
        LOGGER.info("PacketEvents API {} has been initialized!", PacketEvents.getAPI().getVersion());

//    	 new EntityRegistry(this);
        this.setPlugin(this);
        setVersion();
        loadConfig();
        updateConfig();
        loadMsgs();
        checkForHybridEnvironment();
        checkForPaperServer();
        checkForFoliaServer();
        IllegalStackCommand illegalStackCommand = new IllegalStackCommand();
        this.getCommand("istack").setExecutor(illegalStackCommand);
        this.getCommand("istack").setTabCompleter(illegalStackCommand);


        ProCosmetics = this.getServer().getPluginManager().getPlugin("ProCosmetics");


        if (this.getServer().getPluginManager().getPlugin("EpicRename") != null) {
            EpicRename = true;
        }

        if (this.getServer().getPluginManager().getPlugin("ClueScrolls") != null) {
            setClueScrolls(true);
        }

        setHasChestedAnimals();
        setHasContainers();
        setHasTraders();
        setHasShulkers();
        setHasAsyncScheduler();
        setHasElytra();
        setHasUnbreakable();
        setHasStorage();

        try {
            Class.forName("com.github.stefvanschie.inventoryframework.Gui");
            LOGGER.info("Found a plugin using InventoryFramework, these items will be whitelisted while inside their GUI.");
            setHasFactionGUI(true);
        } catch (ClassNotFoundException ignored) {
        }


        ItemStack test = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta im = test.getItemMeta();

        try {
            im.getAttributeModifiers();
            setHasAttribAPI(true);


        } catch (NoSuchMethodError e) {
            setHasAttribAPI(false);

        }


        try {
            Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
            LOGGER.info("Chat Components found! Enabling clickable commands in /istack");
            Spigot = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("Spigot chat components NOT found! disabling chat components.");
        }

        try {
            Class.forName("fr.minuskube.inv.content.InventoryProvider");
            setSavageFac(true);

        } catch (ClassNotFoundException ignored) {

        }
        try {
            Class.forName("fr.minuskube.inv.SmartInventory");
            setSmartInv(true);

        } catch (ClassNotFoundException ignored) {

        }

        try {
            Class.forName("org.bukkit.inventory.meta.BlockDataMeta");
            blockMetaData = true;

        } catch (ClassNotFoundException e) {
            LOGGER.info("Spigot chat components NOT found! disabling chat components.");
        }

        if (plugin.getServer().getPluginManager().getPlugin("Logblock") != null) {
            File conf = new File(Bukkit.getPluginManager().getPlugin("Logblock").getDataFolder(), "config.yml");
            final FileConfiguration lbconfig = YamlConfiguration.loadConfiguration(conf);

            if (lbconfig != null) {
                setLbBlock(Material.matchMaterial(lbconfig.getString("tools.toolblock.item")));
                LOGGER.info("Logblock plugin found, if blacklisted the toolblock item: " + getLbBlock().name() + " will not be removed.");
            }
        }

        if (this.getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI = true;
            if (Protections.BlockCMIShulkerStacking.isEnabled()) {
                LOGGER.info(
                        "CMI was detected on your server, IllegalStack will block the ability to nest shulkers while shift+right clicking a shulker in your inventory!");
            } else {
                LOGGER.info(
                        "CMI was detected on your server, however BlockCMIShulkerStacking is set to FALSE in your config, so players can use CMI to put shulkers inside shulkers!   To enable this protection add BlockCMIShulkerStacking: true to your config.yml.");
            }
        }

        this.getServer().getPluginManager().registerEvents(new fListener(this), this);

        if (!fListener.is18()) {
            this.getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        }

        if (Protections.RemoveOverstackedItems.isEnabled() || Protections.PreventVibratingBlocks.isEnabled()) {
            ScanTimer = Scheduler.runTaskTimerAsynchronously(
                    this,
                    new fTimer(this),
                    Protections.ItemScanTimer.getIntValue(),
                    Protections.ItemScanTimer.getIntValue()
            );
            syncTimer = Scheduler.runTaskTimer(this, new syncTimer(this), 10, 10);

        }

        if (Protections.RemoveBooksNotMatchingCharset.isEnabled() && !fListener.getInstance().is113() && !fListener.is18()) {
            SignTimer = Scheduler.runTaskTimerAsynchronously(this, new sTimer(), 10, 10);
        }
        if ((fListener.getInstance().isAtLeast113())) {
            new Listener113(this);
        }
        if (fListener.getInstance().isAtLeast114()) {
            new Listener114(this);
            LOGGER.info(
                    "ZombieVillagerTransformChance is set to {} *** Only really matters if the difficulty is set to HARD ***",
                    Protections.ZombieVillagerTransformChance.getIntValue()
            );
        }

        if (this.getServer().getPluginManager().getPlugin("Magic") != null) {
            setHasMagicPlugin(true);
        }

        if (this.getServer().getPluginManager().getPlugin("mcMMO") != null) {
            this.getServer().getPluginManager().registerEvents(new mcMMOListener(this), this);
            setHasMCMMO(true);
        }

        setNbtAPI((this.getServer().getPluginManager().getPlugin("NBTAPI") != null));
        if (!isNbtAPI() && Protections.DestroyInvalidShulkers.isEnabled()) {
            LOGGER.warn(
                    "DestroyInvalidShulkers protection is turned on but this protection REQUIRES the use of NBTApi 2.0+ please install this plugin if you wish to use this feature: https://www.spigotmc.org/resources/nbt-api.7939/");
        }
        if (this.getServer().getPluginManager().getPlugin("Slimefun") != null) {
            SlimeFun = true;
        }

        if ((fListener.getInstance().getIs116())) {
            new Listener116(IllegalStack.getPlugin());
        }
    }

    private void setHasTraders() {
        try {
            Class.forName("import org.bukkit.entity.TraderLlama");
            hasTraders = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasStorage() {
        Inventory inv = Bukkit.getServer().createInventory(null, 9);
        try {
            inv.getStorageContents();
            hasStorage = true;
        } catch (NoSuchMethodError ignored) {
        }
    }

    private void setHasUnbreakable() {
        ItemStack is = new ItemStack(Material.DIRT);
        ItemMeta im = is.getItemMeta();
        try {
            im.setUnbreakable(false);
            hasUnbreakable = true;

        } catch (NoSuchMethodError ignored) {
        }

    }

    private void setHasElytra() {

        Material m = Material.matchMaterial("Elytra");
        if (m != null) {
            hasElytra = true;
        }

    }

    private void setHasIds() {
        ItemStack is = new ItemStack(Material.BEDROCK);
        try {
            is.getType().getId();
            hasIds = true;
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void setHasShulkers() {
        try {

            Class.forName("org.bukkit.block.ShulkerBox");
            hasShulkers = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasContainers() {
        try {
            Class.forName("org.bukkit.block.Container");
            hasContainers = true;

        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasAsyncScheduler() {
        try {
            Class.forName("org.bukkit.Server.getAsyncScheduler");
            hasAsyncScheduler = true;
        } catch (ClassNotFoundException ignored) {

        }
    }

    private void setHasChestedAnimals() {

        try {
            Class.forName("org.bukkit.entity.ChestedHorse");
            hasChestedAnimals = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    private void updateConfig() {
        File conf = new File(getDataFolder(), "config.yml");
        FileConfiguration config = this.getConfig();
        HashMap<String, Object> added = new HashMap<>();

        for (Protections p : Protections.values()) {
            if (!p.getCommand().isEmpty()) {
                continue;
            }
            if (p.isRelevantToVersion(getVersion())) {
                if (config.getString(p.getConfigPath()) == null) {
                    if (p.getConfigValue() instanceof Boolean) {
                        p.setEnabled((Boolean) p.getDefaultValue());
                    }
                    added.put(p.getConfigPath(), p.getDefaultValue());
                }
                for (Protections child : p.getChildren()) {
                    if (config.getString(child.getConfigPath()) == null) {

                        if (child.getConfigValue() instanceof Boolean) {
                            child.setEnabled((Boolean) child.getDefaultValue());
                        }
                        added.put(child.getConfigPath(), child.getDefaultValue());
                    }
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
                    for (Protections child : p.getChildren()) {
                        added.put(child.getConfigPath(), null);
                    }
                }
            }
        }

        boolean updated = false;
        if (config.getString("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad") != null) {
            config.set("UserRequested.Obscure.HackedShulker.RemoveOnChunkLoad", null);
        }
        if (config.getString("Exploits.Enchants.AllowOpBypass") != null) {
            config.set("Exploits.Enchants.AllowOpBypass", null);
        }

        for (String key : added.keySet()) {
            if (added.get(key) == null) {
                LOGGER.info(
                        "Found an old configuration value that is no longer used or not relevant to your server version: {} it has been removed from the config.",
                        key
                );
                config.set(key, null);
            } else {
                LOGGER.info(
                        "Found a missing configuration value {} it has been added to the config with the default value of: {}",
                        key,
                        added.get(key)
                );
                config.set(key, added.get(key));
                Protections p = Protections.findByConfig(key);
                if (p != null && (added.get(key) instanceof Boolean)) {
                    p.setEnabled((Boolean) added.get(key));
                }
                if (p == Protections.AlsoPreventHeadInside && Material.matchMaterial("COMPOSTER") != null) {
                    Protections.AlsoPreventHeadInside.addTxtSet("COMPOSTER", null);
                }


            }
            updated = true;
        }

        if (updated) {
            try {
                config.save(conf);
            } catch (IOException e1) {

                // TODO Auto-generated catch block
                LOGGER.error("Failed to update config!", e1);
            }
        }
    }

    private void loadMsgs() {
        File conf = new File(getDataFolder(), "messages.yml");
        YamlConfiguration fc = new YamlConfiguration();
        try {
            fc.load(conf);
        } catch (FileNotFoundException e) {
            LOGGER.info("Creating messages.yml");
            for (Msg m : Msg.values()) {
                if (fc.getString(m.name()) == null) {
                    LOGGER.info("Adding default message to messages.yml for: {}", m.name());
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
                if (fc.getString(m.name()) == null) {
                    LOGGER.info(
                            " {} Was missing from messages.yml, adding it with the default value {}",
                            m.name(),
                            m.getConfigVal()
                    );
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
        File conf = new File(getDataFolder(), "config.yml");
        try {
            plugin.getConfig().load(conf);
        } catch (FileNotFoundException e) {
            LOGGER.error(
                    "Configuration File Not Found! /plugins/IllegalStack/config.yml - Creating a new one with default values.");
            FileConfiguration config = this.getConfig();
            try {
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                LOGGER.error("failed to save config?", e1);
            }
            writeConfig();
        } catch (IOException | InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (getConfig().getString("ConfigVersion") == null) { //server is running an old config version, should probably save it.
            File confOld = new File(getDataFolder(), "config.OLD");
            FileConfiguration config = this.getConfig();

            conf.renameTo(confOld);

            try {
                config.set("Settings", null);
                config.save(conf);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            LOGGER.warn(
                    "You are upgrading from an older version, I apologize but we need to regenerate your Config.yml file.  Your old settings have been saved in /plugins/IllegalStack/config.OLD");
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


        for (String s : Protections.NetherWhiteList.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            String mode = "allowed";
            if (!Protections.NetherWhiteListMode.isEnabled()) {
                mode = "NOT allowed";
            }

            LOGGER.info("The following entity's (by name) are {} to travel through nether portals: {}", mode, whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.EndWhiteList.getTxtSet())//this.getConfig().getStringList("Settings.EndWhiteList"))
        {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            String mode = "allowed";
            if (!Protections.EndWhiteListMode.isEnabled()) {
                mode = "NOT allowed";
            }
            LOGGER.info("The following entity's (by name) are {} to travel through end portals: {}", mode, whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.NotifyInsteadOfBlockExploits.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }

        if (whitelisted.length() > 0) {
            LOGGER.warn(
                    "WARNING: IllegalStack will NOT block but instead, will notify for the following exploits: {}",
                    whitelisted
            );
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.DisableInWorlds.getTxtSet()) {//this.getConfig().getStringList("Settings.DisableInWorlds")) {
            World w = this.getServer().getWorld(s);
            if (w == null) {
                LOGGER.warn(
                        "IllegalStack was told to ignore all checks in the world {} in the configuration but this does not appear to be a loaded world...  Please double check your config.yml!",
                        s
                );
            }

            whitelisted.append(" ").append(s);

        }
        if (whitelisted.length() > 0) {
            LOGGER.warn("IllegalStack will NOT do any exploit checks in the following worlds: {}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.RemoveItemTypes.getTxtSet()) {
            Material m = null;
            if (s.equalsIgnoreCase("ENCHANTED_GOLDEN_APPLE")) {
                m = Material.matchMaterial("GOLDEN_APPLE");
                Protections.RemoveItemTypes.setNukeApples(true);
                LOGGER.info("Now removing enchanted golden apples.");
                continue;
            } else {
                m = Material.matchMaterial(s);
            }
            int id = -1;
            int data = 0;

            if (m != null) {
                whitelisted.append(s).append(" ");
            } else {
                if (s.contains(":")) {
                    String[] splStr = s.split(":");

                    try {
                        id = Integer.parseInt(splStr[0]);
                        data = Integer.parseInt(splStr[1]);
                    } catch (NumberFormatException ignored) {
                    }

                }
                if (id != -1) {
                    whitelisted.append(s).append(" ");
                } else {
                    LOGGER.warn("Unable to find a material matching: {} make sure it is a valid minecraft material type!", s);
                }
            }
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("The following materials will be removed from player inventories when found: {}", whitelisted);
        }

        whitelisted = new StringBuilder();

        for (String s : Protections.AllowStack.getTxtSet())//this.getConfig().getStringList("Settings.AllowStack"))
        {
            Material m = Material.matchMaterial(s);
            if (m != null) {
                whitelisted.append(s).append(" ");
            } else {
                LOGGER.warn("Unable to find a material matching: {} make sure it is a valid minecraft material type!", s);
            }

        }
        if (whitelisted.length() > 0) {
            LOGGER.info("The following materials are allowed to have stacks larger than the vanilla size: {}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.BookAuthorWhitelist.getTxtSet()) {
            whitelisted.append(s).append(" ");
        }
        if (whitelisted.length() > 0) {
            LOGGER.info(
                    "The following players may create books that do NOT match the specified charset (change in config!): {}",
                    whitelisted
            );
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemNamesToRemove.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("Items matching the following names will be removed from players inventories: {}", whitelisted);
        }

        whitelisted = new StringBuilder();
        for (String s : Protections.ItemLoresToRemove.getTxtSet()) {
            whitelisted.append(" ").append(s);
        }
        if (whitelisted.length() > 0) {
            LOGGER.info("Items matching the following lore will be removed from players inventories: {}", whitelisted);
        }
    }

    private static boolean disable = false;

    public static boolean isDisable() {
        return disable;
    }

    @Override
    public void onDisable() {
        // Signal plugin is now disabled
        disable = true;

        // Terminate PacketEvents
        PacketEvents.getAPI().getEventManager().unregisterAllListeners();
        PacketEvents.getAPI().terminate();
        LOGGER.info("PacketEvents has been terminated successfully!");

        // Unregister listeners
        HandlerList.unregisterAll(this);
        LOGGER.info("Listeners have unregistered successfully!");

        // Cancel all tasks
        if (hasAsyncScheduler) {
            getServer().getAsyncScheduler().cancelTasks(this);
        } else if (!isFoliaServer()){
            Bukkit.getScheduler().cancelTasks(this);
        }

        // Save configuration
        writeConfig();
    }

    private void writeConfig() {

        File conf = new File(getDataFolder(), "config.yml");
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

                        if (p == Protections.RemoveOverstackedItems && this.getServer().getPluginManager().getPlugin(
                                "StackableItems") != null) {
                            config.set(p.getConfigPath(), false);
                            LOGGER.warn(
                                    "The StackableItems plugin has been detected on your server, The protection RemoveOverstackedItems has been automatically disabled to prevent item loss, enabling this protection will most definitely remove items as this plugin is known to break the vanilla stack limits.");
                            p.setEnabled(false);
                        } else {
                            config.set(p.getConfigPath(), p.getDefaultValue());
                        }

                        LOGGER.warn(
                                "Found a missing protection from your configuration: {} it has been added with a default value of: {}",
                                p.name(),
                                p.getDefaultValue()
                        );
                    }
                    if (p.isList()) {
                        ArrayList<String> list = new ArrayList<>();
                        for (String s : (HashSet<String>) p.getConfigValue()) {
                            list.add(s);
                        }
                        config.set(p.getConfigPath(), list);
                        continue;
                    }

                    if (p.getConfigValue() instanceof String) {
                        config.set(p.getConfigPath(), p.getConfigValue());
                    } else if (p.getConfigValue() instanceof Integer) {
                        config.set(p.getConfigPath(), p.getConfigValue());
                    } else {
                        config.set(p.getConfigPath(), p.isEnabled());
                    }

                    if ((p == Protections.DestroyBadSignsonChunkLoad || p == Protections.RemoveExistingGlitchedMinecarts) && p.isEnabled()) {
                        p.setEnabled(false);
                        LOGGER.warn("Automatically disabling " + p.getConfigPath() + " this setting should never be left on indefinitely.");
                        config.set(p.getConfigPath(), false);
                    }
                } else {                //not relevant check to see if it should be deleted.
                    if (config.getString(p.getConfigPath()) != null) {
                        config.set(p.getConfigPath(), null);
                        LOGGER.info(
                                "Found a protection in the config that was not relevant to your server version: {} ( {} + ) it has been removed.",
                                p.name(),
                                p.getVersion()
                        );
                    }
                }
            }
        }
        try {
            config.save(conf);
        } catch (IOException e1) {

            LOGGER.error("Failed to save config?", e1);
        }
    }

    private void setVersion() {

        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            version = null;
        }

        if (version != null) {
            version = getString(version);
            IllegalStack.version = version;
        } else {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String bukkitVersion = Bukkit.getServer().getBukkitVersion();
            if (bukkitVersion.contains("1.20.5")) {
                serverVersion = ServerVersion.v1_20_R5;
            } else if (bukkitVersion.contains("1.20.6")) {
                serverVersion = ServerVersion.v1_20_R5;
            } else if (bukkitVersion.contains("1.21")) {
                serverVersion = ServerVersion.v1_21_R1;
            } else {
                serverVersion = ServerVersion.valueOf(packageName.replace("org.bukkit.craftbukkit.", ""));
            }

            IllegalStack.version = serverVersion.getServerVersionName();
        }
    }

    public static Material getLbBlock() {
        return lbBlock;
    }

    public static void setLbBlock(Material lbBlock) {
        IllegalStack.lbBlock = lbBlock;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public static int getMajorServerVersion() {
        int version;

        try {
            version = Integer.parseInt(getVersion().split("_")[1]);
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to process server version!");
            LOGGER.error("Some features may break unexpectedly!");
            LOGGER.error("Report any issues to the developer!");
            return 0;
        }
        return version;
    }

}
