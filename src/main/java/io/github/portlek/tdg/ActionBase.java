package io.github.portlek.tdg;

import io.github.portlek.tdg.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class ActionBase<T extends MenuEvent> {

    @NotNull
    private final Consumer<T> consumer;

    public ActionBase(@NotNull Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public void apply(@NotNull T event) {
        consumer.accept(event);
    }

}
