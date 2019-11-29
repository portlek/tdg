package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.type.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ClickTypeReq implements Requirement {

    @NotNull
    private final List<ClickType> clickTypes;

    public ClickTypeReq(@NotNull List<ClickType> clickTypes) {
        this.clickTypes = clickTypes;
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        return event instanceof IconClickEvent &&
            (clickTypes.contains(ClickType.ANY) ||
                clickTypes.contains(((IconClickEvent) event).getClickType()));
    }

}
