package io.github.portlek.tdg.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.reflection.clazz.ClassOf;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Skull implements Scalar<ItemStack> {

    @NotNull
    private final String url;

    public Skull(@NotNull String url) {
        this.url = url;
    }

    @Override
    public ItemStack value() {
        final ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);

        if(url.isEmpty()) {
            return item;
        }

        final SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        if (itemMeta == null) {
            return item;
        }

        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        final byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        new ClassOf(itemMeta)
            .getField("profile")
            .of(itemMeta)
            .set(profile);
        item.setItemMeta(itemMeta);

        return item;
    }

}