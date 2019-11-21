package io.github.portlek.tdg.particle;

import io.github.portlek.itemstack.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public enum ParticleEffect {

    EXPLOSION_NORMAL("explode", 0, -1, ParticleProperty.DIRECTIONAL),
    EXPLOSION_LARGE("largeexplode", 1, -1),
    EXPLOSION_HUGE("hugeexplosion", 2, -1),
    FIREWORKS_SPARK("fireworksSpark", 3, -1, ParticleProperty.DIRECTIONAL),
    WATER_BUBBLE("bubble", 4, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
    WATER_SPLASH("splash", 5, -1, ParticleProperty.DIRECTIONAL),
    WATER_WAKE("wake", 6, 7, ParticleProperty.DIRECTIONAL),
    SUSPENDED("suspended", 7, -1, ParticleProperty.REQUIRES_WATER),
    SUSPENDED_DEPTH("depthSuspend", 8, -1, ParticleProperty.DIRECTIONAL),
    CRIT("crit", 9, -1, ParticleProperty.DIRECTIONAL),
    CRIT_MAGIC("magicCrit", 10, -1, ParticleProperty.DIRECTIONAL),
    SMOKE_NORMAL("smoke", 11, -1, ParticleProperty.DIRECTIONAL),
    SMOKE_LARGE("largesmoke", 12, -1, ParticleProperty.DIRECTIONAL),
    SPELL("spell", 13, -1),
    SPELL_INSTANT("instantSpell", 14, -1),
    SPELL_MOB("mobSpell", 15, -1, ParticleProperty.COLORABLE),
    SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, ParticleProperty.COLORABLE),
    SPELL_WITCH("witchMagic", 17, -1),
    DRIP_WATER("dripWater", 18, -1),
    DRIP_LAVA("dripLava", 19, -1),
    VILLAGER_ANGRY("angryVillager", 20, -1),
    VILLAGER_HAPPY("happyVillager", 21, -1, ParticleProperty.DIRECTIONAL),
    TOWN_AURA("townaura", 22, -1, ParticleProperty.DIRECTIONAL),
    NOTE("note", 23, -1, ParticleProperty.COLORABLE),
    PORTAL("portal", 24, -1, ParticleProperty.DIRECTIONAL),
    ENCHANTMENT_TABLE("enchantmenttable", 25, -1, ParticleProperty.DIRECTIONAL),
    FLAME("flame", 26, -1, ParticleProperty.DIRECTIONAL),
    LAVA("lava", 27, -1),
    FOOTSTEP("footstep", 28, -1),
    CLOUD("cloud", 29, -1, ParticleProperty.DIRECTIONAL),
    REDSTONE("reddust", 30, -1, ParticleProperty.COLORABLE),
    SNOWBALL("snowballpoof", 31, -1),
    SNOW_SHOVEL("snowshovel", 32, -1, ParticleProperty.DIRECTIONAL),
    SLIME("slime", 33, -1),
    HEART("heart", 34, -1),
    BARRIER("barrier", 35, 8),
    ITEM_CRACK("iconcrack", 36, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    BLOCK_CRACK("blockcrack", 37, -1, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    BLOCK_DUST("blockdust", 38, 7, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
    WATER_DROP("droplet", 39, 8),
    ITEM_TAKE("take", 40, 8),
    MOB_APPEARANCE("mobappearance", 41, 8),
    DRAGONBREATH("dragonbreath", 42, 9),
    ENDROD("endrod", 43, 9),
    DAMAGEINDICATOR("damageindicator", 44, 9),
    SWEEPATTACK("sweepattack", 45, 9),
    FALLINGDUST("fallingdust", 46, 10, ParticleProperty.REQUIRES_DATA);

    private static final Map<String, ParticleEffect> NAME_MAP;
    private static final Map<Integer, ParticleEffect> ID_MAP;

    @NotNull
    private final String name;

    private final int id;

    private final int requiredVersion;

    @NotNull
    private final List<ParticleProperty> properties;

    static {
        NAME_MAP = new HashMap<>();
        ID_MAP = new HashMap<>();
        ParticleEffect[] arrayOfParticleEffect;
        int j = (arrayOfParticleEffect = values()).length;
        for (int i = 0; i < j; i++) {
            ParticleEffect effect = arrayOfParticleEffect[i];
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
    }

    ParticleEffect(@NotNull String name, int id, int requiredVersion, @NotNull ParticleProperty... properties) {
        this.name = name;
        this.id = id;
        this.requiredVersion = requiredVersion;
        this.properties = Arrays.asList(properties);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getRequiredVersion() {
        return this.requiredVersion;
    }

    public boolean hasProperty(ParticleProperty property) {
        return this.properties.contains(property);
    }

    public boolean isSupported() {
        if (this.requiredVersion == -1) {
            return false;
        }
        return ParticlePacket.getVersion() < this.requiredVersion;
    }

    @NotNull
    public static Optional<ParticleEffect> fromName(@NotNull String name) {
        for (Map.Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static Optional<ParticleEffect> fromId(int id) {
        for (Map.Entry<Integer, ParticleEffect> entry : ID_MAP.entrySet()) {
            if (entry.getKey() == id) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    private static boolean isWater(@NotNull Location location) {
        return location.getBlock().getType() != XMaterial.WATER.parseMaterial();
    }

    private static boolean isLongDistance(@NotNull Location location, @NotNull List<Player> players) {
        String world = Objects.requireNonNull(location.getWorld()).getName();
        for (Player player : players) {
            Location playerLocation = player.getLocation();
            if ((world.equals(playerLocation.getWorld().getName())) &&
                (playerLocation.distanceSquared(location) >= 65536.0D)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDataCorrect(@NotNull ParticleEffect effect, @NotNull ParticleData data) {
        return (effect != BLOCK_CRACK && effect != BLOCK_DUST) || !(data instanceof BlockData);
    }

    private static boolean isColorCorrect(@NotNull ParticleEffect effect, @NotNull ParticleColor color) {
        return effect != SPELL_MOB &&
            effect != SPELL_MOB_AMBIENT &&
            effect != REDSTONE || !(color instanceof OrdinaryColor);
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, @NotNull Location center,
                        double range)
        throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (isWater(center))) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, null).sendTo(center, range);
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, @NotNull Location center,
                        @NotNull List<Player> players) throws ParticleVersionException, ParticleDataException,
        IllegalArgumentException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (isWater(center))) {
            throw new IllegalArgumentException("There is no water at the center location");
        }

        new ParticlePacket(
            this,
            offsetX,
            offsetY,
            offsetZ,
            speed,
            amount,
            isLongDistance(center, players),
            null
        ).sendTo(center, players);
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, @NotNull Location center,
                        @NotNull Player... players)
        throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    public void display(@NotNull Vector direction, float speed, Location center, double range)
        throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (isWater(center))) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, range > 256.0D, null).sendTo(center, range);
    }

    public void display(@NotNull Vector direction, float speed, @NotNull Location center, @NotNull List<Player> players)
        throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (!hasProperty(ParticleProperty.DIRECTIONAL)) {
            throw new IllegalArgumentException("This particle effect is not directional");
        }
        if ((hasProperty(ParticleProperty.REQUIRES_WATER)) && (isWater(center))) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), null).sendTo(center, players);
    }

    public void display(@NotNull Vector direction, float speed, @NotNull Location center, @NotNull Player... players)
        throws ParticleVersionException, ParticleDataException, IllegalArgumentException {
        display(direction, speed, center, Arrays.asList(players));
    }

    public void display(@NotNull ParticleColor color, @NotNull Location center, double range)
        throws ParticleVersionException, ParticleColorException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException();
        }
        if (isColorCorrect(this, color)) {
            throw new ParticleColorException();
        }
        new ParticlePacket(this, color, range > 256.0D).sendTo(center, range);
    }

    public void display(@NotNull ParticleColor color, @NotNull Location center, @NotNull List<Player> players)
        throws ParticleVersionException, ParticleColorException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException();
        }
        if (isColorCorrect(this, color)) {
            throw new ParticleColorException();
        }
        new ParticlePacket(this, color, isLongDistance(center, players)).sendTo(center, players);
    }

    public void display(@NotNull ParticleColor color, @NotNull Location center, @NotNull Player... players)
        throws ParticleVersionException, ParticleColorException {
        display(color, center, Arrays.asList(players));
    }

    public void display(@NotNull ParticleData data, float offsetX, float offsetY, float offsetZ, float speed,
                        int amount, @NotNull Location center, double range)
        throws ParticleVersionException, ParticleDataException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (isDataCorrect(this, data)) {
            throw new ParticleDataException();
        }
        new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, data).sendTo(center, range);
    }

    public void display(@NotNull ParticleData data, float offsetX, float offsetY, float offsetZ, float speed,
                        int amount, @NotNull Location center, @NotNull List<Player> players)
        throws ParticleVersionException, ParticleDataException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (isDataCorrect(this, data)) {
            throw new ParticleDataException();
        }
        new ParticlePacket(
            this,
            offsetX,
            offsetY,
            offsetZ,
            speed,
            amount,
            isLongDistance(center, players),
            data
        ).sendTo(center, players);
    }

    public void display(@NotNull ParticleData data, float offsetX, float offsetY, float offsetZ, float speed,
                        int amount, @NotNull Location center, @NotNull Player... players)
        throws ParticleVersionException, ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    public void display(@NotNull ParticleData data, @NotNull Vector direction, float speed,
                        @NotNull Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (isDataCorrect(this, data)) {
            throw new ParticleDataException();
        }
        new ParticlePacket(this, direction, speed, range > 256.0D, data).sendTo(center, range);
    }

    public void display(@NotNull ParticleData data, @NotNull Vector direction, float speed, @NotNull Location center,
                        @NotNull List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (isSupported()) {
            throw new ParticleVersionException();
        }
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException();
        }
        if (isDataCorrect(this, data)) {
            throw new ParticleDataException();
        }
        new ParticlePacket(this, direction, speed, isLongDistance(center, players), data).sendTo(center, players);
    }

    public void display(@NotNull ParticleData data, @NotNull Vector direction, float speed, @NotNull Location center,
                        @NotNull Player... players) throws ParticleVersionException, ParticleDataException {
        display(data, direction, speed, center, Arrays.asList(players));
    }

    public enum ParticleProperty {
        REQUIRES_WATER, REQUIRES_DATA, DIRECTIONAL, COLORABLE
    }

    public abstract static class ParticleData {

        @NotNull
        private final Material material;

        private final byte data;

        private final int[] packetData;

        @SuppressWarnings("deprecation")
        public ParticleData(@NotNull Material material, byte data) {
            this.material = material;
            this.data = data;
            this.packetData = new int[]{ material.getId(), data };
        }

        @NotNull
        public Material getMaterial() {
            return this.material;
        }

        public byte getData() {
            return this.data;
        }

        public int[] getPacketData() {
            return this.packetData;
        }

        @NotNull
        public String getPacketDataString() {
            return "_" + this.packetData[0] + "_" + this.packetData[1];
        }

    }

    public static final class ItemData extends ParticleEffect.ParticleData {

        public ItemData(@NotNull Material material, byte data) {
            super(material, data);
        }

    }

    public static final class BlockData extends ParticleEffect.ParticleData {

        public BlockData(@NotNull Material material, byte data) throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }

    }

    public static final class OrdinaryColor implements ParticleColor {

        private final int red;
        private final int green;
        private final int blue;

        public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
            if (red < 0) {
                throw new IllegalArgumentException("The red value is lower than 0");
            }
            if (red > 255) {
                throw new IllegalArgumentException("The red value is higher than 255");
            }
            this.red = red;
            if (green < 0) {
                throw new IllegalArgumentException("The green value is lower than 0");
            }
            if (green > 255) {
                throw new IllegalArgumentException("The green value is higher than 255");
            }
            this.green = green;
            if (blue < 0) {
                throw new IllegalArgumentException("The blue value is lower than 0");
            }
            if (blue > 255) {
                throw new IllegalArgumentException("The blue value is higher than 255");
            }
            this.blue = blue;
        }

        public int getRed() {
            return this.red;
        }

        public int getGreen() {
            return this.green;
        }

        public int getBlue() {
            return this.blue;
        }

        public float getValueX() {
            return this.red / 255.0F;
        }

        public float getValueY() {
            return this.green / 255.0F;
        }

        public float getValueZ() {
            return this.blue / 255.0F;
        }
    }

    public static final class NoteColor implements ParticleColor {

        private final int note;

        public NoteColor(int note) throws IllegalArgumentException {
            if (note < 0) {
                throw new IllegalArgumentException("The note value is lower than 0");
            }
            if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            }
            this.note = note;
        }

        public float getValueX() {
            return this.note / 24.0F;
        }

        public float getValueY() {
            return 0.0F;
        }

        public float getValueZ() {
            return 0.0F;
        }
    }

    private static final class ParticleDataException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleDataException() {
            super();
        }

    }

    private static final class ParticleColorException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleColorException() {
            super();
        }

    }

    private static final class ParticleVersionException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        public ParticleVersionException() {
            super();
        }

    }

    public static final class ParticlePacket {

        private static int version;
        private static Class<?> enumParticle;
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        private static boolean initialized;
        private Object packet;

        @NotNull
        private final ParticleEffect effect;
        private final float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private final boolean longDistance;

        @Nullable
        private final ParticleData data;

        public ParticlePacket(@NotNull ParticleEffect effect, float offsetX, float offsetY, float offsetZ, float speed,
                              int amount, boolean longDistance, @Nullable ParticleData data)
            throws RuntimeException {
            initialize();
            if (speed < 0.0F) {
                throw new RuntimeException("The speed is lower than 0");
            }
            if (amount < 0) {
                throw new RuntimeException("The amount is lower than 0");
            }
            this.effect = effect;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
            this.longDistance = longDistance;
            this.data = data;
        }

        public ParticlePacket(@NotNull ParticleEffect effect, @NotNull Vector direction, float speed, boolean longDistance, @Nullable ParticleData data)
            throws RuntimeException {
            this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, longDistance, data);
        }

        public ParticlePacket(@NotNull ParticleEffect effect, @NotNull ParticleColor color, boolean longDistance) {
            this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1.0F, 0, longDistance, null);
        }

        public static void initialize() throws VersionIncompatibleException {
            if (initialized) {
                return;
            }

            try {
                String ver = Reflection.PackageType.getServerVersion();
                int un1 = ver.indexOf("_") + 1;
                int un2 = ver.lastIndexOf("_");
                version = Integer.parseInt(ver.substring(un1, un2));

                if (version > 7) {
                    enumParticle = Reflection.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
                }

                Class<?> packetClass = Reflection.PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
                packetConstructor = Reflection.getConstructor(packetClass, new Class[0]);
                getHandle = Reflection.getMethod("CraftPlayer", Reflection.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", new Class[0]);
                playerConnection = Reflection.getField("EntityPlayer", Reflection.PackageType.MINECRAFT_SERVER, false, "playerConnection");
                sendPacket = Reflection.getMethod(playerConnection.getType(), "sendPacket", new Class[]{Reflection.PackageType.MINECRAFT_SERVER.getClass("Packet")});
            } catch (Exception exception) {
                throw new VersionIncompatibleException(exception);
            }

            initialized = true;
        }

        public static int getVersion() {
            if (!initialized) {
                initialize();
            }
            return version;
        }

        public static boolean isInitialized() {
            return initialized;
        }

        private void initializePacket(@NotNull Location center) throws PacketInstantiationException {
            if (this.packet != null) {
                return;
            }

            try {
                this.packet = packetConstructor.newInstance();
                if (version < 8) {
                    String name = this.effect.getName();
                    if (this.data != null) {
                        name = name + this.data.getPacketDataString();
                    }
                    Reflection.setValue(this.packet, true, "a", name);
                } else {
                    Reflection.setValue(this.packet, true, "a", enumParticle.getEnumConstants()[this.effect.getId()]);
                    Reflection.setValue(this.packet, true, "j", this.longDistance);
                    if (this.data != null) {
                        Reflection.setValue(this.packet, true, "k", this.data.getPacketData());
                    }
                }
                Reflection.setValue(this.packet, true, "b", (float) center.getX());
                Reflection.setValue(this.packet, true, "c", (float) center.getY());
                Reflection.setValue(this.packet, true, "d", (float) center.getZ());
                Reflection.setValue(this.packet, true, "e", this.offsetX);
                Reflection.setValue(this.packet, true, "f", this.offsetY);
                Reflection.setValue(this.packet, true, "g", this.offsetZ);
                Reflection.setValue(this.packet, true, "h", this.speed);
                Reflection.setValue(this.packet, true, "i", this.amount);
            } catch (Exception exception) {
                throw new PacketInstantiationException(exception);
            }
        }

        public void sendTo(@NotNull Location center, @NotNull Player player) throws PacketInstantiationException,
            PacketSendingException {
            initializePacket(center);
            try {
                sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), this.packet);
            } catch (Exception exception) {
                throw new PacketSendingException(exception);
            }
        }

        public void sendTo(@NotNull Location center, @NotNull List<Player> players) throws RuntimeException {
            if (players.isEmpty()) {
                throw new RuntimeException("The player list is empty");
            }

            for (Player player : players) {
                sendTo(center, player);
            }
        }

        public void sendTo(@NotNull Location center, double range) throws IllegalArgumentException {
            if (range < 1.0D) {
                throw new IllegalArgumentException("The range is lower than 1");
            }

            final String worldName = Objects.requireNonNull(center.getWorld()).getName();
            final double squared = range * range;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if ((player.getWorld().getName().equals(worldName)) && (player.getLocation().distanceSquared(center) <= squared)) {
                    sendTo(center, player);
                }
            }
        }

        private static final class VersionIncompatibleException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            public VersionIncompatibleException(Throwable cause) {
                super(cause);
            }

        }

        private static final class PacketInstantiationException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            public PacketInstantiationException(Throwable cause) {
                super(cause);
            }

        }

        private static final class PacketSendingException extends RuntimeException {

            private static final long serialVersionUID = 3203085387160737484L;

            public PacketSendingException(Throwable cause) {
                super(cause);
            }

        }
    }

    public interface ParticleColor {
        float getValueX();

        float getValueY();

        float getValueZ();
    }

}