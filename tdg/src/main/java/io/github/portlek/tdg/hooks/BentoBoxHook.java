package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.hook.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;

import java.util.Optional;

public class BentoBoxHook implements Hook {

    private BentoBox bentoBox;

    @Override
    public boolean initiate() {
        if (Bukkit.getPluginManager().getPlugin("BentoBox") != null)
            bentoBox = BentoBox.getInstance();

        return bentoBox != null && bentoBox.getAddonsManager().getAddonByName("Level").isPresent();
    }

    @NotNull
    @Override
    public BentoBoxWrapper create() {
        if (bentoBox == null) {
            throw new IllegalStateException("BentoBox not initiated! Use BentoBox#initiate() method.");
        }

        final Optional<Addon> addon = bentoBox.getAddonsManager().getAddonByName("Level");

        if (!addon.isPresent()) {
            throw new IllegalStateException("BentoBox not initiated! Use BentoBox#initiate() method.");
        }

        return new BentoBoxWrapper(bentoBox, bentoBox.getAddonsManager().getLoader(addon.get()));
    }
}
