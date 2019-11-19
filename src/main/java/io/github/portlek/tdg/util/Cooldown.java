package io.github.portlek.tdg.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
 
public class Cooldown {
 
    private static Map<String, Cooldown> COOLDOWNS = new HashMap<>();

    @NotNull
    private final UUID uuid;

    @NotNull
    private final String cooldownName;

    private final int timeInSeconds;

    private long start;

    public Cooldown(@NotNull UUID uuid, @NotNull String cooldownName, int timeInSeconds) {
        this.uuid = uuid;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public boolean isInCooldown(@NotNull UUID id, @NotNull String cooldownName) {
        if(getTimeLeft(id, cooldownName) >= 1) {
            return true;
        }

        stop(id, cooldownName);

        return false;
    }
 
    private void stop(@NotNull UUID id, @NotNull String cooldownName) {
        COOLDOWNS.remove(id + cooldownName);
    }
 
    private Cooldown getCooldown(@NotNull UUID id, @NotNull String cooldownName) {
        return COOLDOWNS.get(id.toString() + cooldownName);
    }
 
    public int getTimeLeft(@NotNull UUID id, @NotNull String cooldownName) {
        final Cooldown cooldown = getCooldown(id, cooldownName);
        int f = -1;

        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - cooldown.timeInSeconds) * (-1);
        }

        return f;
    }
 
    public void start() {
        start = System.currentTimeMillis();
        COOLDOWNS.put(uuid.toString() + cooldownName, this);
    }
 
}