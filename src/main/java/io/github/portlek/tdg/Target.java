package io.github.portlek.tdg;

import io.github.portlek.tdg.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public interface Target<T extends MenuEvent> {

    void handle(@NotNull T event);

}
