package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.GroupWrapped;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GroupManagerWrapper implements GroupWrapped {

    @NotNull
    private final GroupManager groupManager;

    public GroupManagerWrapper(@NotNull GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @NotNull
    public String getGroup(@NotNull Player player) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player);

        if (handler == null) {
            return "";
        }

        return handler.getGroup(player.getName());
    }

}
