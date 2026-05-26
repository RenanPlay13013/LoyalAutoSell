package net.loyalnetwork.loyalAutoSell.ui.formatter;

import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;

import java.util.StringJoiner;

public class SellResultFormatter {

    public String formatAmounts(AutoSellResult result) {
        StringJoiner joiner = new StringJoiner(", ");
        result.amounts().forEach((economy, amount) ->
                joiner.add(amount + " " + economy)
        );
        return joiner.toString();
    }

    public String formatChat(AutoSellResult result, String template) {
        return template
                .replace("%items%", String.valueOf(result.itemsSold()))
                .replace("%amounts%", formatAmounts(result));
    }

    public String formatActionBar(AutoSellResult result, String template) {
        return template.replace("%amounts%", formatAmounts(result));
    }
}
