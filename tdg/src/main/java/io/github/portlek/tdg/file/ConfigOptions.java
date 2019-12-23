package io.github.portlek.tdg.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.api.hook.Hook;
import io.github.portlek.tdg.api.hook.Wrapped;
import io.github.portlek.tdg.hooks.*;
import org.bukkit.Bukkit;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class ConfigOptions implements Scalar<Config> {

    private static final String HOOKS = "hooks.";

    @NotNull
    private final IYaml yaml;

    public ConfigOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Config value() {
        yaml.create();

        final String pluginPrefix = yaml.getOrSet("plugin-prefix", "&6[&eKekoRank&6]");
        final String pluginLanguage = yaml.getOrSet("plugin-language", "en");
        final boolean checkForUpdate = yaml.getOrSet("check-for-update", true);
        final boolean hoverEffect = yaml.getOrSet("hover-effect", true);
        final Map<String, Wrapped> wrapped = new HashMap<>();

        initiateHooks(pluginPrefix, wrapped);

        return new Config(
            pluginLanguage,
            pluginPrefix,
            checkForUpdate,
            hoverEffect,
            wrapped
        );
    }

    private void initiateHooks(@NotNull String pluginPrefix, @NotNull Map<String, Wrapped> wrapped) {
        final boolean hooksAutoDetect = yaml.getOrSet(HOOKS + "auto-detect", true);
        boolean hooksLuckPerms = yaml.getOrSet(HOOKS + HookType.LUCK_PERMS.getType(), false);

        hookWithTry(
            "net.luckperms.api.LuckPerms",
            hooksAutoDetect,
            hooksLuckPerms,
            new NewLuckPermsHook(),
            pluginPrefix,
            wrapped,
            HookType.LUCK_PERMS.getType(),
            () -> !wrapped.containsKey(HookType.PERMISSIONS_EX.getType()) &&
                !wrapped.containsKey(HookType.LUCK_PERMS.getType()) &&
                !wrapped.containsKey(HookType.GROUP_MANAGER.getType())
        );

        hookWithTry(
            "me.lucko.luckperms.common.plugin.LuckPermsPlugin",
            hooksAutoDetect,
            hooksLuckPerms,
            new OldLuckPermsHook(),
            pluginPrefix,
            wrapped,
            HookType.LUCK_PERMS.getType(),
            () -> !wrapped.containsKey(HookType.PERMISSIONS_EX.getType()) &&
                !wrapped.containsKey(HookType.LUCK_PERMS.getType()) &&
                !wrapped.containsKey(HookType.GROUP_MANAGER.getType())
        );

        if (!wrapped.containsKey(HookType.LUCK_PERMS.getType())) {
            yaml.set(HOOKS + HookType.LUCK_PERMS.getType(), false);
        }

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.PLACEHOLDER_API.getType(), false),
            new PlaceholderAPIHook(),
            pluginPrefix,
            wrapped,
            HookType.PLACEHOLDER_API.getType()
        );

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.GROUP_MANAGER.getType(), false),
            new GroupManagerHook(), pluginPrefix, wrapped, HookType.GROUP_MANAGER.getType(),
            () -> !wrapped.containsKey(HookType.PERMISSIONS_EX.getType()) &&
                !wrapped.containsKey(HookType.LUCK_PERMS.getType()) &&
                !wrapped.containsKey(HookType.GROUP_MANAGER.getType())
        );

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.PERMISSIONS_EX.getType(), false),
            new PermissionsExHook(), pluginPrefix, wrapped, HookType.PERMISSIONS_EX.getType(),
            () -> !wrapped.containsKey(HookType.GROUP_MANAGER.getType()) &&
                !wrapped.containsKey(HookType.LUCK_PERMS.getType())
        );

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.VAULT.getType(), false),
            new VaultHook(), pluginPrefix, wrapped, HookType.VAULT.getType());

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.ASKYBLOCK.getType(), false),
            new ASkyBlockHook(), pluginPrefix, wrapped, HookType.ASKYBLOCK.getType(),
            () -> !wrapped.containsKey("BentoBox")
        );

        hookLittle(
            hooksAutoDetect,
            yaml.getOrSet(HOOKS + HookType.BENTOBOX.getType(), false),
            new ASkyBlockHook(), pluginPrefix, wrapped, HookType.BENTOBOX.getType(),
            () -> !wrapped.containsKey("ASkyBlock")
        );
    }

    private void hookWithTry(@NotNull String className, boolean hooksAutoDetect, boolean hooks, @NotNull Hook hook,
                             @NotNull String pluginPrefix, @NotNull Map<String, Wrapped> wrapped,
                             @NotNull String path, @NotNull BooleanSupplier booleanSupplier) {
        try {
            Class.forName(className);
            hookLittle(
                hooksAutoDetect,
                hooks,
                hook,
                pluginPrefix,
                wrapped,
                path,
                booleanSupplier
            );
        } catch (Exception ignored) {
            // ignored
        }
    }

    private void hookLittle(boolean hooksAutoDetect, boolean hooks, @NotNull Hook hook, @NotNull String pluginPrefix,
                            @NotNull Map<String, Wrapped> wrapped, @NotNull String path,
                            @NotNull BooleanSupplier supplier) {
        if ((hooksAutoDetect || hooks) && hook.initiate() && supplier.getAsBoolean()) {
            sendHookNotify(pluginPrefix, path);
            wrapped.put(path, hook.create());
            yaml.set(HOOKS + path, true);
        } else {
            yaml.set(HOOKS + path, false);
        }
    }

    private void hookLittle(boolean hooksAutoDetect, boolean hooks, @NotNull Hook hook, @NotNull String pluginPrefix,
                            @NotNull Map<String, Wrapped> wrapped, @NotNull String path) {
        hookLittle(hooksAutoDetect, hooks, hook, pluginPrefix, wrapped, path, () -> true);
    }

    private void sendHookNotify(@NotNull String pluginPrefix, @NotNull String path) {
        Bukkit.getConsoleSender().sendMessage(
            prefix(pluginPrefix, "%prefix% &r>> &a" + path + " is hooking")
        );
    }

    @NotNull
    private String prefix(@NotNull String prefix, @NotNull String text) {
        return new Colored(
            text.replace("%prefix%", prefix)
        ).value();
    }

}
