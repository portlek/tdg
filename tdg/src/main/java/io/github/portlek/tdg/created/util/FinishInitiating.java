package io.github.portlek.tdg.created.util;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.util.Metadata;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.cactoos.BiProc;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class FinishInitiating implements BiProc<List<Player>, List<Player>> {

    @NotNull
    private final Player player;

    @NotNull
    private final List<ArmorStand> armorStands;

    public FinishInitiating(@NotNull Player player, @NotNull ArmorStand... armorStands) {
        this.player = player;
        this.armorStands = new ListOf<>(armorStands);
    }

    @Override
    public void exec(@NotNull List<Player> view, @NotNull List<Player> toHide) {
        if (armorStands.isEmpty()) {
            return;
        }

        final ArmorStand armorStand = armorStands.get(0);

        for (ArmorStand as : armorStands) {
            as.setArms(true);
            view.add(player);
            TDG.getAPI().entities.add(as);
            Metadata.set(as, "tdg", player.getUniqueId());
        }

        new RunCreated(
            armorStands,
            player,
            armorStand.getLocation()
        ).exec(view, toHide);
    }

}
