package io.github.portlek.tdg.icon;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.types.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cactoos.iterable.Filtered;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ClickAction implements Consumer<IconClickEvent> {

    @NotNull
    private final Consumer<IconClickEvent> consumer;

    public ClickAction(@NotNull Consumer<IconClickEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(IconClickEvent event) {
        consumer.accept(event);
    }

    @NotNull
    public static List<ClickAction> parse(@NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId) {
        return new ListOf<>(
            new Mapped<>(
                action -> {
                    final String path = "menus." + menuId + ".icons." + iconId + ".click-actions." + action + ".";
                    final ActionType actionType = ActionType.fromString(
                        yaml.getString(path + "action")
                            .orElse("NONE")
                    );

                    switch (actionType) {
                        case MESSAGE:
                            return new ClickAction(event -> new Joined<>(
                                new ListOf<>(
                                    new Filtered<>(
                                        filtered -> !filtered.isEmpty(),
                                        new ListOf<>(yaml.getString(path + "value").orElse(""))
                                    )
                                ),
                                yaml.getStringList(path + "value")
                            ).forEach(s -> event.getPlayer().sendMessage(
                                new Colored(s).value()
                            )));
                        case COMMAND:
                            return new ClickAction(event -> {
                                final List<String> commands = new Joined<>(
                                    new ListOf<>(
                                        new Filtered<>(
                                            filtered -> !filtered.isEmpty(),
                                            new ListOf<>(yaml.getString(path + "value").orElse(""))
                                        )
                                    ),
                                    yaml.getStringList(path + "value")
                                );

                                final boolean asPlayer = yaml.getBoolean(path + "as-player");
                                final Player player = event.getPlayer();
                                final Stream<String> cmd = commands
                                    .stream()
                                    .flatMap(s -> Stream.of(
                                        new Colored(
                                            s.replaceAll("%player%", player.getName())
                                        ).value())
                                    );
                                
                                if (asPlayer) {
                                    cmd.forEach(command -> Bukkit.getScheduler().callSyncMethod(
                                            TDG.getAPI().tdg,
                                        () ->
                                            player.performCommand(
                                                command
                                            )
                                        )
                                    );
                                    return;
                                }

                                cmd.forEach(command -> Bukkit.getScheduler().callSyncMethod(
                                        TDG.getAPI().tdg,
                                    () -> Bukkit.getServer().dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        command
                                    )
                                ));
                            });
                        case SOUND:
                            return new ClickAction(event -> {});
                        case OPEN_MENU:
                            return new ClickAction(event -> {});
                        case PARTICLES:
                            return new ClickAction(event -> {});
                        case CLOSE_MENU:
                            return new ClickAction(event -> {});
                        case NONE:
                        default:
                            return new ClickAction(event -> {});
                    }
                },
                yaml.getSection("menus." + menuId + ".icons." + iconId + ".click-actions").getKeys(false)
            )
        );
    }

}
