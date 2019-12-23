package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.hooks.PlaceholderAPIWrapper;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        final List<String> permission = new Mapped<>(
            perm -> {
                final AtomicReference<String> atomicReference = new AtomicReference<>(
                    perm
                );
                TDG.getAPI().config.getWrapped("PlaceholderAPI").ifPresent(wrapped ->
                    atomicReference.set(((PlaceholderAPIWrapper)wrapped).apply(event.getPlayer(), perm)));

                return atomicReference.get().replace("%player_name%", event.getPlayer().getName());
            },
            permissions
        );

        final boolean check = permission.stream().allMatch(s -> event.getPlayer().hasPermission(s));

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(
                fallback
                    .replace("%permissions%", permission.toString())
            );
        }

        return check;
    }

}
