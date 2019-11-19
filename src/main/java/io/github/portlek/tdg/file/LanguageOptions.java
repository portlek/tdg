package io.github.portlek.tdg.file;

import io.github.portlek.mcyaml.IYaml;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public final class LanguageOptions implements Scalar<Language> {

    @NotNull
    private final Config config;

    @NotNull
    private final IYaml yaml;

    public LanguageOptions(@NotNull Config config, @NotNull IYaml yaml) {
        this.config = config;
        this.yaml = yaml;
    }

    @Override
    public Language value() {
        yaml.create();

        return new Language(

        );
    }

}
