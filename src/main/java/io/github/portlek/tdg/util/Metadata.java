
package io.github.portlek.tdg.util;

import io.github.portlek.tdg.TDG;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

public class Metadata {
	
    public static void set(Entity e, String key, Object value) {
        e.setMetadata(key, new FixedMetadataValue(TDG.getAPI().tdg, value));
    }
 
    public static void remove(Entity e, String key) {
        if (hasKey(e, key)) {
            e.removeMetadata(key, TDG.getAPI().tdg);
        }
    }
 
    public static boolean hasKey(Entity e, String key) {
        return !e.getMetadata(key).isEmpty();
    }
 
    public Object get(Entity e, String key) {
        return e.getMetadata(key).get(0).value();
    }
 
    public void removeAll(Entity e, String[] keys) {
        for (String k : keys) {
            remove(e, k);
        }
    }

}