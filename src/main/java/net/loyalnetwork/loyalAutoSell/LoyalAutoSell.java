package net.loyalnetwork.loyalAutoSell;

import lombok.Getter;
import net.loyalnetwork.loyalAutoSell.core.api.AutoSellAPI;
import net.loyalnetwork.loyalAutoSell.core.command.AutoSellCommand;
import net.loyalnetwork.loyalAutoSell.core.command.AutoSellTabCompleter;
import net.loyalnetwork.loyalAutoSell.core.config.SellConfigLoader;
import net.loyalnetwork.loyalAutoSell.core.listener.BlockBreakListener;
import net.loyalnetwork.loyalAutoSell.core.manager.EconomyManager;
import net.loyalnetwork.loyalAutoSell.core.manager.SellManager;
import net.loyalnetwork.loyalAutoSell.core.manager.WorldManager;
import net.loyalnetwork.loyalAutoSell.core.placeholder.AutoSellExpansion;
import net.loyalnetwork.loyalAutoSell.core.service.AutoSellService;
import net.loyalnetwork.loyalAutoSell.core.task.AutoSellFlushTask;
import net.loyalnetwork.loyalAutoSell.ui.config.MessagesConfig;
import net.loyalnetwork.loyalAutoSell.ui.config.UIConfigLoader;
import net.loyalnetwork.loyalAutoSell.ui.formatter.SellResultFormatter;
import net.loyalnetwork.loyalAutoSell.ui.listener.AutoSellNotificationListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

public final class LoyalAutoSell extends JavaPlugin {

    @Getter private SellManager sellManager;
    @Getter private AutoSellService autoSellService;
    @Getter private EconomyManager economyManager;
    @Getter private WorldManager worldManager;
    @Getter private SellResultFormatter sellResultFormatter;

    private ExcellentEconomyAPI api;
    private SellConfigLoader configLoader;
    private MessagesConfig messagesConfig;
    private AutoSellExpansion expansion;
    private UIConfigLoader uiConfigLoader;

    @Override
    public void onEnable() {
        if (!setupEconomy()) return;

        initManagers();
        loadConfig();
        setupPlaceholderAPI();
        registerListeners();
        scheduleTasks();
        registerAPI();
        registerCommands();

        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        if (autoSellService != null) {
            autoSellService.flushAll();
        }
    }

    public ExcellentEconomyAPI getEconomyApi() {
        return api;
    }

    // -------------------------------------------------------------------------
    // Private setup methods
    // -------------------------------------------------------------------------

    private boolean setupEconomy() {
        RegisteredServiceProvider<ExcellentEconomyAPI> provider =
                Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);

        if (provider == null) {
            getLogger().severe("ExcellentEconomy provider not found. Disabling!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }

        api = provider.getProvider();
        return true;
    }

    private void initManagers() {
        saveDefaultConfig();
        this.sellManager = new SellManager();
        this.economyManager = new EconomyManager(this);
        this.worldManager = new WorldManager();
        this.autoSellService = new AutoSellService(sellManager, economyManager, worldManager);
        this.sellResultFormatter = new SellResultFormatter();

        worldManager.load(getConfig().getStringList("enabled-worlds"));
    }

    private void loadConfig() {
        this.configLoader = new SellConfigLoader(sellManager);
        configLoader.load(getConfig());

        messagesConfig = new MessagesConfig(this);
        messagesConfig.load();

        uiConfigLoader = new UIConfigLoader();

    }

    private void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.expansion = new AutoSellExpansion(autoSellService, sellManager);
            expansion.register();
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(
                new BlockBreakListener(autoSellService), this
        );

        Bukkit.getPluginManager().registerEvents(new AutoSellNotificationListener(sellResultFormatter, uiConfigLoader), this);
    }

    private void scheduleTasks() {
        new AutoSellFlushTask(autoSellService).runTaskTimer(this, 20L, 20L);
    }

    private void registerAPI() {
        Bukkit.getServicesManager().register(
                AutoSellAPI.class, autoSellService, this, ServicePriority.Normal
        );
    }

    private void registerCommands() {
        getCommand("autosell").setExecutor(
                new AutoSellCommand(this, configLoader, worldManager, messagesConfig, uiConfigLoader, expansion)
        );
        getCommand("autosell").setTabCompleter(new AutoSellTabCompleter());
    }
}