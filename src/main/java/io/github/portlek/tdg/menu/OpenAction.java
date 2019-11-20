package io.github.portlek.tdg.menu;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.ActionBase;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class OpenAction extends ActionBase<MenuOpenEvent> {

    public OpenAction(@NotNull Consumer<MenuOpenEvent> consumer) {
        super(consumer);
    }

    @NotNull
    public static List<OpenAction> parse(@NotNull IYaml yaml, @NotNull String menuId) {
        return new ListOf<>(

        );
    }

}
