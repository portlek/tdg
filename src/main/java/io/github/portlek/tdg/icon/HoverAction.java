package io.github.portlek.tdg.icon;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.ActionBase;
import io.github.portlek.tdg.events.IconClickEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class HoverAction extends ActionBase<IconClickEvent> {

    public HoverAction(@NotNull Consumer<IconClickEvent> consumer) {
        super(consumer);
    }

    @NotNull
    public static List<HoverAction> parse(@NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId) {
        return new ListOf<>(

        );
    }

}
