package net.loyalnetwork.loyalAutoSell.ui.model;

import net.kyori.adventure.text.Component;

public record FormattedSellMessage(
        Component chat,
        Component actionBar
) {
}
