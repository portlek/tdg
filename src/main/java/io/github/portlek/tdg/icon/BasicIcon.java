package io.github.portlek.tdg.icon;

import io.github.portlek.tdg.ActionBase;
import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.events.abs.IconEvent;
import io.github.portlek.tdg.types.IconType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BasicIcon<T extends IconEvent> implements Icon {

    private static final List<Player> view = new ArrayList<>();

    private static final List<Player> toHide = new ArrayList<>();

    private static final List<ArmorStand> icons = new ArrayList<>();

    @NotNull
    private final String id;

    @NotNull
    private final String name;

    @NotNull
    private final IconType iconType;

    @NotNull
    private final String material;

    private final byte materialData;

    @NotNull
    private final List<ActionBase<T>> actionBases;

    private final int positionX;

    private final int positionY;

    public BasicIcon(@NotNull String id, @NotNull String name, @NotNull IconType iconType, @NotNull String material,
                     byte materialData, @NotNull List<ActionBase<T>> actionBases, int positionX, int positionY) {
        this.id = id;
        this.name = name;
        this.iconType = iconType;
        this.material = material;
        this.materialData = materialData;
        this.actionBases = actionBases;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    @Override
    public void openFor(@NotNull Player player) {

    }

}
