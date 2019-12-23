package io.github.portlek.tdg.api.hook;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    boolean initiate();

    @NotNull
    Wrapped create();

}
