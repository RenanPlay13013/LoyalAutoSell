package net.loyalnetwork.loyalAutoSell.ui.formatter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MessageFormatter {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final String[][] LEGACY_MAP = {
        {"&0", "<black>"},
        {"&1", "<dark_blue>"},
        {"&2", "<dark_green>"},
        {"&3", "<dark_aqua>"},
        {"&4", "<dark_red>"},
        {"&5", "<dark_purple>"},
        {"&6", "<gold>"},
        {"&7", "<gray>"},
        {"&8", "<dark_gray>"},
        {"&9", "<blue>"},
        {"&a", "<green>"},
        {"&b", "<aqua>"},
        {"&c", "<red>"},
        {"&d", "<light_purple>"},
        {"&e", "<yellow>"},
        {"&f", "<white>"},
        {"&k", "<obfuscated>"},
        {"&l", "<bold>"},
        {"&m", "<strikethrough>"},
        {"&n", "<underline>"},
        {"&o", "<italic>"},
        {"&r", "<reset>"},
    };

    public static Component parse(String message) {
        if (message == null || message.isBlank()) return Component.empty();

        String converted = message;
        for (String[] mapping : LEGACY_MAP) {
            converted = converted.replace(mapping[0], mapping[1]);
        }

        return MINI_MESSAGE.deserialize(converted);
    }
}
