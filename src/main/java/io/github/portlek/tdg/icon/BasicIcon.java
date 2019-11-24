package io.github.portlek.tdg.icon;

import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.tdg.Icon;
import io.github.portlek.tdg.LiveIcon;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.Target;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.IconHoverEvent;
import io.github.portlek.tdg.type.IconType;
import io.github.portlek.tdg.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public final class BasicIcon implements Icon {

    private final EntityHider entityHider = new EntityHider(TDG.getAPI().tdg);

    private static final List<Player> view = new ArrayList<>();

    private static final List<Player> toHide = new ArrayList<>();

    private static final List<ArmorStand> icons = new ArrayList<>();

    @NotNull
    private final String name;

    @NotNull
    private final IconType iconType;

    @NotNull
    private final String material;

    private final byte materialData;

    @NotNull
    private final String texture;

    @NotNull
    private final List<Target<IconClickEvent>> clickTargets;

    @NotNull
    private final List<Target<IconHoverEvent>> hoverTargets;

    private final int positionX;

    private final int positionY;

    public BasicIcon(@NotNull String name, @NotNull IconType iconType, @NotNull String material,
                     byte materialData, @NotNull String texture, @NotNull List<Target<IconClickEvent>> clickTargets,
                     @NotNull List<Target<IconHoverEvent>> hoverTargets, int positionX, int positionY) {
        this.name = name;
        this.iconType = iconType;
        this.material = material;
        this.materialData = materialData;
        this.texture = texture;
        this.clickTargets = clickTargets;
        this.hoverTargets = hoverTargets;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    @NotNull
    @Override
    public LiveIcon createFor(@NotNull Player player, IntFunction<Location> function,
                              boolean changed) {
        final Location location = function.apply(positionX);
        final List<ArmorStand> armorStands;

        switch (iconType) {
            case BLOCK:
                final ItemStack block = new ItemStack(
                    XMaterial.matchXMaterial(material).parseMaterial(),
                    1,
                    materialData
                );
                armorStands = new ListOf<>(
                    addIconBlock(player, location, block)
                );
                break;
            case ITEM:
                final ItemStack item = new ItemStack(
                    XMaterial.matchXMaterial(material).parseMaterial(),
                    1,
                    materialData
                );
                armorStands = addIconItem(player, location, item);
                break;
            case TOOL:
                final ItemStack tool = new ItemStack(
                    XMaterial.matchXMaterial(material).parseMaterial(),
                    1,
                    materialData
                );
                armorStands = addIconTool(player, location, tool);
                break;
            case HEAD:
            default:
                armorStands = new ListOf<>(
                    addIconHead(player, location)
                );
                break;
        }

        return new BasicLiveIcon(
            armorStands,
            player,
            clickTargets,
            hoverTargets
        );
    }

    @NotNull
    private ArmorStand addIconBlock(@NotNull Player player, @NotNull Location location, @NotNull ItemStack itemStack) {
        final ArmorStand armorStand = player.getWorld().spawn(init(player, location), ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setCustomName(name.replace("&", "§"));
        armorStand.setCustomNameVisible(true);
        armorStand.setHelmet(itemStack);

        return initArmorStand(player, armorStand);
    }

    @NotNull
    private ArmorStand addIconHead(@NotNull Player player, @NotNull Location location) {
        final ArmorStand armorStand = player.getWorld().spawn(init(player, location), ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);

        if (texture.contains("textures.minecraft.net")) {
            armorStand.setHelmet(new Skull(texture).value());
            return initArmorStand(player, armorStand);
        }

        final ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
        final SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(texture.replace("%player%", player.getName()));
        skull.setItemMeta(meta);
        armorStand.setHelmet(skull);

        return initArmorStand(player, armorStand);
    }

    @NotNull
    private List<ArmorStand> addIconItem(@NotNull Player player, @NotNull Location location,
                                         @NotNull ItemStack itemStack) {
        final ArmorStand armorStand = player.getWorld().spawn(init(player, location), ArmorStand.class);
        final Location armorStandLocation = armorStand.getLocation();
        final Vector direction = armorStandLocation.getDirection();
        final Vector direction2 = direction.clone().setX(direction.getZ()).setZ(-direction.getX());

        armorStandLocation.setDirection(direction2);

        return initArmorStands(
            player,
            itemStack,
            armorStand,
            armorStandLocation,
            4.7,
            4.8,
            6.3
        );
    }

    @NotNull
    private List<ArmorStand> addIconTool(@NotNull Player player, @NotNull Location location,
                                         @NotNull ItemStack itemStack) {
        final ArmorStand armorStand = player.getWorld().spawn(init(player, location), ArmorStand.class);

        return initArmorStands(
            player,
            itemStack,
            armorStand,
            Utils.getLeftSide(armorStand.getLocation().add(0.0, -0.3, 0.0), 0.4),
            -1.1,
            1.7,
            1.4
        );
    }

    @NotNull
    private ArmorStand initArmorStand(@NotNull Player player, @NotNull ArmorStand armorStand) {
        armorStand.setArms(true);
        icons.add(armorStand);
        view.add(player);
        TDG.getAPI().entities.add(armorStand);
        Metadata.set(armorStand, "tdg", player.getUniqueId());
        Location locb = armorStand.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!armorStand.isValid()) {
                    return;
                }

                armorStand.teleport(locb);
                armorStand.setFireTicks(0);
                toHide.clear();

                for (Player all : Bukkit.getOnlinePlayers()) {
                    toHide.add(all);
                    toHide.remove(player);
                }

                for (Player hide : toHide) {
                    entityHider.hideEntity(hide, armorStand);
                }

                if (new Targeted(player).value() == armorStand) {
                    armorStand.setGravity(true);
                    armorStand.setVelocity(player.getLocation().toVector().subtract(armorStand.getLocation().toVector()).multiply(0.1));
                    armorStand.teleport(locb);
                } else {
                    armorStand.setGravity(false);
                }

                if (player.getLocation().distanceSquared(armorStand.getLocation()) >= 120) {
                    view.remove(player);
                }

                if (!player.isOnline()) {
                    TDG.getAPI().entities.remove(armorStand);
                    armorStand.remove();
                    view.remove(player);
                }

                if (!view.contains(player)) {
                    TDG.getAPI().entities.remove(armorStand);
                    armorStand.remove();
                    TDG.getAPI().opened.remove(player.getUniqueId());
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);

        return armorStand;
    }

    @NotNull
    private List<ArmorStand> initArmorStands(@NotNull Player player, @NotNull ItemStack itemStack,
                                             @NotNull ArmorStand armorStand, @NotNull Location location, double v3,
                                             double v4, double v5) {
        final ArmorStand armorStand2 = player.getWorld().spawn(location, ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand2.setVisible(false);
        armorStand2.setCustomName(name);
        armorStand2.setRightArmPose(new EulerAngle(v3, v4, v5));
        armorStand2.setItemInHand(itemStack);
        armorStand.setArms(true);
        armorStand2.setArms(true);
        icons.add(armorStand);
        icons.add(armorStand2);
        view.add(player);
        TDG.getAPI().entities.add(armorStand);
        TDG.getAPI().entities.add(armorStand2);
        Metadata.set(armorStand, "tdg", player.getUniqueId());
        Metadata.set(armorStand2, "tdg", player.getUniqueId());

        final Location locationB = armorStand.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!armorStand.isValid()) {
                    return;
                }

                armorStand.teleport(locationB);
                armorStand2.teleport(location);
                armorStand.setFireTicks(0);
                armorStand2.setFireTicks(0);
                toHide.clear();

                for (Player all : Bukkit.getOnlinePlayers()) {
                    toHide.add(all);
                    toHide.remove(player);
                }

                for (Player hide : toHide) {
                    entityHider.hideEntity(hide, armorStand);
                    entityHider.hideEntity(hide, armorStand2);
                }

                final Entity entity = new Targeted(player).value();

                if (entity == armorStand || entity == armorStand2) {
                    armorStand.setGravity(true);
                    armorStand.setVelocity(
                        player.getLocation().toVector().subtract(armorStand.getLocation().toVector()).multiply(0.5)
                    );
                    armorStand.teleport(locationB);
                    armorStand2.setGravity(true);
                    armorStand2.setVelocity(
                        player.getLocation().toVector().subtract(armorStand.getLocation().toVector()).multiply(0.5)
                    );
                    armorStand2.teleport(location);
                } else {
                    armorStand.setGravity(false);
                    armorStand2.setGravity(false);
                }

                if (player.getLocation().distanceSquared(armorStand.getLocation()) >= 120) {
                    view.remove(player);
                }

                if (!player.isOnline()) {
                    TDG.getAPI().entities.remove(armorStand);
                    TDG.getAPI().entities.remove(armorStand2);
                    armorStand.remove();
                    armorStand2.remove();
                    view.remove(player);
                }

                if (!view.contains(player)) {
                    TDG.getAPI().entities.remove(armorStand);
                    TDG.getAPI().entities.remove(armorStand2);
                    armorStand.remove();
                    armorStand2.remove();
                    TDG.getAPI().opened.remove(player.getUniqueId());
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);

        return new ListOf<>(armorStand, armorStand2);
    }

    @NotNull
    private Location init(@NotNull Player player, @NotNull Location location) {
        final Vector playerDirection = player.getLocation().getDirection();
        final Vector direction = playerDirection.normalize();

        direction.multiply(-2);
        location.setDirection(direction);

        final float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        final float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;

        location.setYaw(yaw);
        location.setPitch(pitch);

        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }

        return location;
    }

}
