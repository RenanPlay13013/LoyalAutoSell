package net.loyalnetwork.loyalAutoSell.ui.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MessageFormatter {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String message) {
        if (message == null || message.isBlank()) return Component.empty();
        return MINI_MESSAGE.deserialize(message);
    }
}
