package net.loyalnetwork.loyalAutoSell.core.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.loyalnetwork.loyalAutoSell.core.api.AutoSellAPI;
import net.loyalnetwork.loyalAutoSell.core.manager.SellManager;
import net.loyalnetwork.loyalAutoSell.core.model.PendingSell;
import net.loyalnetwork.loyalAutoSell.core.model.SellEntry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class AutoSellExpansion extends PlaceholderExpansion {

    private final AutoSellAPI autoSellAPI;
    private final SellManager sellManager;

    private final Map<String, String> cache =
            new ConcurrentHashMap<>();

    @Override
    public @NotNull String getIdentifier() {
        return "autosell";
    }

    @Override
    public @NotNull String getAuthor() {
        return "café";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(
            Player player,
            @NotNull String params
    ) {

        if (player == null) {
            return "";
        }

        if (params.equalsIgnoreCase("items")) {

            PendingSell pendingSell =
                    autoSellAPI.getPendingSell(player);

            if (pendingSell == null) {
                return "0";
            }

            return String.valueOf(
                    pendingSell.itemsSold()
            );
        }

        if (params.startsWith("pending_")) {

            String economy = params
                    .substring("pending_".length())
                    .toLowerCase(Locale.ROOT);

            return String.valueOf(
                    autoSellAPI.getPending(player)
                            .getOrDefault(economy, 0D)
            );
        }

        if (params.startsWith("price_")) {

            return getCached(params, () -> {

                SellEntry entry = getEntry(
                        params.substring("price_".length())
                );

                if (entry == null) {
                    return "0";
                }

                return String.valueOf(entry.price());
            });
        }

        if (params.startsWith("currency_")) {

            return getCached(params, () -> {

                SellEntry entry = getEntry(
                        params.substring("currency_".length())
                );

                if (entry == null) {
                    return "";
                }

                return entry.economy();
            });
        }

        return null;
    }

    private SellEntry getEntry(String materialName) {

        Material material = Material.matchMaterial(
                materialName.toUpperCase(Locale.ROOT)
        );

        if (material == null) {
            return null;
        }

        return sellManager.get(material);
    }

    private String getCached(
            String key,
            Supplier<String> supplier
    ) {

        return cache.computeIfAbsent(
                key,
                ignored -> supplier.get()
        );
    }

    public void clearCache() {
        cache.clear();
    }
}