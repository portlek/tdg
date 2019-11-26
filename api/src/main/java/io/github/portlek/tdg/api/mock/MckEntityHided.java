package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.EntityHided;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MckEntityHided implements EntityHided {
    @Override
    public void hide(@NotNull Player player, @NotNull Entity entity) {
    }
}
