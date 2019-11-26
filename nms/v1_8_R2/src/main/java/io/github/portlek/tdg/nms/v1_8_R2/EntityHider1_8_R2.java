package io.github.portlek.tdg.nms.v1_8_R2;

import io.github.portlek.tdg.api.EntityHided;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityHider1_8_R2 implements EntityHided {

    @Override
    public void hide(@NotNull Player player, @NotNull Entity en) {
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(en.getEntityId());
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

}
