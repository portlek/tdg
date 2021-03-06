package io.github.portlek.tdg.api.type;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public enum ClickType {

    LEFT("left", "left-click", "left_click"),
    RIGHT("right", "right-click", "right_click"),
    SHIFT_LEFT(
        "shift-left",
        "shift-left-click",
        "shift_left",
        "shift_left_click",
        "shift_left-click",
        "shift-left_click"
    ),
    SHIFT_RIGHT("shift-right",
        "shift-right-click",
        "shift_right",
        "shift_right_click",
        "shift_right-click",
        "shift-right_click"
    ),
    ANY("any");

    @NotNull
    private final List<String> types;

    ClickType(@NotNull String... types) {
        this.types = new ListOf<>(types);
    }

    @NotNull
    public static ClickType fromString(@NotNull String name) {
        for (ClickType clickType : values()) {
            if (clickType.types.stream().anyMatch(s -> s.equalsIgnoreCase(name.toLowerCase(Locale.ENGLISH)))) {
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

    @NotNull
    public static ClickType fromInteractEvent(@NotNull PlayerInteractAtEntityEvent event) {
        return event.getPlayer().isSneaking() ? SHIFT_RIGHT : RIGHT;
    }

}
