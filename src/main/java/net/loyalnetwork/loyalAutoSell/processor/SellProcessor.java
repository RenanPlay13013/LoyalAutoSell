package net.loyalnetwork.loyalAutoSell.processor;

import net.loyalnetwork.loyalAutoSell.cache.ItemCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellProcessor {

    private final JavaPlugin plugin;
    private final ItemCache itemCache;
    private final ExcellentEconomyAPI economyApi;

    public SellProcessor(JavaPlugin plugin, ItemCache itemCache, ExcellentEconomyAPI economyApi) {
        this.plugin = plugin;
        this.itemCache = itemCache;
        this.economyApi = economyApi;
    }

    public void processAsync(UUID uuid, ItemStack[] contents) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Map<String, Double> earnings = new HashMap<>();
            BitSet slotsToClear = new BitSet(contents.length);

            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];
                if (item == null || item.getType() == Material.AIR) continue;

                var data = itemCache.get(item.getType());
                if (data == null) continue;

                double total = data.price() * item.getAmount();
                earnings.merge(data.currency(), total, Double::sum);
                slotsToClear.set(i);
            }

            if (earnings.isEmpty()) return;

            Bukkit.getScheduler().runTask(plugin, () -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                for (int i = slotsToClear.nextSetBit(0); i >= 0; i = slotsToClear.nextSetBit(i + 1)) {
                    player.getInventory().setItem(i, null);
                }

                for (Map.Entry<String, Double> entry : earnings.entrySet()) {
                    economyApi.depositAsync(uuid, entry.getKey(), entry.getValue());
                }
            });
        });
    }
}
