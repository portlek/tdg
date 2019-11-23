package io.github.portlek.tdg.requirements;

import io.github.portlek.tdg.Requirement;
import io.github.portlek.tdg.events.abs.MenuEvent;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PermissionReq implements Requirement {

    @NotNull
    private final List<String> permissions;

    public PermissionReq(@NotNull String... permissions) {
        this.permissions = new ListOf<>(permissions);
    }

    public PermissionReq(@NotNull List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        return permissions.stream().allMatch(s -> event.getPlayer().hasPermission(s));
    }

}
