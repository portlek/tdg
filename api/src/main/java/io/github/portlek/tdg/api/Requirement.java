package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public interface Requirement {

    boolean control(@NotNull MenuEvent event);

}
