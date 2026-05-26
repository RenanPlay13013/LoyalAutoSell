package net.loyalnetwork.loyalAutoSell.listener;

import io.papermc.paper.event.player.PlayerPickItemEvent;
import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.LoyalAutoSell;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

@RequiredArgsConstructor
public class AutoSellListener implements Listener {
    private final LoyalAutoSell plugin;
    private final ExcellentEconomyAPI economyApi;

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();

        String world = player.getWorld().getName();

        if (!plugin.getConfig().getStringList("enabled-worlds").contains(world)) return;

        ItemStack item = event.getItem().getItemStack();

        if (item == null || item.getType() == Material.AIR) return;

        String path = "items." + item.getType().name();

        if (!plugin.getConfig().isSet(path)) return;

        double price = plugin.getConfig().getDouble(path + ".price");

        String economy = plugin.getConfig().getString(path + ".economy");

        if (economy == null || economy.isBlank()) return;

        int amount = item.getAmount();
        double total = price * amount;

        Bukkit.getScheduler().runTask(plugin, () -> {
           event.getItem().remove();
        });

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            economyApi.depositAsync(player.getUniqueId(), economy, total);
        });
    }
}
