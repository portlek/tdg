package io.github.portlek.tdg.created;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.created.util.FinishInitiating;
import io.github.portlek.tdg.created.util.InitiatedIcon;
import io.github.portlek.tdg.created.util.RunCreated;
import io.github.portlek.tdg.created.util.SetupArmorStand;
import io.github.portlek.tdg.util.Metadata;
import io.github.portlek.tdg.util.Utils;
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

public class ToolIconCreated implements Scalar<List<ArmorStand>> {

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

    public ToolIconCreated(@NotNull Player player, @NotNull Location location, @NotNull String name, @NotNull ItemStack itemStack, int positionY) {
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
            Utils.getLeftSide(armorStand.getLocation().add(0.0, -0.3, 0.0), 0.4),
            ArmorStand.class
        );

        new SetupArmorStand(armorStand, player, name).run();
        armorStand.setCustomNameVisible(true);
        new SetupArmorStand(armorStand2, player, name).run();
        armorStand2.setRightArmPose(new EulerAngle(-1.1, 1.7, 1.4));
        armorStand2.setItemInHand(itemStack);
        new FinishInitiating(armorStand, player).exec(view, toHide);
        new FinishInitiating(armorStand2, player).exec(view, toHide);

        return new ListOf<>(
            armorStand,
            armorStand2
        );
    }

}
