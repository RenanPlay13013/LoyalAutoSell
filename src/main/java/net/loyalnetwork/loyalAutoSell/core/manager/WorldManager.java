package net.loyalnetwork.loyalAutoSell.core.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldManager {

    private final Set<String> enabledWorlds =
            new HashSet<>();

    public void load(List<String> worlds) {

        enabledWorlds.clear();

        for (String world : worlds) {
            enabledWorlds.add(world.toLowerCase());
        }
    }

    public boolean isEnabled(String worldName) {

        if (worldName == null) {
            return false;
        }

        return enabledWorlds.contains(
                worldName.toLowerCase()
        );
    }

}