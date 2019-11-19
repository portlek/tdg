package io.github.portlek.tdg.icon;

import io.github.portlek.mcyaml.IYaml;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ClickAction {

    @NotNull
    public static List<ClickAction> parse(@NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId) {
        return new ListOf<>();
    }

}
