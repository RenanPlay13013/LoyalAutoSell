package net.loyalnetwork.loyalAutoSell.ui.formatter;

import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import net.loyalnetwork.loyalAutoSell.ui.model.FormattedSellMessage;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

@RequiredArgsConstructor
public class SellResultFormatter {

    public FormattedSellMessage format(
            AutoSellResult result,
            Player player
    ) {

        StringJoiner joiner =
                new StringJoiner(", ");

        result.amounts().forEach((economy, amount) -> {

            joiner.add(
                    amount + " " + economy
            );
        });

        String chatMessage =
                "&aSold &f" +
                        result.itemsSold() +
                        " &aitems for &f" +
                        joiner;

        String actionBarMessage =
                "<green>+$" +
                        joiner;

        return new FormattedSellMessage(
                MessageFormatter.parse(chatMessage),
                MessageFormatter.parse(actionBarMessage)
        );
    }
}