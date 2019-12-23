package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import io.github.portlek.tdg.api.hook.Wrapped;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public final class NewLuckPermsHook implements Hook {

    private LuckPerms luckPerms;

    @Override
    public boolean initiate() {
        try {
            Class.forName("net.luckperms.api.LuckPerms");
        } catch (Exception exception) {
            return false;
        }

        final boolean check = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;

        if (check) {
            final RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPerms = provider.getProvider();
            }
        }

        return luckPerms != null;
    }

    @NotNull
    public Wrapped create() {
        if (luckPerms == null) {
            throw new IllegalStateException("LuckPerms not initiated! Use NewLuckPermsHook#initiate method.");
        }

        return new NewLuckPermsWrapper(luckPerms);
    }

}
