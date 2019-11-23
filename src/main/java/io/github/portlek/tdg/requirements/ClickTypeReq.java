package io.github.portlek.tdg.requirements;

import io.github.portlek.tdg.ClickType;
import io.github.portlek.tdg.Requirement;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.abs.MenuEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ClickTypeReq implements Requirement {

    @NotNull
    private final List<ClickType> clickType;

    public ClickTypeReq(@NotNull ClickType... clickType) {
        this.clickType = new ListOf<>(clickType);
    }

    public ClickTypeReq(@NotNull List<ClickType> clickType) {
        this.clickType = clickType;
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        return event instanceof IconClickEvent &&
            clickType.contains(((IconClickEvent) event).getClickType());
    }
}
