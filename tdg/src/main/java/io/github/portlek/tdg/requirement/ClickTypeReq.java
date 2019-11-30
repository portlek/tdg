package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.type.ClickType;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ClickTypeReq implements Requirement {

    @NotNull
    private final String fallback;

    @NotNull
    private final List<ClickType> clickTypes;

    public ClickTypeReq(@NotNull String fallback, @NotNull List<ClickType> clickTypes) {
        this.fallback = fallback;
        this.clickTypes = clickTypes;
    }

    public ClickTypeReq(@NotNull List<ClickType> clickTypes) {
        this("", clickTypes);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        if (!(event instanceof IconClickEvent)) {
            return false;
        }

        final boolean check = clickTypes.contains(ClickType.ANY) ||
            clickTypes.contains(((IconClickEvent) event).getClickType());

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(
                fallback
                    .replaceAll("%click%", ((IconClickEvent) event).getClickType().name())
                    .replaceAll("%require%", new ListOf<>(new Mapped<>(Enum::name, clickTypes)).toString())
            );
        }

        return check;
    }

}
