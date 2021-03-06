package io.github.portlek.tdg.created;

import io.github.portlek.tdg.created.util.FinishInitiating;
import io.github.portlek.tdg.created.util.InitiatedIcon;
import io.github.portlek.tdg.created.util.SetupArmorStand;
import io.github.portlek.tdg.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ItemIconCreated implements Scalar<List<ArmorStand>> {

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
        final ArmorStand armorStand2 = player.getWorld().spawn(
            Utils.getLeftSide(armorStand.getLocation(), -0.5),
            ArmorStand.class
        );

        new SetupArmorStand(armorStand, player, name).run();
        new SetupArmorStand(armorStand2, player, name).run();
        armorStand2.setCustomNameVisible(true);
        armorStand.setRightArmPose(new EulerAngle(4.7, 0, 6.3));
        armorStand.setItemInHand(itemStack);
        new FinishInitiating(player, armorStand, armorStand2).run();

        return new ListOf<>(
            armorStand,
            armorStand2
        );
    }

}
