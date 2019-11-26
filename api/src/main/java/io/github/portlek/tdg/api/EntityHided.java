package io.github.portlek.tdg.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface EntityHided {

    void hide(@NotNull Player player, @NotNull Entity entity);

}
