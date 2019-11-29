package io.github.portlek.tdg.icon;

import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.tdg.api.Icon;
import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.type.IconType;
import io.github.portlek.tdg.created.BlockIconCreated;
import io.github.portlek.tdg.created.HeadIconCreated;
import io.github.portlek.tdg.created.ItemIconCreated;
import io.github.portlek.tdg.created.ToolIconCreated;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public final class BasicIcon implements Icon {

    @NotNull
    private final String id;

    @NotNull
    private final String name;

    @NotNull
    private final IconType iconType;

    @NotNull
    private final String material;

    private final byte materialData;

    @NotNull
    private final String texture;

    @NotNull
    private final List<Target<IconClickEvent>> clickTargets;

    @NotNull
    private final List<Target<IconHoverEvent>> hoverTargets;

    private final int positionX;

    private final int positionY;

    public BasicIcon(@NotNull String id, @NotNull String name, @NotNull IconType iconType, @NotNull String material,
                     byte materialData, @NotNull String texture, @NotNull List<Target<IconClickEvent>> clickTargets,
                     @NotNull List<Target<IconHoverEvent>> hoverTargets, int positionX, int positionY) {
        this.id = id;
        this.name = name;
        this.iconType = iconType;
        this.material = material;
        this.materialData = materialData;
        this.texture = texture;
        this.clickTargets = clickTargets;
        this.hoverTargets = hoverTargets;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    @NotNull
    @Override
    public LiveIcon createFor(@NotNull Player player, IntFunction<Location> function,
                              boolean changed) {
        final Location location = function.apply(positionX);
        final List<ArmorStand> armorStands = new ArrayList<>();

        switch (iconType) {
            case BLOCK:
                armorStands.add(
                    new BlockIconCreated(
                        player,
                        location,
                        new ItemStack(
                            XMaterial.matchXMaterial(material).parseMaterial(),
                            1,
                            materialData
                        ),
                        positionY,
                        name
                    ).value()
                );
                break;
            case ITEM:
                armorStands.addAll(
                    new ItemIconCreated(
                        player,
                        location,
                        name,
                        new ItemStack(
                            XMaterial.matchXMaterial(material).parseMaterial(),
                            1,
                            materialData
                        ),
                        positionY
                    ).value()
                );
                break;
            case TOOL:
                armorStands.addAll(
                    new ToolIconCreated(
                        player,
                        location,
                        name,
                        new ItemStack(
                            XMaterial.matchXMaterial(material).parseMaterial(),
                            1,
                            materialData
                        ),
                        positionY
                    ).value()
                );
                break;
            case HEAD:
                armorStands.add(
                    new HeadIconCreated(
                        player,
                        location,
                        texture,
                        positionY,
                        name
                    ).value()
                );
                break;
            default:
                break;
        }

        return new BasicLiveIcon(
            this,
            armorStands,
            clickTargets,
            hoverTargets
        );
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void exec(IconHoverEvent event) {
        hoverTargets.forEach(target -> target.handle(event));
    }

    @Override
    public void accept(IconClickEvent event) {
        clickTargets.forEach(target -> target.handle(event));
    }

}
