package net.loyalnetwork.loyalAutoSell.ui.config;

import lombok.Getter;
import net.loyalnetwork.loyalAutoSell.ui.model.NotificationSettings;
import net.loyalnetwork.loyalAutoSell.ui.model.UISettings;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class UIConfigLoader {

    private UISettings settings;

    public void load(
            MessagesConfig messagesConfig
    ) {

        FileConfiguration config =
                messagesConfig.getConfig();

        NotificationSettings chat =
                new NotificationSettings(
                        config.getBoolean(
                                "messages.chat.enabled"
                        ),
                        config.getString(
                                "messages.chat.message",
                                ""
                        )
                );

        NotificationSettings actionBar =
                new NotificationSettings(
                        config.getBoolean(
                                "messages.actionbar.enabled"
                        ),
                        config.getString(
                                "messages.actionbar.message",
                                ""
                        )
                );

        this.settings = new UISettings(
                chat,
                actionBar
        );
    }
}