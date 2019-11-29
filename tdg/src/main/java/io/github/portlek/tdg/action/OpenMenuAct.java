package io.github.portlek.tdg.action;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Icon;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.util.Metadata;
import io.github.portlek.tdg.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class OpenMenuAct implements Consumer<MenuOpenEvent> {

    @NotNull
    private final List<Icon> icons;

    private final int x1;

    private final int x2;

    private final int x4;

    private final int x5;

    public OpenMenuAct(@NotNull List<Icon> icons, int x1, int x2, int x4, int x5) {
        this.icons = icons;
        this.x1 = x1;
        this.x2 = x2;
        this.x4 = x4;
        this.x5 = x5;
    }

    @Override
    public void accept(MenuOpenEvent event) {
        final Player player = event.getPlayer();

        for (Entity en : player.getWorld().getEntities()) {
            if (Metadata.hasKey(en, "tdg")) {
                en.remove();
                TDG.getAPI().entities.remove(en);
            }
        }

        final Location location;

        if (event.isChanged()) {
            location = TDG.getAPI().lastLocations.get(player);
        } else {
            location = Utils.getBFLoc(player.getLocation(), 3.5);
            TDG.getAPI().lastLocations.put(player, location);
        }

        event.getOpenedMenu().addIcons(
            new Mapped<>(
                icon -> icon.createFor(
                    player,
                    positionX -> Utils.setPosition(location, positionX, x1, x2, x4, x5),
                    event.isChanged()
                ),
                icons
            )
        );
        TDG.getAPI().opened.put(player.getUniqueId(), event.getOpenedMenu());
    }

}
