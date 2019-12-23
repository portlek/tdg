package io.github.portlek.tdg.file;

import io.github.portlek.mcyaml.IYaml;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SkyBlockFixOptions implements Scalar<SkyBlockFix> {

    @NotNull
    public final IYaml yaml;

    public SkyBlockFixOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public SkyBlockFix value() {
        final Map<UUID, Integer> skyBlockFix = new HashMap<>();

        for (String id : yaml.getOrCreateSection("players").getKeys(false)) {
            skyBlockFix.put(
                UUID.fromString(id),
                yaml.getOrSet("players." + id, 0)
            );
        }

        return new SkyBlockFix(
            skyBlockFix
        );
    }

}
