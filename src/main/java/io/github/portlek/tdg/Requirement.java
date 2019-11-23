package io.github.portlek.tdg;

import io.github.portlek.tdg.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public interface Requirement {

    boolean control(@NotNull MenuEvent event);

}
