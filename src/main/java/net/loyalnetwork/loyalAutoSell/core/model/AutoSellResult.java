package net.loyalnetwork.loyalAutoSell.core.model;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public record AutoSellResult(
        UUID playerId,
        Map<String, Double> amounts,
        long itemsSold
) {
    public AutoSellResult {
        amounts = Collections.unmodifiableMap(amounts);
    }

    public double get(String economy) {
        return amounts().getOrDefault(economy, 0D);
    }

    public boolean isEmpty() {
        return amounts().isEmpty();
    }

    public boolean hasSold() {
        return !amounts().isEmpty();
    }

    public static AutoSellResult empty() {
        return new AutoSellResult(
                null,
                Collections.emptyMap(),
                0
        );
    }
}
