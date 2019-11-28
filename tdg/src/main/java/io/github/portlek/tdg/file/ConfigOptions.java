package io.github.portlek.tdg.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.hooks.PAPIHook;
import io.github.portlek.tdg.hooks.VaultHook;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public class ConfigOptions implements Scalar<Config> {

    @NotNull
    private final IYaml yaml;

    public ConfigOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Config value() {
        yaml.create();

        final String language = yaml.getOrSet("Language", "en");
        //final boolean updateCheck = yaml.getOrSet("Update-Check", false);
        final String pluginPrefix = new Colored(yaml.getOrSet("Plugin-Prefix", "&6[&a&lTDG&6]")).value();
        final boolean hoverEffect = yaml.getOrSet("Hover-Effect", true);
        boolean hooksPlaceholderAPI = yaml.getOrSet("Hooks.PlaceholderAPI", false);
        boolean hooksVault = yaml.getOrSet("Hooks.Vault", false);
        final VaultHook vaultHook = new VaultHook();

        if (hooksPlaceholderAPI) {
            hooksPlaceholderAPI = new PAPIHook().initiate();

            if (hooksPlaceholderAPI) {
                TDG.getAPI().tdg.getServer().getLogger().info("PlaceholderAPI hooked!");
            }
        }

        if (hooksVault) {
            hooksVault = vaultHook.initiate();

            if (hooksVault) {
                TDG.getAPI().tdg.getLogger().info("Vault hooked!");
            }
        }

        return new Config(
            language,
            false,
            pluginPrefix,
            hoverEffect,
            hooksPlaceholderAPI,
            hooksVault,
            vaultHook
        );
    }

}
