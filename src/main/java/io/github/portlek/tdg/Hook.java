package io.github.portlek.tdg;

import org.jetbrains.annotations.NotNull;

public interface Hook<T> {

    boolean initiate();

    @NotNull
    T get();

}
