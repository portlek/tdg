
package io.github.portlek.tdg.util;

import io.github.portlek.tdg.TDG;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Metadata {
	
    public static void set(@NotNull Entity entity, @NotNull String key, @NotNull Object value) {
        entity.setMetadata(key, new FixedMetadataValue(TDG.getAPI().tdg, value));
    }
 
    public static void remove(@NotNull Entity entity, @NotNull String key) {
        if (hasKey(entity, key)) {
            entity.removeMetadata(key, TDG.getAPI().tdg);
        }
    }
 
    public static boolean hasKey(@NotNull Entity entity, @NotNull String key) {
        return !entity.getMetadata(key).isEmpty();
    }

    @Nullable
    public static Object get(@NotNull Entity entity, @NotNull String key) {
        return entity.getMetadata(key).get(0).value();
    }
 
    public void removeAll(@NotNull Entity entity, @NotNull String... keys) {
        for (String k : keys) {
            remove(entity, k);
        }
    }

}