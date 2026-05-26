package net.loyalnetwork.loyalAutoSell.core.manager;

import net.loyalnetwork.loyalAutoSell.LoyalAutoSell;
import org.bukkit.entity.Player;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.ExcellentCurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    private final ExcellentEconomyAPI api;
    private final Map<String, ExcellentCurrency> currencies = new ConcurrentHashMap<>();

    public EconomyManager(LoyalAutoSell plugin) {
        this.api = plugin.getEconomyApi();

        if (this.api == null) {
            throw new IllegalStateException("ExcellentEconomyAPI is not available.");
        }
    }

    public void deposit(
            Player player,
            String currencyId,
            double amount
    ) {

        if (player == null || currencyId == null || amount <= 0) {
            return;
        }


        ExcellentCurrency currency = currencies.computeIfAbsent(
                currencyId.toLowerCase(),
                api::getCurrency
        );

        if (currency == null) {
            return;
        }

        api.depositAsync(player.getUniqueId(), currency.getId(), amount);
    }

}