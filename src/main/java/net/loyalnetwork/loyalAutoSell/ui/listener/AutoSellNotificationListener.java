package net.loyalnetwork.loyalAutoSell.ui.listener;

import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.core.event.AutoSellCompleteEvent;
import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import net.loyalnetwork.loyalAutoSell.ui.formatter.SellResultFormatter;
import net.loyalnetwork.loyalAutoSell.ui.model.FormattedSellMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AutoSellNotificationListener implements Listener {
    private final SellResultFormatter formatter;

    @EventHandler
    public void onAutoSell(AutoSellCompleteEvent event) {
        Player player = event.getPlayer();

        FormattedSellMessage message = formatter.format(event.getResult(), player);

        player.sendMessage(message.chat());

        player.sendActionBar(message.actionBar());
    }
}
