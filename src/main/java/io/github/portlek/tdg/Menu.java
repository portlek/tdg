package io.github.portlek.tdg;

import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Menu {

    boolean hasPermission(@NotNull Player player);

    boolean is(@NotNull String command);

    void open(@NotNull Player player, boolean changed);

    void accept(@NotNull MenuOpenEvent event);

    void accept(@NotNull MenuCloseEvent event);

}
