package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.GroupWrapped;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class NewLuckPermsWrapper implements GroupWrapped {

    @NotNull
    private final LuckPerms luckPerms;

    public NewLuckPermsWrapper(@NotNull LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @NotNull
    @Override
    public String getGroup(@NotNull Player player) {
        final User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            return "";
        }

        return user.getPrimaryGroup();
    }

}
