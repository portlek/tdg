package io.github.portlek.tdg;

import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.IconHoverEvent;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface LiveIcon {

    boolean is(@NotNull Entity entity);

    void playSound();

    void close();

    void accept(@NotNull IconClickEvent event);

    void accept(@NotNull IconHoverEvent event);

}
