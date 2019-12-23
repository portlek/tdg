package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.hook.IslandWrapped;
import org.jetbrains.annotations.NotNull;

public final class IslandLevelReq implements Requirement {

    @NotNull
    private final String fallback;

    private final int level;

    @NotNull
    private final IslandWrapped wrapper;

    public IslandLevelReq(@NotNull String fallback, int level, @NotNull IslandWrapped wrapper) {
        this.fallback = fallback;
        this.level = level;
        this.wrapper = wrapper;
    }

    public IslandLevelReq(int level, @NotNull IslandWrapped wrapper) {
        this("", level, wrapper);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final boolean check = wrapper.getIslandLevel(event.getPlayer().getUniqueId()) < level;

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(fallback
                .replace("%level%", String.valueOf(level))
                .replace("%island-level%", String.valueOf(level))
            );
        }

        return check;
    }

}
