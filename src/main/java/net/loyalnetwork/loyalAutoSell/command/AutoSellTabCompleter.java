package net.loyalnetwork.loyalAutoSell.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoSellTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();

        List<String> sub = List.of("reload");

        for (String s : sub) {
            if (s.startsWith(args[0].toLowerCase())) {
                completions.add(s);
            }
        }

        return completions;
    }
}
