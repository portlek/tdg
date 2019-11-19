package io.github.portlek.tdg;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Icon {

    boolean is(@NotNull Entity entity);

    void playSound(@NotNull Player player);

    void accept(@NotNull Player player);

    void closeFor(@NotNull Player player);

    void openFor(@NotNull Player player);

}
