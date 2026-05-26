package net.loyalnetwork.loyalAutoSell.core.model;

import org.bukkit.Material;

import java.util.Locale;
import java.util.Objects;

public record SellEntry(
        Material material,
        double price,
        String economy
) {
    public SellEntry {
        Objects.requireNonNull(material, "material");
        Objects.requireNonNull(economy, "economy");

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        economy = economy.toLowerCase(Locale.ROOT);
    }
}
