package net.loyalnetwork.loyalAutoSell.ui.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.loyalnetwork.loyalAutoSell.LoyalAutoSell;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class MessagesConfig {
    private final LoyalAutoSell plugin;
    private File file;
    private FileConfiguration config;

    public void load() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();

        file = new File(
                plugin.getDataFolder(),
                "messages.yml"
        );

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
