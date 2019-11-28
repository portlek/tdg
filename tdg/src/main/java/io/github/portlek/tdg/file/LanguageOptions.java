package io.github.portlek.tdg.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
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
        final String cooldown = c(yaml.getOrSet("error.cooldown", "error.cooldown"));
        final String alreadyOpen = c(yaml.getOrSet("error.already-open", "error.already-open"));
        final String permission = c(yaml.getOrSet("error.permission", "error.permission"));
        final String menuNotFound = c(yaml.getOrSet("error.menu-not-found", "error.menu-not-found"));
        final String invalidArgument = c(yaml.getOrSet("error.invalid-argument", "error.invalid-argument"));
        final String inGameCommand = c(yaml.getOrSet("error.in-game-command", "error.in-game-command"));
        final String playerNotFound = c(yaml.getOrSet("error.player-not-found", "error.player-not-found"));

        // general
        final String availableMenus = c(yaml.getOrSet("general.available-menus", "general.available-menus"));
        final String reloadComplete = c(yaml.getOrSet("general.reload-complete","general.reload-complete"));
        final String pluginVersion = c(yaml.getOrSet("general.plugin-version", "general.plugin-version"))
            .replaceAll("%version%", TDG.getAPI().tdg.getDescription().getVersion());
        final String newVersionFound = c(yaml.getOrSet("general.new-version-found", "general.new-version-found"));
        final String latestVersion = c(yaml.getOrSet("general.latest-version", "general.latest-version"));

        // commands
        final String commands = cL(yaml.getOrSet("commands", new ListOf<>()));

        return new Language(
            cooldown,
            alreadyOpen,
            permission,
            menuNotFound,
            invalidArgument,
            inGameCommand,
            playerNotFound,
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
