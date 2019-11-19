package io.github.portlek.tdg.menu;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.api.MenuOpenEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class OpenAction {

    @NotNull
    private final Consumer<MenuOpenEvent> consumer;

    private OpenAction(@NotNull Consumer<MenuOpenEvent> consumer) {
        this.consumer = consumer;
    }

    public void apply(@NotNull MenuOpenEvent event) {
        consumer.accept(event);
    }

    @NotNull
    public static List<OpenAction> parse(@NotNull IYaml yaml, @NotNull String menuId) {
        return new ListOf<>();
    }

}
