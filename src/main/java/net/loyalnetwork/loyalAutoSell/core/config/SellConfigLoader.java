package net.loyalnetwork.loyalAutoSell.core.config;

import net.loyalnetwork.loyalAutoSell.core.manager.SellManager;
import net.loyalnetwork.loyalAutoSell.core.model.SellEntry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SellConfigLoader {

    private final SellManager sellManager;

    public SellConfigLoader(SellManager sellManager) {
        this.sellManager = sellManager;
    }

    public void load(FileConfiguration config) {

        sellManager.clear();

        ConfigurationSection section =
                config.getConfigurationSection("sellables");

        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {

            Material material = Material.matchMaterial(key);

            if (material == null) {
                continue;
            }

            String path = "sellables." + key;

            double price = config.getDouble(path + ".price");

            String economy = config.getString(
                    path + ".economy",
                    "money"
            );

            SellEntry entry = new SellEntry(
                    material,
                    price,
                    economy
            );

            sellManager.register(entry);
        }
    }

}