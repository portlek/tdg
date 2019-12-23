package io.github.portlek.tdg.hooks;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import io.github.portlek.tdg.api.hook.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ASkyBlockHook implements Hook {

    private ASkyBlockAPI skyBlockAPI;

    @Override
    public boolean initiate() {
        if (Bukkit.getPluginManager().getPlugin("ASkyBlock") != null)
            skyBlockAPI = ASkyBlockAPI.getInstance();

        return skyBlockAPI != null;
    }

    @NotNull
    @Override
    public ASkyBlockWrapper create() {
        if (skyBlockAPI == null) {
            throw new IllegalStateException("ASkyBlock not initiated! Use ASkyBlockHook#initiate() method.");
        }

        return new ASkyBlockWrapper(skyBlockAPI);
    }

}