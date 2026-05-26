package net.loyalnetwork.loyalAutoSell.core.event;

import lombok.Getter;
import lombok.Setter;
import net.loyalnetwork.loyalAutoSell.core.model.PendingSell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

@Getter
public class AutoSellProcessEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS =
            new HandlerList();

    private final Player player;

    /**
     * Mutable pending sell data.
     */
    private final PendingSell pendingSell;

    @Setter
    private boolean cancelled;

    public AutoSellProcessEvent(
            Player player,
            PendingSell pendingSell
    ) {
        this.player = player;
        this.pendingSell = pendingSell;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
