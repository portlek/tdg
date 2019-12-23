package io.github.portlek.tdg.file;

import org.jetbrains.annotations.NotNull;

public final class Language {

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
    public final String errorPlayerNotFound;

    @NotNull
    public final String generalAvailableMenus;

    @NotNull
    public final String generalReloadComplete;

    @NotNull
    private final String generalNewVersionFound;

    @NotNull
    private final String generalLatestVersion;

    @NotNull
    public final String commands;

    public Language(@NotNull String errorAlreadyOpen, @NotNull String errorPermission,
                    @NotNull String errorMenuNotFound, @NotNull String errorInvalidArgument,
                    @NotNull String errorInGameCommand, @NotNull String errorPlayerNotFound,
                    @NotNull String generalAvailableMenus, @NotNull String generalReloadComplete,
                    @NotNull String generalNewVersionFound, @NotNull String generalLatestVersion,
                    @NotNull String commands) {
        this.errorAlreadyOpen = errorAlreadyOpen;
        this.errorPermission = errorPermission;
        this.errorMenuNotFound = errorMenuNotFound;
        this.errorInvalidArgument = errorInvalidArgument;
        this.errorInGameCommand = errorInGameCommand;
        this.errorPlayerNotFound = errorPlayerNotFound;
        this.generalAvailableMenus = generalAvailableMenus;
        this.generalReloadComplete = generalReloadComplete;
        this.generalNewVersionFound = generalNewVersionFound;
        this.generalLatestVersion = generalLatestVersion;
        this.commands = commands;
    }

    @NotNull
    public String generalNewVersionFound(@NotNull String version) {
        return version(generalNewVersionFound, version);
    }

    @NotNull
    public String generalLatestVersion(@NotNull String version) {
        return version(generalLatestVersion, version);
    }

    @NotNull
    public String errorMenuNotFound(@NotNull String menu) {
        return menu(menu, errorMenuNotFound);
    }

    @NotNull
    private String menu(@NotNull String menu, @NotNull String text) {
        return text.replace("%menu%", menu);
    }

    @NotNull
    private String version(@NotNull String text, @NotNull String version) {
        return text.replace("%version%", version);
    }

}
