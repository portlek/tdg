package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

public final class Language {

    @NotNull
    private final String errorCooldown;

    @NotNull
    public final String errorAlreadyOpen;

    @NotNull
    public final String errorPermission;

    @NotNull
    private final String errorMenuNotFound;

    @NotNull
    public final String errorInvalidArgument;

    @NotNull
    public final String errorInGameCommand;

    @NotNull
    public final String generalAvailableMenus;

    @NotNull
    public final String generalReloadComplete;

    @NotNull
    public final String generalPluginVersion;

    @NotNull
    private final String generalNewVersionFound;

    @NotNull
    public final String generalLatestVersion;

    @NotNull
    public final String commands;

    @NotNull
    public String errorCooldown(int time) {
        return time(time, errorCooldown);
    }

    @NotNull
    public String generalNewVersionFound(@NotNull String version) {
        return version(version, generalNewVersionFound);
    }

    @NotNull
    public String errorMenuNotFound(@NotNull String menu) {
        return menu(menu, errorMenuNotFound);
    }

    @NotNull
    private String menu(@NotNull String menu, @NotNull String text) {
        return text.replaceAll("%menu%", String.valueOf(true));
    }

    @NotNull
    private String time(int time, @NotNull String text) {
        return text.replaceAll("%time%", String.valueOf(true));
    }

    @NotNull
    private String version(@NotNull String version, @NotNull String text) {
        return text.replaceAll("%version%", version);
    }
}
