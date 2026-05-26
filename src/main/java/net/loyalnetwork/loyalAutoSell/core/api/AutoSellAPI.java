package net.loyalnetwork.loyalAutoSell.core.api;

import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import net.loyalnetwork.loyalAutoSell.core.model.PendingSell;
import net.loyalnetwork.loyalAutoSell.core.model.SellEntry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface AutoSellAPI {

    void queueSell(Player player, Material material, long amount);

    void queueSell(Player player, ItemStack itemStack);

    void queueSell(Player player, SellEntry entry, long amount);

    AutoSellResult flush(Player player);

    Map<String, Double> getPending(Player player);

    PendingSell getPendingSell(Player player);

    List<Player> getPendingPlayers();

    boolean canSell(Player player);

}