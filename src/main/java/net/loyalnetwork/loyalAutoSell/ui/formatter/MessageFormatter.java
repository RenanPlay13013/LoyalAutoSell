package net.loyalnetwork.loyalAutoSell.ui.formatter;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@NoArgsConstructor
public final class MessageFormatter {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.builder()
                    .hexColors()
                    .character('&')
                    .build();

    public static Component parse(String message) {
        if (message == null || message.isBlank()) return Component.empty();

        Component legacyComponent = LEGACY.deserialize(message);

        String miniMessage = MINI_MESSAGE.serialize(legacyComponent);

        return MINI_MESSAGE.deserialize(miniMessage);
    }
}
