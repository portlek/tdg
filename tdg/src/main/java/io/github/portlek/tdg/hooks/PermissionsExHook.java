package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class PermissionsExHook implements Hook {

    private PermissionsEx permissionsEx;

    @Override
    public boolean initiate() {
        return (permissionsEx = (PermissionsEx) Bukkit.getPluginManager().getPlugin("PermissionsEx")) != null;
    }

    @NotNull
    public PermissionsExWrapper create() {
        if (permissionsEx == null) {
            throw new IllegalStateException("PermissionsEx not initiated! Use PermissionsExHook#initiate method.");
        }

        return new PermissionsExWrapper(permissionsEx);
    }

}
