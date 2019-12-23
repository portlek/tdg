package io.github.portlek.tdg.created.util;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.hooks.PlaceholderAPIWrapper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SetupArmorStand implements Runnable {

    @NotNull
    private final ArmorStand armorStand;

    @NotNull
    private final Player player;

    @NotNull
    private final String name;

    public SetupArmorStand(@NotNull ArmorStand armorStand, @NotNull Player player, @NotNull String name) {
        this.armorStand = armorStand;
        this.player = player;
        this.name = name;
    }

    @Override
    public void run() {
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomName(name.replace("%player_name%", player.getName()));
        TDG.getAPI().config.getWrapped("PlaceholderAPI").ifPresent(wrapped ->
            ((PlaceholderAPIWrapper)wrapped).apply(player, name)
        );
    }

}
