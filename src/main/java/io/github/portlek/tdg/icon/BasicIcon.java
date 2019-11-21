package io.github.portlek.tdg.icon;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.tdg.ActionBase;
import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.events.abs.IconEvent;
import io.github.portlek.tdg.types.IconType;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
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

    @NotNull
    @Override
    public LiveIcon createFor(@NotNull Player player) {
        final Location location = player.getLocation();
        final Vector playerDirection = player.getLocation().getDirection();
        final Vector direction = playerDirection.normalize();

        direction.multiply(-2);
        location.setDirection(direction);

        final float yaw = (float) Math.toDegrees(
            Math.atan2(
                player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX()
            )
        ) - 90;
        final float pitch = (float) Math.toDegrees(
            Math.atan2(
                player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX()
            )
        ) - 90;

        location.setYaw(yaw);
        location.setPitch(pitch);

        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }

        final ArmorStand armorStand = player.getWorld().spawn(location, ArmorStand.class);
        final LiveIcon liveIcon = new BasicLiveIcon(this, armorStand, player);

        armorStand.setVisible(false);
        armorStand.setCustomName(new Colored(name).value());
        armorStand.setCustomNameVisible(true);

        return liveIcon;
    }

}
