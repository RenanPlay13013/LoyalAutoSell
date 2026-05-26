package net.loyalnetwork.loyalAutoSell.cache;

import net.loyalnetwork.loyalAutoSell.data.SellData;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ItemCache {

    private final ConcurrentHashMap<Material, SellData> cache = new ConcurrentHashMap<>();
    private volatile Set<String> enabledWorlds = Collections.emptySet();
    private volatile long cooldownMs = 150L;

    public void load(FileConfiguration config) {
        cache.clear();
        var section = config.getConfigurationSection("items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Material material = Material.getMaterial(key);
                if (material == null) continue;

                double price = config.getDouble("items." + key + ".price", 0.0);
                String currency = config.getString("items." + key + ".economy");
                if (currency == null || currency.isBlank()) continue;
                if (price <= 0.0) continue;

                cache.put(material, new SellData(price, currency));
            }
        }
        enabledWorlds = Set.copyOf(config.getStringList("enabled-worlds"));
        cooldownMs = config.getLong("cooldown-ms", 150L);
    }

    public SellData get(Material material) {
        return cache.get(material);
    }

    public Set<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    public long getCooldownMs() {
        return cooldownMs;
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
        enabledWorlds = Collections.emptySet();
        cooldownMs = 150L;
    }
}
