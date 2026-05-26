package net.loyalnetwork.loyalAutoSell.core.manager;

import net.loyalnetwork.loyalAutoSell.core.model.SellEntry;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SellManager {

    private final Map<Material, SellEntry> entries =
            new ConcurrentHashMap<>();

    public void register(SellEntry entry) {

        if (entry == null) {
            return;
        }

        entries.put(entry.material(), entry);
    }

    public void unregister(Material material) {

        if (material == null) {
            return;
        }

        entries.remove(material);
    }

    public SellEntry get(Material material) {

        if (material == null) {
            return null;
        }

        return entries.get(material);
    }

    public boolean contains(Material material) {

        if (material == null) {
            return false;
        }

        return entries.containsKey(material);
    }

    public Collection<SellEntry> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }

    public void clear() {
        entries.clear();
    }

}