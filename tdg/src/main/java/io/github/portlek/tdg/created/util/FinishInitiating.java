package io.github.portlek.tdg.created.util;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.icon.BasicLiveIcon;
import io.github.portlek.tdg.util.Metadata;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class FinishInitiating implements Runnable {

    @NotNull
    private final Player player;

    @NotNull
    private final List<ArmorStand> armorStands;

    public FinishInitiating(@NotNull Player player, @NotNull ArmorStand... armorStands) {
        this.player = player;
        this.armorStands = new ListOf<>(armorStands);
    }

    @Override
    public void run() {
        if (armorStands.isEmpty()) {
            return;
        }

        for (ArmorStand as : armorStands) {
            as.setArms(true);
            BasicLiveIcon.view.add(player);
            TDG.getAPI().entities.add(as);
            Metadata.set(as, "tdg", player.getUniqueId());
        }

        new RunCreated(
            armorStands,
            player,
            armorStands.get(0).getLocation()
        ).run();
    }

}
