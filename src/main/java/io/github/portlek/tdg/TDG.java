package io.github.portlek.tdg;

import io.github.portlek.tdg.command.TDGCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class TDG extends JavaPlugin {

    private static TDGAPI api;

    @Override
    public void onEnable() {
        if (api != null) {
            throw new RuntimeException("TDG cannot start twice!");
        }

        api = new TDGAPI(this);

        final PluginCommand pluginCommand = getCommand("tdg");

        if (pluginCommand == null) {
            return;
        }

        final TDGCommand tdgCommand = new TDGCommand(api);

        pluginCommand.setTabCompleter(tdgCommand);
        pluginCommand.setExecutor(tdgCommand);

        api.reloadPlugin();
    }

    @Override
    public void onDisable() {
        api.getEntities().forEach(Entity::remove);
    }

    public static TDGAPI getAPI() {
        if (api == null)
            throw new RuntimeException("TDG cannot use before start!");
        return api;
    }

}
