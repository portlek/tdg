package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public final class SkyBlockFix {

    @NotNull
    public final Map<UUID, Integer> skyBlockFix;

    public SkyBlockFix(@NotNull Map<UUID, Integer> skyBlockFix) {
        this.skyBlockFix = skyBlockFix;
    }

    public int getOrCreate(@NotNull UUID uuid) {
        skyBlockFix.putIfAbsent(uuid, 0);

        return skyBlockFix.get(uuid);
    }
}
