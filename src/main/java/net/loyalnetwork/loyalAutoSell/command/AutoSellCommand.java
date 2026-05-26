package net.loyalnetwork.loyalAutoSell.command;

import net.loyalnetwork.loyalAutoSell.LoyalAutoSell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AutoSellCommand implements CommandExecutor {

    private final LoyalAutoSell plugin;

    public AutoSellCommand(LoyalAutoSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUse: /autosell reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("loyautosell.reload")) {
                sender.sendMessage("§cSem permissão.");
                return true;
            }

            try {
                plugin.reload();
                sender.sendMessage("§aConfig recarregada com sucesso!");
            } catch (Exception e) {
                sender.sendMessage("§cErro ao recarregar config. Verifique o console.");
                e.printStackTrace();
            }

            return true;
        }

        sender.sendMessage("§cSubcomando inválido.");
        return true;
    }
}
