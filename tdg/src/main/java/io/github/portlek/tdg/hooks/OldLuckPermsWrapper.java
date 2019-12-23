package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.GroupWrapped;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class OldLuckPermsWrapper implements GroupWrapped {

    @NotNull
    private final LuckPermsApi luckPermsApi;

    public OldLuckPermsWrapper(@NotNull LuckPermsApi luckPermsApi) {
        this.luckPermsApi = luckPermsApi;
    }

    @NotNull
    @Override
    public String getGroup(@NotNull Player player) {
        final User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            return "";
        }

        return user.getPrimaryGroup();
    }

}
