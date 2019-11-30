package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PermissionReq implements Requirement {

    @NotNull
    private final String fallback;

    @NotNull
    private final List<String> permissions;

    public PermissionReq(@NotNull String fallback, @NotNull List<String> permissions) {
        this.fallback = fallback;
        this.permissions = permissions;
    }

    public PermissionReq(@NotNull List<String> permissions) {
        this("", permissions);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final List<String> permissions = new Mapped<>(
            perm -> {
                if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                    return PlaceholderAPI.setPlaceholders(event.getPlayer(), perm);
                }

                return perm.replaceAll("%player_name%", event.getPlayer().getName());
            },
            this.permissions
        );

        final boolean check = permissions.stream().allMatch(s -> event.getPlayer().hasPermission(s));

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(
                fallback
                    .replaceAll("%permissions%", permissions.toString())
            );
        }

        return check;
    }

}
