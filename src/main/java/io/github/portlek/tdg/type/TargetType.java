package io.github.portlek.tdg.type;

import org.jetbrains.annotations.NotNull;

public enum TargetType {

    OPEN("open-actions"),
    CLOSE("close-actions"),
    CLICK("click-actions"),
    HOVER("hover-actions");

    @NotNull
    private final String type;

    TargetType(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    public String getType() {
        return type;
    }

}
