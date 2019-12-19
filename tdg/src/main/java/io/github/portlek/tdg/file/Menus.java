package io.github.portlek.tdg.file;

import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.mock.MckLiveIcon;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import io.github.portlek.tdg.util.TargetMenu;
import io.github.portlek.tdg.util.Targeted;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cactoos.map.MapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Menus {

    public final Map<UUID, OpenedMenu> opened = new HashMap<>();

    public final Map<Player, Location> lastLocations = new HashMap<>();

    public final List<Entity> entities = new ArrayList<>();

    @NotNull
    public final Map<String, Menu> menus;

    public Menus(@NotNull Map<String, Menu> menus) {
        this.menus = menus;
    }

    public void clear() {
        opened.values().forEach(OpenedMenu::close);
        entities.forEach(Entity::remove);
        entities.clear();
        menus.clear();
        opened.clear();
    }

    @NotNull
    public Menu findMenuByCommand(@NotNull String command) {
        for (Menu menu : menus.values()) {
            if (menu.is(command)) {
                return menu;
            }
        }

        return new MckMenu();
    }

    @NotNull
    public Menu findMenuById(@NotNull String id) {
        return menus.getOrDefault(id, new MckMenu());
    }

    @NotNull
    public OpenedMenu findOpenMenuByUUID(@NotNull UUID uuid) {
        return opened.getOrDefault(uuid, new MckOpenMenu());
    }

    @NotNull
    public Optional<Map.Entry<OpenedMenu, LiveIcon>> getIconOptional(@NotNull Player player) {
        final Map.Entry<LiveIcon, OpenedMenu> entry = findIconAndOpenedMenuByPlayer(player);
        final LiveIcon liveIcon = entry.getKey();
        final OpenedMenu openedMenu = entry.getValue();

        if (liveIcon instanceof MckLiveIcon || openedMenu instanceof MckOpenMenu) {
            return Optional.empty();
        }

        return Optional.of(
            new MapEntry<>(
                openedMenu,
                liveIcon
            )
        );
    }

    @NotNull
    public Map.Entry<LiveIcon, OpenedMenu> findIconAndOpenedMenuByPlayer(@NotNull Player player) {
        final Entity targeted = new Targeted(player).value();

        if (targeted == null) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        final OpenedMenu openedMenu = new TargetMenu(
            targeted
        ).value();

        if (openedMenu instanceof MckOpenMenu) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        final LiveIcon liveIcon = openedMenu.findByEntity(targeted);

        if (liveIcon instanceof MckLiveIcon) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        return new MapEntry<>(liveIcon, openedMenu);
    }

}
