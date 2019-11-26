package io.github.portlek.tdg.api;

import org.jetbrains.annotations.NotNull;

public interface Hook<T> {

    boolean initiate();

    @NotNull
    T get();

}
