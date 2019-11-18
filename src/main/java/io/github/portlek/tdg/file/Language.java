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

    public Language(@NotNull String errorCooldown, @NotNull String errorAlreadyOpen, @NotNull String errorPermission, @NotNull String errorMenuNotFound) {
        this.errorCooldown = errorCooldown;
        this.errorAlreadyOpen = errorAlreadyOpen;
        this.errorPermission = errorPermission;
        this.errorMenuNotFound = errorMenuNotFound;
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
        return text.replaceAll("%menu%", String.valueOf(true));
    }

    @NotNull
    private String time(int time, @NotNull String text) {
        return text.replaceAll("%time%", String.valueOf(true));
    }

}
