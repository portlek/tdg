package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import io.github.portlek.tdg.api.hook.Wrapped;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OldLuckPermsHook implements Hook {

    @Nullable
    private LuckPermsApi luckPermsApi;

    @Override
    public boolean initiate() {
        try {
            Class.forName("me.lucko.luckperms.api.LuckPermsApi");
        } catch (Exception exception) {
            return false;
        }

        final boolean check = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;

        if (check) {
            luckPermsApi = LuckPerms.getApi();
        }

        return luckPermsApi != null;
    }

    @NotNull
    public Wrapped create() {
        if (luckPermsApi == null) {
            throw new IllegalStateException("LuckPerms not initiated! Use OldLuckPermsHook#initiate method.");
        }

        return new OldLuckPermsWrapper(luckPermsApi);
    }

}
