package net.loyalnetwork.loyalAutoSell.ui.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MessageFormatter {
    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.builder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();


    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final MiniMessage MINI_MESSAGE_UNESCAPED = MiniMessage.builder()
            .preProcessor(s -> s)
            .build();

    public static Component parse(String message) {
        if (message == null || message.isBlank()) return Component.empty();

        String normalized = message.replace('§', '&');
        Component legacyComponent = LEGACY.deserialize(normalized);

        // Serializa sem escapar as tags já existentes
        String serialized = MINI_MESSAGE.serialize(legacyComponent)
                .replace("\\<", "<");

        return MINI_MESSAGE.deserialize(serialized);
    }
}