package net.loyalnetwork.loyalAutoSell.ui.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MessageFormatter {

    public static Component parse(String message) {
        if (message == null || message.isBlank()) return Component.empty();

        String normalized = message.replace('§', '&');

        if (normalized.contains("<")) {
            return MiniMessage.miniMessage().deserialize(normalized);
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(normalized);
    }
}
