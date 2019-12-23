package io.github.portlek.tdg.api.hook;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface IslandWrapped extends Wrapped {

    long getIslandLevel(@NotNull UUID uuid);

    void setIslandLevel(@NotNull UUID uuid, long level);

    void removeIslandLevel(@NotNull UUID uuid, long level);

    void addIslandLevel(@NotNull UUID uuid, long level);

}
