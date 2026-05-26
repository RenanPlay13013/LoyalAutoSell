package net.loyalnetwork.loyalAutoSell.scheduler;

import net.loyalnetwork.loyalAutoSell.cache.ItemCache;
import net.loyalnetwork.loyalAutoSell.processor.SellProcessor;
import net.loyalnetwork.loyalAutoSell.tracker.DirtyTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SellScheduler {

    private static final long INTERVAL = 1L;

    private final JavaPlugin plugin;
    private final DirtyTracker dirtyTracker;
    private final SellProcessor processor;
    private final ItemCache itemCache;

    public SellScheduler(JavaPlugin plugin, DirtyTracker dirtyTracker, SellProcessor processor, ItemCache itemCache) {
        this.plugin = plugin;
        this.dirtyTracker = dirtyTracker;
        this.processor = processor;
        this.itemCache = itemCache;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            var worlds = itemCache.getEnabledWorlds();

            for (var iterator = dirtyTracker.entrySet().iterator(); iterator.hasNext();) {
                var entry = iterator.next();
                UUID uuid = entry.getKey();
                long marked = entry.getValue();

                if (now - marked < itemCache.getCooldownMs()) continue;

                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !worlds.contains(player.getWorld().getName())) {
                    iterator.remove();
                    continue;
                }

                ItemStack[] contents = player.getInventory().getContents();
                iterator.remove();
                processor.processAsync(uuid, contents);
            }
        }, INTERVAL, INTERVAL);
    }
}
