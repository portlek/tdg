package io.github.portlek.tdg.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
 
public final class Cooldown {
 
    private static final Map<String, Cooldown> COOLDOWNS = new HashMap<>();

    private final int timeInSeconds;

    @NotNull
    private final UUID id;

    @NotNull
    private final String cooldownName;

    private long start;
 
    public Cooldown(@NotNull UUID uuid, @NotNull String cooldownName, int timeInSeconds) {
        this.id = uuid;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }
 
    public static boolean isInCooldown(@NotNull UUID id, @NotNull String cooldownName) {
        if(getTimeLeft(id, cooldownName)>=1) {
            return true;
        }

        stop(id, cooldownName);

        return false;
    }
 
    private static void stop(@NotNull UUID id, @NotNull String cooldownName) {
        COOLDOWNS.remove(id + cooldownName);
    }
 
    private static Cooldown getCooldown(@NotNull UUID id, @NotNull String cooldownName) {
        return COOLDOWNS.get(id.toString() + cooldownName);
    }
 
    public static int getTimeLeft(@NotNull UUID id, @NotNull String cooldownName) {
        final Cooldown cooldown = getCooldown(id, cooldownName);

        if (cooldown == null) {
            return -1;
        }

        long now = System.currentTimeMillis();
        long cooldownTime = cooldown.start;
        int r = (int) (now - cooldownTime) / 1000;
        return (r - cooldown.timeInSeconds) * (-1);
    }
 
    public void start(){
        start = System.currentTimeMillis();
        COOLDOWNS.put(id.toString() + cooldownName, this);
    }
 
}