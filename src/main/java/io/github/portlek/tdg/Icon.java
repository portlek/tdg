package io.github.portlek.tdg;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Icon {

    boolean is(@NotNull Entity entity);

    void playSound(@NotNull Player player);

    void acceptClickEvent(@NotNull Player player);

    void acceptHoverEvent(@NotNull Player player);

}
