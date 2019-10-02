package io.github.portlek.tdg.file;

import io.github.portlek.mcyaml.IYaml;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public class ConfigOptions implements Scalar<Config> {

    @NotNull
    private final IYaml yaml;

    public ConfigOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Config value() {
        return new Config(

        );
    }

}
