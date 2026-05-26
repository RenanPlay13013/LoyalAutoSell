package net.loyalnetwork.loyalAutoSell.tracker;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DirtyTracker {

    private final ConcurrentHashMap<UUID, Long> dirty = new ConcurrentHashMap<>();

    public void mark(UUID uuid) {
        dirty.put(uuid, System.currentTimeMillis());
    }

    public Set<Map.Entry<UUID, Long>> entrySet() {
        return dirty.entrySet();
    }

    public void clear() {
        dirty.clear();
    }

    public int size() {
        return dirty.size();
    }
}
