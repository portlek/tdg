package io.github.portlek.tdg.menu;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.api.MenuCloseEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class CloseAction {

    @NotNull
    private final Consumer<MenuCloseEvent> consumer;

    private CloseAction(@NotNull Consumer<MenuCloseEvent> consumer) {
        this.consumer = consumer;
    }

    public void apply(@NotNull MenuCloseEvent event) {
        consumer.accept(event);
    }

    @NotNull
    public static List<CloseAction> parse(@NotNull IYaml yaml, @NotNull String menuId) {
        return new ListOf<>();
    }

}
