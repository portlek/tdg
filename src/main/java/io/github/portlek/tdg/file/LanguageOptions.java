package io.github.portlek.tdg.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LanguageOptions implements Scalar<Language> {

    @NotNull
    private final String prefix;

    @NotNull
    private final IYaml yaml;

    public LanguageOptions(@NotNull String prefix, @NotNull IYaml yaml) {
        this.prefix = prefix;
        this.yaml = yaml;
    }

    @Override
    public Language value() {
        yaml.create();

        // error
        final String cooldown = c(yaml.getString("error.cooldown").orElse(""));
        final String alreadyOpen = c(yaml.getString("error.already-open").orElse(""));
        final String permission = c(yaml.getString("error.permission").orElse(""));
        final String menuNotFound = c(yaml.getString("error.menu-not-found").orElse(""));
        final String invalidArgument = c(yaml.getString("error.invalid-argument").orElse(""));
        final String inGameCommand = c(yaml.getString("error.in-game-command").orElse(""));

        // general
        final String availableMenus = c(yaml.getString("general.available-menus").orElse(""));
        final String reloadComplete = c(yaml.getString("general.reload-complete").orElse(""));
        final String pluginVersion = c(yaml.getString("general.plugin-version").orElse(""))
            .replaceAll("%version%", TDG.getAPI().tdg.getDescription().getVersion());
        final String newVersionFound = c(yaml.getString("general.new-version-found").orElse(""));
        final String latestVersion = c(yaml.getString("general.latest-version").orElse(""));

        // commands
        final String commands = cL(yaml.getStringList("commands"));

        return new Language(
            cooldown,
            alreadyOpen,
            permission,
            menuNotFound,
            invalidArgument,
            inGameCommand,
            availableMenus,
            reloadComplete,
            pluginVersion,
            newVersionFound,
            latestVersion,
            commands
        );
    }

    @NotNull
    private String c(@NotNull String text) {
        return new Colored(
            text.replaceAll("%prefix%", prefix)
        ).value();
    }

    @NotNull
    private String cL(@NotNull List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            final String text = list.get(i);

            sb.append(text);

            if (i < list.size() - 1) {
                sb.append("\n");
            }
        }

        return c(sb.toString());
    }

}
