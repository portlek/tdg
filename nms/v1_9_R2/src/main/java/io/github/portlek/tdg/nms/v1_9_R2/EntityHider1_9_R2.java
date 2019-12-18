package io.github.portlek.tdg.nms.v1_9_R2;

import io.github.portlek.tdg.api.EntityHided;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityHider1_9_R2 implements EntityHided {

    @Override
    public void hide(@NotNull Player player, @NotNull Entity en) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(
            new PacketPlayOutEntityDestroy(en.getEntityId())
        );
    }

}
