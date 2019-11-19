package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.api.IconClickedEvent;
import io.github.portlek.tdg.api.IconHoverEvent;
import io.github.portlek.tdg.types.IconType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BasicIcon implements Icon {

    @NotNull
    private final String id;

    @NotNull
    private final String name;

    @NotNull
    private final IconType iconType;

    @NotNull
    private final String material;

    private final byte materialData;

    private final int positionX;

    private final int positionY;

    @NotNull
    private final List<ClickAction> clickAction;

    @NotNull
    private final HoverAction hoverAction;

    public BasicIcon(@NotNull String id, @NotNull String name, @NotNull IconType iconType, @NotNull String material,
                     byte materialData, int positionX, int positionY, @NotNull List<ClickAction> clickAction,
                     @NotNull HoverAction hoverAction) {
        this.id = id;
        this.name = name;
        this.iconType = iconType;
        this.material = material;
        this.materialData = materialData;
        this.positionX = positionX;
        this.positionY = positionY;
        this.clickAction = clickAction;
        this.hoverAction = hoverAction;
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return false;
    }

    @Override
    public void playSound(@NotNull Player player) {

    }

    @Override
    public void acceptClickEvent(@NotNull IconClickedEvent event) {

    }

    @Override
    public void acceptHoverEvent(@NotNull IconHoverEvent event) {

    }
}
