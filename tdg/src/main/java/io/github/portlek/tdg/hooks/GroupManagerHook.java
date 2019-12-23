package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import io.github.portlek.tdg.api.hook.Wrapped;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class GroupManagerHook implements Hook {

    private GroupManager groupManager;

    @Override
    public boolean initiate() {
        return (groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager")) != null;
    }

    @NotNull
    public Wrapped create() {
        if (groupManager == null) {
            throw new IllegalStateException("GroupManager not initiated! Use GroupManagerHook#initiate method.");
        }

        return new GroupManagerWrapper(groupManager);
    }

}
