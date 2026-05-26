package net.loyalnetwork.loyalAutoSell.ui.listener;

import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.core.event.AutoSellCompleteEvent;
import net.loyalnetwork.loyalAutoSell.ui.config.UIConfigLoader;
import net.loyalnetwork.loyalAutoSell.ui.formatter.MessageFormatter;
import net.loyalnetwork.loyalAutoSell.ui.formatter.SellResultFormatter;
import net.loyalnetwork.loyalAutoSell.ui.model.NotificationSettings;
import net.loyalnetwork.loyalAutoSell.ui.model.UISettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AutoSellNotificationListener implements Listener {
    private final SellResultFormatter formatter;
    private final UIConfigLoader uiConfigLoader;

    @EventHandler
    public void onAutoSell(AutoSellCompleteEvent event) {
        Player player = event.getPlayer();
        UISettings settings = uiConfigLoader.getSettings();
        if (settings == null) return;

        NotificationSettings chatSettings = settings.chat();
        if (chatSettings.enabled() && !chatSettings.message().isEmpty()) {
            String msg = formatter.formatChat(event.getResult(), chatSettings.message());
            player.sendMessage(MessageFormatter.parse(msg));
        }

        NotificationSettings actionBarSettings = settings.actionBar();
        if (actionBarSettings.enabled() && !actionBarSettings.message().isEmpty()) {
            String msg = formatter.formatActionBar(event.getResult(), actionBarSettings.message());
            player.sendActionBar(MessageFormatter.parse(msg));
        }
    }
}
