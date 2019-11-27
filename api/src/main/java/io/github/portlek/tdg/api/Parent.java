package io.github.portlek.tdg.api;

import org.jetbrains.annotations.NotNull;

public interface Parent<T> {

    @NotNull
    T getParent();

}
