package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.IslandWrapped;
import org.bukkit.World;
import org.cactoos.scalar.MaxOf;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.AddonClassLoader;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BentoBoxWrapper implements IslandWrapped {

    public static boolean isTDG = false;

    @NotNull
    private final BentoBox bentoBox;

    @NotNull
    private final AddonClassLoader classLoader;

    public BentoBoxWrapper(@NotNull BentoBox bentoBox, @NotNull AddonClassLoader classLoader) {
        this.bentoBox = bentoBox;
        this.classLoader = classLoader;
    }

    @Override
    public long getIslandLevel(@NotNull UUID uuid) {
        final AtomicLong level = new AtomicLong(0);

        findFirstIsland(uuid).ifPresent(island -> {
            try {
                level.addAndGet((long) classLoader
                    .findClass("world.bentobox.level.Level", false)
                    .getMethod("getIslandLevel", World.class, UUID.class)
                    .invoke(classLoader.getAddon(), island.getWorld(), uuid)
                );
            } catch (Exception ignored) {
                // ignored
            }
        });

        return level.get();
    }

    @Override
    public void removeIslandLevel(@NotNull UUID uuid, long level) {
        setIslandLevel(uuid, new MaxOf(0L, getIslandLevel(uuid) - level).longValue());
    }

    @Override
    public void addIslandLevel(@NotNull UUID uuid, long level) {
        setIslandLevel(uuid, getIslandLevel(uuid) + level);
    }

    @Override
    public void setIslandLevel(@NotNull UUID uuid, long level) {
        findFirstIsland(uuid).ifPresent(island -> {
            try {
                if (!isTDG) {
                    isTDG = true;
                    classLoader
                        .findClass("world.bentobox.level.Level", false)
                        .getMethod("calculateIslandLevel", World.class, User.class, UUID.class)
                        .invoke(classLoader.getAddon(), island.getWorld(), User.getInstance(uuid), uuid);
                }

                classLoader
                    .findClass("world.bentobox.level.Level", false)
                    .getMethod("setIslandLevel", World.class, UUID.class, long.class)
                    .invoke(classLoader.getAddon(), island.getWorld(), uuid, level);
            } catch (Exception ignored) {
                // ignored
            }
        });
    }

    @NotNull
    public Optional<Island> findFirstIsland(@NotNull UUID uuid) {
        for (Island island : bentoBox.getIslands().getIslands()) {
            if (island.getWorld() != null &&
                island.getOwner() != null &&
                island.getOwner().equals(uuid)) {
                return Optional.of(island);
            }
        }

        return Optional.empty();
    }

}
