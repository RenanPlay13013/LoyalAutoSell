package net.loyalnetwork.loyalAutoSell.core.service;

import net.loyalnetwork.loyalAutoSell.core.api.AutoSellAPI;
import net.loyalnetwork.loyalAutoSell.core.event.AutoSellCompleteEvent;
import net.loyalnetwork.loyalAutoSell.core.event.AutoSellProcessEvent;
import net.loyalnetwork.loyalAutoSell.core.manager.WorldManager;
import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import net.loyalnetwork.loyalAutoSell.core.model.PendingSell;
import net.loyalnetwork.loyalAutoSell.core.model.SellEntry;
import net.loyalnetwork.loyalAutoSell.core.manager.EconomyManager;
import net.loyalnetwork.loyalAutoSell.core.manager.SellManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AutoSellService implements AutoSellAPI {

    private final SellManager sellManager;
    private final EconomyManager economyManager;
    private final WorldManager worldManager;

    private final Map<UUID, PendingSell> pendingSells =
            new ConcurrentHashMap<>();

    public AutoSellService(
            SellManager sellManager,
            EconomyManager economyManager,
            WorldManager worldManager
    ) {
        this.sellManager = sellManager;
        this.economyManager = economyManager;
        this.worldManager = worldManager;
    }

    @Override
    public void queueSell(Player player, Material material, long amount) {

        if (player == null || material == null || amount <= 0) {
            return;
        }

        SellEntry entry = sellManager.get(material);

        if (entry == null) {
            return;
        }

        queueSell(player, entry, amount);
    }

    @Override
    public void queueSell(Player player, ItemStack itemStack) {

        if (player == null || itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.AIR) {
            return;
        }

        queueSell(
                player,
                itemStack.getType(),
                itemStack.getAmount()
        );
    }

    @Override
    public void queueSell(Player player, SellEntry entry, long amount) {

        if (player == null || entry == null || amount <= 0) {
            return;
        }

        double total = entry.price() * amount;

        PendingSell pendingSell = pendingSells.computeIfAbsent(
                player.getUniqueId(),
                PendingSell::new
        );

        pendingSell.add(entry.economy(), total);
        pendingSell.addItems(amount);
    }

    @Override
    public AutoSellResult flush(Player player) {

        if (player == null) {
            return AutoSellResult.empty();
        }

        PendingSell pendingSell =
                pendingSells.remove(player.getUniqueId());

        if (pendingSell == null || pendingSell.isEmpty()) {
            return AutoSellResult.empty();
        }

        AutoSellProcessEvent processEvent =
                new AutoSellProcessEvent(
                        player,
                        pendingSell
                );

        Bukkit.getPluginManager().callEvent(
                processEvent
        );

        if (processEvent.isCancelled()) {

            pendingSells.put(
                    player.getUniqueId(),
                    pendingSell
            );

            return AutoSellResult.empty();
        }

        Map<String, Double> soldAmounts =
                new HashMap<>();

        for (Map.Entry<String, Double> entry :
                pendingSell.balances().entrySet()) {

            String economy = entry.getKey();
            double amount = entry.getValue();

            if (amount <= 0) {
                continue;
            }

            economyManager.deposit(
                    player,
                    economy,
                    amount
            );

            soldAmounts.put(
                    economy,
                    amount
            );
        }

        AutoSellResult result =
                new AutoSellResult(
                        player.getUniqueId(),
                        soldAmounts,
                        pendingSell.itemsSold()
                );

        Bukkit.getPluginManager().callEvent(
                new AutoSellCompleteEvent(
                        player,
                        result
                )
        );

        return result;
    }

    public void flushAll() {

        for (UUID uuid : pendingSells.keySet()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            flush(player);
        }
    }

    @Override
    public Map<String, Double> getPending(Player player) {

        if (player == null) {
            return Collections.emptyMap();
        }

        PendingSell pendingSell =
                pendingSells.get(player.getUniqueId());

        if (pendingSell == null) {
            return Collections.emptyMap();
        }

        return pendingSell.balances();
    }

    @Override
    public List<Player> getPendingPlayers() {

        List<Player> players = new ArrayList<>();

        for (UUID uuid : pendingSells.keySet()) {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {
                players.add(player);
            }
        }

        return players;
    }

    @Override
    public boolean canSell(Player player) {
        if (player == null) {
            return false;
        }

        return worldManager.isEnabled(
                player.getWorld().getName()
        );
    }

    @Override
    public PendingSell getPendingSell(Player player) {
        return pendingSells.get(player.getUniqueId());
    }
}