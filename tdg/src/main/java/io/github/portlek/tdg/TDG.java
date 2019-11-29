package io.github.portlek.tdg;

import io.github.portlek.tdg.api.EntityHided;
import io.github.portlek.tdg.api.mock.MckEntityHided;
import io.github.portlek.tdg.command.TDGCommand;
import io.github.portlek.tdg.nms.v1_10_R1.EntityHider1_10_R1;
import io.github.portlek.tdg.nms.v1_11_R1.EntityHider1_11_R1;
import io.github.portlek.tdg.nms.v1_12_R1.EntityHider1_12_R1;
import io.github.portlek.tdg.nms.v1_13_R1.EntityHider1_13_R1;
import io.github.portlek.tdg.nms.v1_13_R2.EntityHider1_13;
import io.github.portlek.tdg.nms.v1_13_R2.Skull1_13;
import io.github.portlek.tdg.nms.v1_14_R1.EntityHider1_14_R1;
import io.github.portlek.tdg.nms.v1_14_R1.Skull1_14;
import io.github.portlek.tdg.nms.v1_8_R1.EntityHider1_8_R1;
import io.github.portlek.tdg.nms.v1_8_R2.EntityHider1_8_R2;
import io.github.portlek.tdg.nms.v1_8_R3.EntityHider1_8_R3;
import io.github.portlek.tdg.nms.v1_9_R1.EntityHider1_9_R1;
import io.github.portlek.tdg.nms.v1_9_R2.EntityHider1_9_R2;
import io.github.portlek.versionmatched.Version;
import io.github.portlek.versionmatched.VersionMatched;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public final class TDG extends JavaPlugin {

    private static final Version version = new Version();
    public static final Function<String, ItemStack> SKULL;
    public static final boolean NEW_PARTICLES = version.minor() > 8;
    public static final EntityHided ENTITY_HIDED = new VersionMatched<>(
        new MckEntityHided(),
        EntityHider1_8_R1.class,
        EntityHider1_8_R2.class,
        EntityHider1_8_R3.class,
        EntityHider1_9_R1.class,
        EntityHider1_9_R2.class,
        EntityHider1_10_R1.class,
        EntityHider1_11_R1.class,
        EntityHider1_12_R1.class,
        EntityHider1_13_R1.class,
        EntityHider1_13.class,
        EntityHider1_14_R1.class
    ).of().instance();

    static {
        if (version.minor() > 13) {
            SKULL = texture -> new Skull1_14(texture).value();
        } else {
            SKULL = texture -> new Skull1_13(texture).value();
        }

        if (ENTITY_HIDED instanceof MckEntityHided) {
            throw new UnsupportedOperationException("Please run the TDG plugin between 1.8 and above!");
        }
    }

    private static TDGAPI api;

    @Override
    public void onEnable() {
        if (api != null) {
            throw new RuntimeException("TDG cannot start twice!");
        }

        final PluginCommand pluginCommand = getCommand("threedimentiongui");

        if (pluginCommand == null) {
            return;
        }

        api = new TDGAPI(this);
        final TDGCommand tdgCommand = new TDGCommand(getAPI());

        pluginCommand.setTabCompleter(tdgCommand);
        pluginCommand.setExecutor(tdgCommand);
        getAPI().reloadPlugin();
    }

    @Override
    public void onDisable() {
        if (api == null) {
            return;
        }

        api.disablePlugin();
        api = null;
    }

    public static TDGAPI getAPI() {
        if (api == null) {
            throw new RuntimeException("TDG cannot use before start!");
        }

        return api;
    }

}
