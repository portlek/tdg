package io.github.portlek.tdg;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cactoos.iterable.IterableOf;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.FirstOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ClickType {

    LEFT("left", "left-click"),
    RIGHT("right", "right-clikc"),
    SHIFT_LEFT("shift-left", "shift-left-click"),
    SHIFT_RIGHT("shift-right", "shift-right-click"),
    ANY("any");

    @NotNull
    private final List<String> types;

    ClickType(@NotNull String... types) {
        this.types = new ListOf<>(types);
    }

    @NotNull
    public static ClickType fromString(@NotNull String name) {
        for (ClickType clickType : values()) {
            if (clickType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name))) {
                return clickType;
            }
        }

        return ANY;
    }

    @NotNull
    public static ClickType fromInteractEvent(@NotNull PlayerInteractEvent event) {
        final boolean isSneak = event.getPlayer().isSneaking();
        final Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            return isSneak ? SHIFT_LEFT : LEFT;
        } else if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            return isSneak ? SHIFT_RIGHT : RIGHT;
        }

        return ANY;
    }

}
