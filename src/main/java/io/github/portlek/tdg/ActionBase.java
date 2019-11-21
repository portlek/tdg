package io.github.portlek.tdg;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.events.abs.MenuEvent;
import io.github.portlek.tdg.particle.Particles;
import io.github.portlek.tdg.types.ActionType;
import io.github.portlek.tdg.util.Utils;
import io.github.portlek.tdg.util.XSound;
import org.bukkit.Bukkit;
import org.cactoos.iterable.Filtered;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class ActionBase<T extends MenuEvent> {

    @NotNull
    private final Consumer<T> consumer;

    public ActionBase(@NotNull Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public void apply(@NotNull T event) {
        consumer.accept(event);
    }

    @NotNull
    public static <X extends MenuEvent> List<ActionBase<X>> parse(@NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId) {
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
                            return new ActionBase<>(event -> new Joined<>(
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
                            return new ActionBase<>(event -> new Mapped<>(
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
                            return new ActionBase<>(event -> XSound
                                .matchXSound(
                                    yaml.getString(path + "value").orElse("BLOCK_GLASS_BREAK"),
                                    XSound.BLOCK_GLASS_BREAK
                                ).playSound(
                                    event.getPlayer(),
                                    (float) yaml.getDouble(path + "volume"),
                                    (float) yaml.getDouble(path + "pitch")
                                ));
                        case OPEN_MENU:
                            return new ActionBase<>(event -> yaml.getString(path + "value")
                                .ifPresent(s ->
                                    TDG.getAPI().findMenuById(s).open(event.getPlayer())
                                )
                            );
                        case PARTICLES:
                            return new ActionBase<>(event -> new Particles(
                                Utils.getBFLoc(event.getPlayer().getLocation(), 3.5).add(0, 2.5, 0),
                                yaml.getString(path + "value").orElse("clound"),
                                yaml.getInt(path + "amount"),
                                yaml.getInt(path + "speed")
                            ).spawnParticles());
                        case CLOSE_MENU:
                            return new ActionBase<>(event -> event.getOpenedMenu().close());
                        case NONE:
                        default:
                            return new ActionBase<>(event -> {});
                    }
                },
                yaml.getSection("menus." + menuId + ".icons." + iconId + ".click-actions").getKeys(false)
            )
        );
    }

}
