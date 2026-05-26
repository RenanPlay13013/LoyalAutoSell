package net.loyalnetwork.loyalAutoSell.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.loyalnetwork.loyalAutoSell.cache.ItemCache;
import net.loyalnetwork.loyalAutoSell.tracker.DirtyTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AutoSellListener implements Listener {

    private final ItemCache itemCache;
    private final DirtyTracker dirtyTracker;

    public AutoSellListener(ItemCache itemCache, DirtyTracker dirtyTracker) {
        this.itemCache = itemCache;
        this.dirtyTracker = dirtyTracker;
    }

    @EventHandler
    public void onSlotChange(PlayerInventorySlotChangeEvent event) {
        if (!itemCache.getEnabledWorlds().contains(event.getPlayer().getWorld().getName())) return;
        dirtyTracker.mark(event.getPlayer().getUniqueId());
    }
}
