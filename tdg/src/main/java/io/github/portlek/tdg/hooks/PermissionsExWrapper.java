package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.GroupWrapped;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class PermissionsExWrapper implements GroupWrapped {

    @NotNull
    private final PermissionsEx permissionsEx;

    public PermissionsExWrapper(@NotNull PermissionsEx permissionsEx) {
        this.permissionsEx = permissionsEx;
    }

    @NotNull
    public String getGroup(@NotNull Player player) {
        return permissionsEx.getPermissionsManager().getUser(player).getIdentifier();
    }

}
