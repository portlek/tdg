package io.github.portlek.tdg.hooks;

import org.jetbrains.annotations.NotNull;

public enum HookType {

    PLACEHOLDER_API("PlaceholderAPI"),
    GROUP_MANAGER("GroupManager"),
    LUCK_PERMS("LuckPerms"),
    PERMISSIONS_EX("PermissionsEx"),
    VAULT("Vault"),
    ASKYBLOCK("ASkyBlock"),
    BENTOBOX("BentoBox");

    @NotNull
    private final String type;

    HookType(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    public String getType() {
        return type;
    }
}
