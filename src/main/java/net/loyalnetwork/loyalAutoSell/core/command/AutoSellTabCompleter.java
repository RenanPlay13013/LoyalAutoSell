package net.loyalnetwork.loyalAutoSell.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AutoSellTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (!sender.hasPermission(
                "loyalautosell.reload"
        )) {
            return Collections.emptyList();
        }

        if (args.length == 1) {

            if ("reload".startsWith(
                    args[0].toLowerCase(Locale.ROOT)
            )) {

                return List.of("reload");
            }
        }

        return Collections.emptyList();
    }
}