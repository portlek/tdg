package io.github.portlek.tdg.created;

import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.created.util.FinishInitiating;
import io.github.portlek.tdg.created.util.InitiatedIcon;
import io.github.portlek.tdg.created.util.SetupArmorStand;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class HeadIconCreated implements Scalar<ArmorStand> {

    private final List<Player> view = new ArrayList<>();

    private final List<Player> toHide = new ArrayList<>();

    @NotNull
    private final Player player;

    @NotNull
    private final Location location;

    @NotNull
    private final String texture;

    private final int positionY;

    @NotNull
    private final String name;

    public HeadIconCreated(@NotNull Player player, @NotNull Location location, @NotNull String texture,
                           int positionY, @NotNull String name) {
        this.player = player;
        this.location = location;
        this.texture = texture;
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

        new SetupArmorStand(armorStand, player, name).run();
        armorStand.setCustomNameVisible(true);

        if (texture.contains("textures.minecraft.net")) {
            armorStand.setHelmet(TDG.SKULL.apply(texture));
        } else {
            final ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short)3);
            final SkullMeta meta = (SkullMeta)skull.getItemMeta();

            meta.setOwner(
                TDG.getAPI().getConfig().hooksPlaceholderAPI
                    ? PlaceholderAPI.setPlaceholders(player, texture)
                    : texture.replaceAll("%player_name%", player.getName())
            );
            skull.setItemMeta(meta);
            armorStand.setHelmet(skull);
        }

        new FinishInitiating(player, armorStand).run();

        return armorStand;
    }

}
