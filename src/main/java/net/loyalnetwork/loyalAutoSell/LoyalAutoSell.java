package net.loyalnetwork.loyalAutoSell;

import net.loyalnetwork.loyalAutoSell.cache.ItemCache;
import net.loyalnetwork.loyalAutoSell.command.AutoSellCommand;
import net.loyalnetwork.loyalAutoSell.command.AutoSellTabCompleter;
import net.loyalnetwork.loyalAutoSell.listener.AutoSellListener;
import net.loyalnetwork.loyalAutoSell.processor.SellProcessor;
import net.loyalnetwork.loyalAutoSell.scheduler.SellScheduler;
import net.loyalnetwork.loyalAutoSell.tracker.DirtyTracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

public final class LoyalAutoSell extends JavaPlugin {

    private ItemCache itemCache;
    private DirtyTracker dirtyTracker;
    private SellProcessor processor;
    private SellScheduler scheduler;
    private ExcellentEconomyAPI economyApi;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("ExcellentEconomy") == null) {
            getServer().getLogger().severe("ExcellentEconomy não encontrado!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<ExcellentEconomyAPI> provider =
                Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);
        if (provider != null) {
            economyApi = provider.getProvider();
        }

        itemCache = new ItemCache();
        itemCache.load(getConfig());

        dirtyTracker = new DirtyTracker();
        processor = new SellProcessor(this, itemCache, economyApi);
        scheduler = new SellScheduler(this, dirtyTracker, processor, itemCache);

        getServer().getPluginManager().registerEvents(new AutoSellListener(itemCache, dirtyTracker), this);
        getCommand("autosell").setExecutor(new AutoSellCommand(this));
        getCommand("autosell").setTabCompleter(new AutoSellTabCompleter());

        scheduler.start();

        getLogger().info("LoyalAutoSell ativado com " + itemCache.size() + " itens configurados.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if (itemCache != null) itemCache.clear();
        if (dirtyTracker != null) dirtyTracker.clear();
    }

    public void reload() {
        reloadConfig();
        itemCache.load(getConfig());
    }

    public ItemCache getItemCache() {
        return itemCache;
    }
}
