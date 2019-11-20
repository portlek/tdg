package io.github.portlek.tdg.util;

import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.tdg.TDG;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class IconUtil {

    private final List<Player> view = new ArrayList<>();

    private final List<Player> toHide = new ArrayList<>();

    private final List<ArmorStand> icons = new ArrayList<>();

    private final EntityHider entityHider = new EntityHider(TDG.getAPI().tdg);

    public void addIconHead(@NotNull Player player, @NotNull Location location, String name, @NotNull ItemStack itemStack,
                            int positionY) {
        Vector playerDirection = player.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        location.setDirection(direction);
        float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        location.setYaw(yaw);
        location.setPitch(pitch);
        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }
        ArmorStand a = player.getWorld().spawn(location, ArmorStand.class);
        a.setVisible(false);
        a.setCustomName(name.replace("&", "§"));
        a.setCustomNameVisible(true);
        a.setHelmet(itemStack);
        a.setArms(true);
        icons.add(a);
        view.add(player);
        TDG.getAPI().entities.add(a);
        Metadata.set(a, player.getName(), player);
        Location locb = a.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (a.isValid()) {
                    a.teleport(locb);
                    a.setFireTicks(0);
                    toHide.clear();

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        toHide.add(all);
                        toHide.remove(player);
                    }

                    for (Player hide : toHide) {
                        entityHider.hideEntity(hide, a);
                    }

                    if (new Targeted(player).value() == a) {
                        a.setGravity(true);
                        a.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a.teleport(locb);
                    } else {
                        a.setGravity(false);
                    }

                    if (player.getLocation().distanceSquared(a.getLocation()) >= 120) {
                        view.remove(player);
                    }

                    if (!player.isOnline()) {
                        TDG.getAPI().entities.remove(a);
                        a.remove();
                        view.remove(player);
                    }

                    if (!view.contains(player)) {
                        TDG.getAPI().entities.remove(a);
                        a.remove();
                        TDG.getAPI().opened.remove(player.getUniqueId(), "none");
                    }
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);
    }

    public void addIconHead(@NotNull Player player, @NotNull Location location, @NotNull String name,
                            @NotNull String texture, int positionY) {
        Vector playerDirection = player.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        location.setDirection(direction);
        float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        location.setYaw(yaw);
        location.setPitch(pitch);
        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }
        ArmorStand a = player.getWorld().spawn(location, ArmorStand.class);
        a.setVisible(false);
        a.setCustomName(name.replace("&", "§"));
        a.setCustomNameVisible(true);
        if (texture.contains("textures.minecraft.net")) {
            a.setHelmet(new Skull(texture).value());
        } else {
            ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(texture.replace("%player%", player.getName()));
            skull.setItemMeta(meta);
            a.setHelmet(skull);
        }
        a.setArms(true);
        icons.add(a);
        view.add(player);
        TDG.getAPI().entities.add(a);
        Metadata.set(a, player.getName(), player);
        Location locb = a.getLocation();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (a.isValid()) {
                    a.teleport(locb);
                    a.setFireTicks(0);
                    toHide.clear();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        toHide.add(all);
                        toHide.remove(player);
                    }
                    for (Player hide : toHide) {
                        entityHider.hideEntity(hide, a);
                    }
                    if (new Targeted(player).value() == a) {
                        a.setGravity(true);
                        a.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a.teleport(locb);
                    } else {
                        a.setGravity(false);
                    }
                    if (player.getLocation().distanceSquared(a.getLocation()) >= 120) {
                        view.remove(player);
                    }
                    if (!player.isOnline()) {
                        TDG.getAPI().entities.remove(a);
                        a.remove();
                        view.remove(player);
                    }
                    if (!view.contains(player)) {
                        TDG.getAPI().entities.remove(a);
                        a.remove();
                        TDG.getAPI().opened.remove(player.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);
    }

    public void addIconItem(@NotNull Player player, @NotNull Location location, @NotNull String name,
                            @NotNull ItemStack item, int positionY) {
        Vector playerDirection = player.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        location.setDirection(direction);
        float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        location.setYaw(yaw);
        location.setPitch(pitch);
        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }
        ArmorStand a = player.getWorld().spawn(location, ArmorStand.class);
        Vector v = a.getLocation().getDirection();
        Vector v2 = v.clone().setX(v.getZ()).setZ(-v.getX());
        Location locs = a.getLocation();
        locs.setDirection(v2);
        ArmorStand a2 = player.getWorld().spawn(locs, ArmorStand.class);
        a.setVisible(false);
        a.setCustomName(name.replace("&", "§"));
        a.setCustomNameVisible(true);
        a2.setVisible(false);
        a2.setCustomName(name.replace("&", "§"));
        a2.setRightArmPose(new EulerAngle(4.7, 4.8, 6.3));
        a2.setItemInHand(item);
        a.setArms(true);
        a2.setArms(true);
        icons.add(a);
        icons.add(a2);
        view.add(player);
        TDG.getAPI().entities.add(a);
        TDG.getAPI().entities.add(a2);
        Metadata.set(a, player.getName(), player);
        Metadata.set(a2, player.getName(), player);
        Location locb = a.getLocation();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (a.isValid()) {
                    a.teleport(locb);
                    a2.teleport(locs);
                    a.setFireTicks(0);
                    a2.setFireTicks(0);
                    toHide.clear();

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        toHide.add(all);
                        toHide.remove(player);
                    }

                    for (Player hide : toHide) {
                        entityHider.hideEntity(hide, a);
                        entityHider.hideEntity(hide, a2);
                    }

                    final Entity targetd = new Targeted(player).value();
                    if (targetd == a || targetd == a2) {
                        a.setGravity(true);
                        a.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a.teleport(locb);
                        a2.setGravity(true);
                        a2.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a2.teleport(locs);
                    } else {
                        a.setGravity(false);
                        a2.setGravity(false);
                    }

                    if (player.getLocation().distanceSquared(a.getLocation()) >= 120) {
                        view.remove(player);
                    }

                    if (!player.isOnline()) {
                        TDG.getAPI().entities.remove(a);
                        TDG.getAPI().entities.remove(a2);
                        a.remove();
                        a2.remove();
                        view.remove(player);
                    }

                    if (!view.contains(player)) {
                        TDG.getAPI().entities.remove(a);
                        TDG.getAPI().entities.remove(a2);
                        a.remove();
                        a2.remove();
                        TDG.getAPI().opened.remove(player.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);
    }

    public void addIconTool(@NotNull Player player, @NotNull Location location, @NotNull String name,
                            @NotNull ItemStack item, int positionY) {
        Vector playerDirection = player.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        location.setDirection(direction);
        float yaw = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        float pitch = (float) Math.toDegrees(Math.atan2(player.getLocation().getZ() - location.getZ(), player.getLocation().getX() - location.getX())) - 90;
        location.setYaw(yaw);
        location.setPitch(pitch);
        if (positionY == 2) {
            location.add(0.0, 1.5, 0.0);
        }
        ArmorStand a = player.getWorld().spawn(location, ArmorStand.class);
        Location locs = Utils.getLeftSide(a.getLocation().add(0.0, -0.3, 0.0), 0.4);
        ArmorStand a2 = player.getWorld().spawn(locs, ArmorStand.class);
        a.setVisible(false);
        a.setCustomName(name.replace("&", "§"));
        a.setCustomNameVisible(true);
        a2.setVisible(false);
        a2.setCustomName(name.replace("&", "§"));
        a2.setRightArmPose(new EulerAngle(-1.1, 1.7, 1.4));
        a2.setItemInHand(item);
        a.setArms(true);
        a2.setArms(true);
        icons.add(a);
        icons.add(a2);
        view.add(player);
        TDG.getAPI().entities.add(a);
        TDG.getAPI().entities.add(a2);
        Metadata.set(a, player.getName(), player);
        Metadata.set(a2, player.getName(), player);
        Location locb = a.getLocation();

        new BukkitRunnable() {

            @Override
            public void run() {
                if (a.isValid()) {
                    a.teleport(locb);
                    a2.teleport(locs);
                    a.setFireTicks(0);
                    a2.setFireTicks(0);

                    toHide.clear();
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        toHide.add(all);
                        toHide.remove(player);
                    }

                    for (Player hide : toHide) {
                        entityHider.hideEntity(hide, a);
                        entityHider.hideEntity(hide, a2);
                    }

                    final Entity targetd = new Targeted(player).value();
                    if (targetd == a || targetd == a2) {
                        a.setGravity(true);
                        a.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a.teleport(locb);
                        a2.setGravity(true);
                        a2.setVelocity(player.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
                        a2.teleport(locs);
                    } else {
                        a.setGravity(false);
                        a2.setGravity(false);
                    }

                    if (player.getLocation().distanceSquared(a.getLocation()) >= 120) {
                        view.remove(player);
                    }

                    if (!player.isOnline()) {
                        TDG.getAPI().entities.remove(a);
                        TDG.getAPI().entities.remove(a2);
                        a.remove();
                        a2.remove();
                        view.remove(player);
                    }

                    if (!view.contains(player)) {
                        TDG.getAPI().entities.remove(a);
                        TDG.getAPI().entities.remove(a2);
                        a.remove();
                        a2.remove();
                        TDG.getAPI().opened.remove(player.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 0);
    }

}
