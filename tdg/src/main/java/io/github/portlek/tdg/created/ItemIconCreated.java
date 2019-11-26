package io.github.portlek.tdg.created;

import io.github.portlek.tdg.created.util.FinishInitiating;
import io.github.portlek.tdg.created.util.InitiatedIcon;
import io.github.portlek.tdg.created.util.SetupArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemIconCreated implements Scalar<List<ArmorStand>> {

    private final List<Player> view = new ArrayList<>();

    private final List<Player> toHide = new ArrayList<>();

    @NotNull
    private final Player player;

    @NotNull
    private final Location location;

    @NotNull
    private final String name;

    @NotNull
    private final ItemStack itemStack;

    private final int positionY;

    public ItemIconCreated(@NotNull Player player, @NotNull Location location, @NotNull String name, @NotNull ItemStack itemStack, int positionY) {
        this.player = player;
        this.location = location;
        this.name = name;
        this.itemStack = itemStack;
        this.positionY = positionY;
    }

    @Override
    public List<ArmorStand> value() {
        final ArmorStand armorStand = player.getWorld().spawn(
            new InitiatedIcon(player, location, positionY).value(),
            ArmorStand.class
        );

        new SetupArmorStand(armorStand, player, name).run();
        armorStand.setCustomNameVisible(true);
        armorStand.setRightArmPose(new EulerAngle(4.7, 0, 6.3));
        armorStand.setItemInHand(itemStack);
        new FinishInitiating(player, armorStand).exec(view, toHide);

        return new ListOf<>(
            armorStand
        );
    }

}
