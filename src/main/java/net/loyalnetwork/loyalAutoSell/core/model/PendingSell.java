package net.loyalnetwork.loyalAutoSell.core.model;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PendingSell {
    private final UUID playerId;

    private final Map<String, Double> balances = new HashMap<>();

    private long itemsSold;

    public void add(String economy, double amount) {
        balances.merge(economy, amount, Double::sum);
    }

    public double get(String economy) {
        return balances.getOrDefault(economy, 0D);
    }

    public Map<String, Double> balances() {
        return Collections.unmodifiableMap(balances);
    }

    public void addItems(long amount) {
        this.itemsSold += amount;
    }

    public long itemsSold() {
        return itemsSold;
    }

    public UUID playerId() {
        return playerId;
    }

    public boolean isEmpty() {
        return balances.isEmpty();
    }

    public void clear() {
        balances.clear();
        itemsSold = 0;
    }
}
