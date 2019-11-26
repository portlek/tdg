package io.github.portlek.tdg.created;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.created.util.FinishInitiating;
import io.github.portlek.tdg.created.util.InitiatedIcon;
import io.github.portlek.tdg.created.util.RunCreated;
import io.github.portlek.tdg.created.util.SetupArmorStand;
import io.github.portlek.tdg.util.Metadata;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BlockIconCreated implements Scalar<ArmorStand> {

    private final List<Player> view = new ArrayList<>();

    private final List<Player> toHide = new ArrayList<>();

    @NotNull
    private final Player player;

    @NotNull
    private final Location location;

    @NotNull
    private final ItemStack itemStack;

    private final int positionY;

    @NotNull
    private final String name;

    public BlockIconCreated(@NotNull Player player, @NotNull Location location, @NotNull ItemStack itemStack, int positionY, @NotNull String name) {
        this.player = player;
        this.location = location;
        this.itemStack = itemStack;
        this.positionY = positionY;
        this.name = name;
    }

    @Override
    @NotNull
    public ArmorStand value() {
        final ArmorStand armorStand = player.getWorld().spawn(
            new InitiatedIcon(player, location, positionY).value(),
            ArmorStand.class
        );

        new SetupArmorStand(armorStand, player, name);
        armorStand.setHelmet(itemStack);
        new FinishInitiating(armorStand, player).exec(view, toHide);

        return armorStand;
    }

}
