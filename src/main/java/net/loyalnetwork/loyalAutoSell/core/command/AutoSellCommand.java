package net.loyalnetwork.loyalAutoSell.core.command;

import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.LoyalAutoSell;
import net.loyalnetwork.loyalAutoSell.core.config.SellConfigLoader;
import net.loyalnetwork.loyalAutoSell.core.manager.WorldManager;
import net.loyalnetwork.loyalAutoSell.core.placeholder.AutoSellExpansion;
import net.loyalnetwork.loyalAutoSell.ui.config.MessagesConfig;
import net.loyalnetwork.loyalAutoSell.ui.config.UIConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class AutoSellCommand implements CommandExecutor {

    private final LoyalAutoSell plugin;

    private final SellConfigLoader configLoader;
    private final WorldManager worldManager;
    private final MessagesConfig messagesConfig;
    private final UIConfigLoader uiConfigLoader;

    private final AutoSellExpansion expansion;

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission(
                "loyalautosell.reload"
        )) {

            sender.sendMessage(
                    "§cYou do not have permission."
            );

            return true;
        }

        if (args.length == 0) {

            sender.sendMessage(
                    "§cUsage: /autosell reload"
            );

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            plugin.reloadConfig();
            messagesConfig.reload();
            uiConfigLoader.load(messagesConfig);

            configLoader.load(
                    plugin.getConfig()
            );

            worldManager.load(
                    plugin.getConfig()
                            .getStringList(
                                    "enabled-worlds"
                            )
            );

            if (expansion != null) {
                expansion.clearCache();
            }

            sender.sendMessage(
                    "§aLoyalAutoSell reloaded."
            );

            return true;
        }

        sender.sendMessage(
                "§cUsage: /autosell reload"
        );

        return true;
    }
}