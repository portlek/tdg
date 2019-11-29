package io.github.portlek.tdg.api;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface LiveIcon extends Parent<Icon> {

    boolean is(@NotNull Entity entity);

    void close();

}
