package io.github.portlek.tdg.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Menu {

    boolean hasPermission(@NotNull Player player);

    boolean is(@NotNull String command);

    void open(@NotNull Player player, boolean changed);

}
