package io.github.portlek.tdg.icon;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.types.ActionType;
import io.github.portlek.tdg.util.XSound;
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
                            return new ClickAction(event -> new Mapped<>(
                                command -> new Colored(
                                    command.replaceAll("%player%", event.getPlayer().getName())
                                ).value(),
                                new Joined<>(
                                    new ListOf<>(
                                        new Filtered<>(
                                            filtered -> !filtered.isEmpty(),
                                            new ListOf<>(yaml.getString(path + "value").orElse(""))
                                        )
                                    ),
                                    yaml.getStringList(path + "value")
                                )
                            ).forEach(s -> Bukkit.getScheduler().callSyncMethod(
                                TDG.getAPI().tdg,
                                () -> {
                                    if (yaml.getBoolean(path + "as-player")) {
                                        return event.getPlayer().performCommand(s);
                                    }

                                    return Bukkit.dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        s
                                    );
                                }
                            )));
                        case SOUND:
                            return new ClickAction(event -> XSound
                                .matchXSound(
                                    yaml.getString(path + "value").orElse("BLOCK_GLASS_BREAK"),
                                    XSound.BLOCK_GLASS_BREAK
                                ).playSound(
                                    event.getPlayer(),
                                    (float) yaml.getDouble(path + "volume"),
                                    (float) yaml.getDouble(path + "pitch")
                                ));
                        case OPEN_MENU:
                            return new ClickAction(event -> {
                                
                            });
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
