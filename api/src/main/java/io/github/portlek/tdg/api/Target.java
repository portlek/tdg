package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public interface Target<T extends MenuEvent> {

    void handle(@NotNull T event);

}
