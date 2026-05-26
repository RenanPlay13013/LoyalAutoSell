package net.loyalnetwork.loyalAutoSell.core.task;

import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import net.loyalnetwork.loyalAutoSell.core.service.AutoSellService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class AutoSellFlushTask extends BukkitRunnable {
    private final AutoSellService autoSellService;

    @Override
    public void run() {
        for (Player player : autoSellService.getPendingPlayers()) {
            AutoSellResult result = autoSellService.flush(player);
        }
    }
}
