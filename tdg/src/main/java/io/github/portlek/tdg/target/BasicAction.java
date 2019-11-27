package io.github.portlek.tdg.target;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class BasicAction<T extends MenuEvent> implements Target<T> {

    @NotNull
    private final Consumer<T> consumer;

    @NotNull
    private final List<Requirement> requirements;

    public BasicAction(@NotNull Consumer<T> consumer, @NotNull List<Requirement> requirements) {
        this.consumer = consumer;
        this.requirements = requirements;
    }

    public BasicAction(@NotNull Consumer<T> consumer, @NotNull Requirement... requirements) {
        this(consumer, new ListOf<>(requirements));
    }

    @Override
    public void handle(@NotNull T event) {
        if (requirements.stream().allMatch(requirement -> requirement.control(event))) {
            consumer.accept(event);
        }
    }

}
