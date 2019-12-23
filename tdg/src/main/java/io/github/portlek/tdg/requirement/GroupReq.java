package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.hook.GroupWrapped;
import org.jetbrains.annotations.NotNull;

public final class GroupReq implements Requirement {

    @NotNull
    private final String fallback;

    @NotNull
    private final String group;

    @NotNull
    private final GroupWrapped wrapper;

    public GroupReq(@NotNull String fallback, @NotNull String group, @NotNull GroupWrapped wrapper) {
        this.fallback = fallback;
        this.group = group;
        this.wrapper = wrapper;
    }

    public GroupReq(@NotNull String group, @NotNull GroupWrapped wrapper) {
        this("", group, wrapper);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final boolean check = wrapper.getGroup(event.getPlayer()).equalsIgnoreCase(group);

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(fallback
                .replace("%group%", group)
            );
        }

        return check;
    }

}
