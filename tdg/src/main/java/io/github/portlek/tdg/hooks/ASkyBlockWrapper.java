package io.github.portlek.tdg.hooks;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import io.github.portlek.tdg.api.hook.IslandWrapped;
import org.cactoos.scalar.MaxOf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ASkyBlockWrapper implements IslandWrapped {

    public static boolean isTDG = false;

    @NotNull
    private final ASkyBlockAPI skyBlockAPI;

    public ASkyBlockWrapper(@NotNull ASkyBlockAPI skyBlockAPI) {
        this.skyBlockAPI = skyBlockAPI;
    }

    @Override
    public long getIslandLevel(@NotNull UUID uuid) {
        return skyBlockAPI.getLongIslandLevel(uuid);
    }

    @Override
    public void setIslandLevel(@NotNull UUID uuid, long level) {
        if (!isTDG) {
            isTDG = true;
            skyBlockAPI.calculateIslandLevel(uuid);
        }
        ASkyBlock.getPlugin().getPlayers().setIslandLevel(uuid, level);
    }

    @Override
    public void removeIslandLevel(@NotNull UUID uuid, long level) {
        setIslandLevel(uuid, new MaxOf(0L, getIslandLevel(uuid) - level).longValue());
    }

    @Override
    public void addIslandLevel(@NotNull UUID uuid, long level) {
        setIslandLevel(uuid, getIslandLevel(uuid) + level);
    }

}