package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OpenedMenu extends Parent<Menu> {

    @NotNull
    LiveIcon findByEntity(@NotNull Entity entity);

    void close();

    void addIcons(@NotNull List<LiveIcon> liveIcons);

    void accept(@NotNull MenuOpenEvent event);

    void accept(@NotNull MenuCloseEvent event);

}
