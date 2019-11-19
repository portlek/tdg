package io.github.portlek.tdg;

import io.github.portlek.tdg.api.IconClickedEvent;
import io.github.portlek.tdg.api.IconHoverEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Icon {

    boolean is(@NotNull Entity entity);

    void playSound(@NotNull Player player);

    void acceptClickEvent(@NotNull IconClickedEvent event);

    void acceptHoverEvent(@NotNull IconHoverEvent event);

}
