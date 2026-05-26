package net.loyalnetwork.loyalAutoSell;

import net.loyalnetwork.loyalAutoSell.command.AutoSellCommand;
import net.loyalnetwork.loyalAutoSell.command.AutoSellTabCompleter;
import net.loyalnetwork.loyalAutoSell.listener.AutoSellListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

public final class LoyalAutoSell extends JavaPlugin {
    private ExcellentEconomyAPI economyApi;
    @Override
    public void onEnable() {

        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("ExcellentEconomy") == null) {
            getServer().getLogger().severe("ExcellentEconomy não encontrado!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<ExcellentEconomyAPI> provider = Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);

        if (provider != null) {
        economyApi = provider.getProvider();
        }

        getServer().getPluginManager().registerEvents(
                new AutoSellListener(this, economyApi),
                this
        );

        getCommand("autosell").setExecutor(new AutoSellCommand(this));
        getCommand("autosell").setTabCompleter(new AutoSellTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
