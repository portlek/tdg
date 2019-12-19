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
    public final String errorPlayerNotFound;

    @NotNull
    public final String generalAvailableMenus;

    @NotNull
    public final String generalReloadComplete;

    @NotNull
    public final String commands;

    public Language(@NotNull String errorCooldown, @NotNull String errorAlreadyOpen, @NotNull String errorPermission,
                    @NotNull String errorMenuNotFound, @NotNull String errorInvalidArgument,
                    @NotNull String errorInGameCommand, @NotNull String errorPlayerNotFound,
                    @NotNull String generalAvailableMenus, @NotNull String generalReloadComplete,
                    @NotNull String commands) {
        this.errorCooldown = errorCooldown;
        this.errorAlreadyOpen = errorAlreadyOpen;
        this.errorPermission = errorPermission;
        this.errorMenuNotFound = errorMenuNotFound;
        this.errorInvalidArgument = errorInvalidArgument;
        this.errorInGameCommand = errorInGameCommand;
        this.errorPlayerNotFound = errorPlayerNotFound;
        this.generalAvailableMenus = generalAvailableMenus;
        this.generalReloadComplete = generalReloadComplete;
        this.commands = commands;
    }

    @NotNull
    public String errorCooldown(int time) {
        return time(time, errorCooldown);
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
    private String time(int time, @NotNull String text) {
        return text.replace("%time%", String.valueOf(time));
    }

}
