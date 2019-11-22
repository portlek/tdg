package io.github.portlek.tdg;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OpenedMenu {

    @NotNull
    LiveIcon findByEntity(@NotNull Entity entity);

    void close();

    void addIcons(@NotNull List<LiveIcon> liveIcons);

}
