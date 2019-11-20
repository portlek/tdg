package io.github.portlek.tdg.menu;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.ActionBase;
import io.github.portlek.tdg.events.MenuCloseEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class CloseAction extends ActionBase<MenuCloseEvent> {

    public CloseAction(@NotNull Consumer<MenuCloseEvent> consumer) {
        super(consumer);
    }

    @NotNull
    public static List<CloseAction> parse(@NotNull IYaml yaml, @NotNull String menuId) {
        return new ListOf<>(

        );
    }

}
