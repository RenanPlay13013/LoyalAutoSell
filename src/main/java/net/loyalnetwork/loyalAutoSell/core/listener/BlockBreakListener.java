package net.loyalnetwork.loyalAutoSell.core.listener;


import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.core.api.AutoSellAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {
    private final AutoSellAPI autoSellAPI;

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!autoSellAPI.canSell(player)) return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = event.getBlock().getDrops(tool, player);

        if (drops.isEmpty()) {
            return;
        }
        event.setDropItems(false);

        for (ItemStack drop : drops) {
            autoSellAPI.queueSell(player, drop);
        }
    }
}
