package io.github.portlek.tdg.created.util;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.mock.MckLiveIcon;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import io.github.portlek.tdg.icon.BasicLiveIcon;
import io.github.portlek.tdg.util.TargetMenu;
import io.github.portlek.tdg.util.Targeted;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RunCreated implements Runnable {

    private final boolean hoverEffect = TDG.getAPI().config.hoverEffect;

    private final AtomicBoolean looking = new AtomicBoolean(false);

    @NotNull
    private final List<ArmorStand> armorStands;

    @NotNull
    private final Player player;

    @NotNull
    private final Location oldLocation;

    public RunCreated(@NotNull List<ArmorStand> armorStands, @NotNull Player player, @NotNull Location oldLocation) {
        this.armorStands = armorStands;
        this.player = player;
        this.oldLocation = oldLocation;
    }

    @Override
    public void run() {
        if (armorStands.isEmpty()) {
            return;
        }

        final ArmorStand armorStand = armorStands.get(0);
        ArmorStand temp = null;

        try {
            temp = armorStands.get(1);
        } catch (Exception ignored) {
            // ignored
        }

        final Optional<ArmorStand> armorStand2 = Optional.ofNullable(temp);
        final Optional<Location> oldLocation1 = armorStand2.map(Entity::getLocation);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!armorStand.isValid() || (armorStand2.isPresent() && !armorStand2.get().isValid())) {
                    cancel();
                    return;
                }
                
                armorStand2.ifPresent(armorStand1 -> armorStand1.setFireTicks(0));
                armorStand.setFireTicks(0);
                BasicLiveIcon.toHide.clear();
                BasicLiveIcon.toHide.addAll(Bukkit.getOnlinePlayers());
                BasicLiveIcon.toHide.remove(player);

                for (Player hide : BasicLiveIcon.toHide) {
                    TDG.ENTITY_HIDED.hide(hide, armorStand);
                    armorStand2.ifPresent(armorStand1 -> TDG.ENTITY_HIDED.hide(hide, armorStand1));
                }

                final Entity entity = new Targeted(player).value();

                if (hoverEffect) {
                    if (entity == armorStand) {
                        if (!looking.get()) {
                            armorStand.setGravity(true);
                            armorStand.setVelocity(
                                player.getLocation().toVector().subtract(armorStand.getLocation().toVector()).multiply(0.1)
                            );
                            armorStand2.ifPresent(armorStand1 -> {
                                armorStand1.setGravity(true);
                                armorStand1.setVelocity(
                                    player.getLocation().toVector().subtract(armorStand.getLocation().toVector()).multiply(0.1)
                                );
                            });
                            looking.set(true);

                            final OpenedMenu openedMenu = new TargetMenu(entity).value();
                            final LiveIcon liveIcon = openedMenu.findByEntity(entity);

                            if (!(liveIcon instanceof MckLiveIcon)) {
                                final IconHoverEvent iconHoverEvent = new IconHoverEvent(
                                    player,
                                    openedMenu,
                                    liveIcon
                                );

                                Bukkit.getServer().getPluginManager().callEvent(iconHoverEvent);

                                if (iconHoverEvent.isCancelled()) {
                                    return;
                                }

                                liveIcon.getParent().exec(iconHoverEvent);
                            }
                        } else {
                            armorStand.setGravity(false);
                            armorStand2.ifPresent(armorStand1 -> armorStand1.setGravity(false));
                        }
                    } else {
                        armorStand.setGravity(true);
                        armorStand.teleport(oldLocation);
                        armorStand.setGravity(false);
                        armorStand2.ifPresent(armorStand1 -> {
                            armorStand1.setGravity(true);
                            oldLocation1.ifPresent(armorStand1::teleport);
                            armorStand1.setGravity(false);
                        });
                        looking.set(false);
                    }
                }

                if (player.getLocation().distanceSquared(armorStand.getLocation()) >= 120 ||
                    (armorStand2.isPresent() &&
                        player.getLocation().distanceSquared(armorStand2.get().getLocation()) >= 120)) {
                    BasicLiveIcon.view.remove(player);
                }

                if (!player.isOnline()) {
                    BasicLiveIcon.view.remove(player);
                }

                if (!BasicLiveIcon.view.contains(player)) {
                    final OpenedMenu openedMenu = TDG.getAPI().menus.opened.getOrDefault(player.getUniqueId(), new MckOpenMenu());

                    if (openedMenu instanceof MckOpenMenu) {
                        TDG.getAPI().menus.entities.remove(armorStand);
                        armorStand.remove();
                        armorStand2.ifPresent(armorStand1 -> {
                            TDG.getAPI().menus.entities.remove(armorStand1);
                            armorStand1.remove();
                        });
                    } else {
                        openedMenu.close();
                        TDG.getAPI().menus.opened.remove(player.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(TDG.getAPI().tdg, 0, 1L);
    }

}
