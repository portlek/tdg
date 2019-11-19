package io.github.portlek.tdg.util;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Metadata {

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final Entity entity;

    public Metadata(@NotNull Plugin plugin, @NotNull Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
    }

    public void set(@NotNull String key, @NotNull Object value) {
        entity.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public void remove(@NotNull String key) {
        if (hasKey(key)) {
            entity.removeMetadata(key, plugin);
        }
    }

    public boolean hasKey(@NotNull String key) {
        return !entity.getMetadata(key).isEmpty();
    }

    public Object get(@NotNull String key) {
        return entity.getMetadata(key).get(0).value();
    }

    public void removeAll(@NotNull String[] keys) {
        for (String k : keys) {
            remove(k);
        }
    }

}