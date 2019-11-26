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
    private final ArmorStand armorStand;

    @NotNull
    private final Player player;

    public FinishInitiating(@NotNull ArmorStand armorStand, @NotNull Player player) {
        this.armorStand = armorStand;
        this.player = player;
    }

    @Override
    public void exec(@NotNull List<Player> view, @NotNull List<Player> toHide) {
        armorStand.setArms(true);
        view.add(player);
        TDG.getAPI().entities.add(armorStand);
        Metadata.set(armorStand, "tdg", player.getUniqueId());
        new RunCreated(
            new ListOf<>(armorStand),
            player,
            armorStand.getLocation()
        ).exec(view, toHide);
    }

}
