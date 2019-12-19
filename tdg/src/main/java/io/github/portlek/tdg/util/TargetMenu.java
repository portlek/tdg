package io.github.portlek.tdg.util;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import org.bukkit.entity.Entity;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class TargetMenu implements Scalar<OpenedMenu> {

    @NotNull
    private final Scalar<Entity> targeted;

    public TargetMenu(@NotNull Scalar<Entity> targeted) {
        this.targeted = targeted;
    }

    public TargetMenu(@NotNull Entity entity) {
        this(() -> entity);
    }

    /**
     * gives target menu of player
     * @return if target menu is null, returns {@link MckOpenMenu}
     */
    @Override
    public OpenedMenu value() {
        try {
            final Object object = Metadata.get(targeted.value(), "tdg");

            if (object instanceof UUID) {
                return TDG.getAPI().menus.findOpenMenuByUUID((UUID) object);
            }
        } catch (Exception ignored) {
            // ignored
        }

        return new MckOpenMenu();
    }

}
