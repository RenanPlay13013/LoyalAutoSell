package net.loyalnetwork.loyalAutoSell.core.event;


import lombok.Getter;
import net.loyalnetwork.loyalAutoSell.core.model.AutoSellResult;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class AutoSellCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final AutoSellResult result;

    public AutoSellCompleteEvent(Player player, AutoSellResult result) {
        this.player = player;
        this.result = result;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
