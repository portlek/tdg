package io.github.portlek.tdg.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.type.ActionType;
import io.github.portlek.tdg.api.type.ClickType;
import io.github.portlek.tdg.api.type.RequirementType;
import io.github.portlek.tdg.api.type.TargetType;
import io.github.portlek.tdg.nms.v1_9_R1.Particles1_9;
import io.github.portlek.tdg.oldparticle.ParticleEffect;
import io.github.portlek.tdg.target.BasicTarget;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TargetParsed<T extends MenuEvent> {

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final String menuId;

    @NotNull
    private final String iconId;

    @NotNull
    private final TargetType targetType;

    public TargetParsed(@NotNull Class<T> tClass, @NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId) {
        this.yaml = yaml;
        this.menuId = menuId;
        this.iconId = iconId;
        this.targetType = fromClass(tClass);
    }

    public TargetParsed(@NotNull Class<T> tClass, @NotNull IYaml yaml, @NotNull String menuId) {
        this(tClass, yaml, menuId, "");
    }

    @NotNull
    public List<Target<T>> parse() {
        final String finalPath;

        if (iconId.isEmpty()) {
            finalPath = "menus." + menuId + "." + targetType.getType();
        } else {
            finalPath = "menus." + menuId + ".icons." + iconId + "." + targetType.getType();
        }

        return new ListOf<>(
            new Mapped<>(
                key -> new BasicTarget<>(
                    parseConsumer(finalPath + "." + key + "."),
                    parseRequirements(finalPath + "." + key + ".")
                ),
                yaml.getSection(finalPath).getKeys(false)
            )
        );
    }

    @NotNull
    private List<Requirement> parseRequirements(@NotNull String path) {
        return new ListOf<>(
            new Mapped<>(
                requirement -> {
                    final RequirementType requirementType = RequirementType.fromString(requirement);
                    final String reqPath = path + "requirements." + requirement;

                    switch (requirementType) {
                        case CLICK_TYPE:
                            return event -> {
                                final List<ClickType> clickTypes = new Mapped<>(
                                    ClickType::fromString,
                                    yaml.getStringList(reqPath)
                                );

                                if (!clickTypes.isEmpty()) {
                                    return event instanceof IconClickEvent &&
                                        (clickTypes.contains(ClickType.ANY) ||
                                            clickTypes.contains(((IconClickEvent) event).getClickType()));
                                }

                                final ClickType clickType = ClickType.fromString(
                                    yaml.getString(reqPath).orElse("")
                                );

                                return event instanceof IconClickEvent &&
                                    (clickType == ClickType.ANY ||
                                        clickType == (((IconClickEvent) event).getClickType()));
                            };
                        case PERMISSIONS:
                            return event -> {
                                final List<String> permissions = new Mapped<>(
                                    perm -> {
                                        if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                                            return PlaceholderAPI.setPlaceholders(event.getPlayer(), perm);
                                        }

                                        return perm.replaceAll("%player_name%", event.getPlayer().getName());
                                    },
                                    yaml.getStringList(reqPath)
                                );

                                if (!permissions.isEmpty()) {
                                    return permissions.stream().allMatch(s -> event.getPlayer().hasPermission(s));
                                }

                                final String permission = yaml.getString(reqPath).orElse("");

                                if (!permission.isEmpty()) {
                                    return event.getPlayer().hasPermission(
                                        permission.replaceAll("%player_name%", event.getPlayer().getName())
                                    );
                                }

                                return true;
                            };
                        // TODO: 24/11/2019 More requirement support
                        case NONE:
                        default:
                            return event -> true;
                    }
                },
                yaml.getSection(path + "requirements").getKeys(false)
            )
        );
    }

    @NotNull
    private Consumer<T> parseConsumer(@NotNull String path) {
        final ActionType actionType = ActionType.fromString(
            yaml.getString(path + "action")
                .orElse("NONE")
        );

        switch (actionType) {
            case MESSAGE:
                return event -> {
                    final List<String> value = new Mapped<>(
                        list -> {
                            final String colored = new Colored(
                                list
                            ).value();

                            if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                                return PlaceholderAPI.setPlaceholders(event.getPlayer(), colored);
                            }

                            return colored.replaceAll("%player_name%", event.getPlayer().getName());
                        },
                        yaml.getStringList(path + "value")
                    );

                    if (!value.isEmpty()) {
                        value.forEach(s -> event.getPlayer().sendMessage(s));
                        return;
                    }

                    final String message = yaml.getString(path + "value").orElse("");

                    if (!message.isEmpty()) {
                        if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                            event.getPlayer().sendMessage(
                                new Colored(
                                    PlaceholderAPI.setPlaceholders(event.getPlayer(), message)
                                ).value()
                            );

                            return;
                        }

                        event.getPlayer().sendMessage(
                            new Colored(
                                message.replaceAll("%player_name%", event.getPlayer().getName())
                            ).value()
                        );
                    }
                };
            case COMMAND:
                return event -> {
                    final List<String> commands = new ArrayList<>();
                    final List<String> value = new Mapped<>(
                        list -> {
                            if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                                return PlaceholderAPI.setPlaceholders(event.getPlayer(), list);
                            }

                            return list.replaceAll("%player_name%", event.getPlayer().getName());
                        },
                        yaml.getStringList(path + "value")
                    );

                    if (!value.isEmpty()) {
                        commands.addAll(value);
                    }

                    final String command = yaml.getString(path + "value").orElse("");

                    if (!command.isEmpty()) {
                        if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                            commands.add(
                                PlaceholderAPI.setPlaceholders(event.getPlayer(), command)
                            );
                        } else {
                            commands.add(command.replaceAll("%player_name%", event.getPlayer().getName()));
                        }
                    }

                    commands.forEach(s -> Bukkit.getScheduler().callSyncMethod(
                        TDG.getAPI().tdg,
                        () -> {
                            if (yaml.getBoolean(path + "as-player")) {
                                return event.getPlayer().performCommand(s);
                            }

                            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                        }
                    ));
                };
            case SOUND:
                return event -> XSound
                    .matchXSound(
                        yaml.getString(path + "value").orElse("BLOCK_GLASS_BREAK"),
                        XSound.BLOCK_GLASS_BREAK
                    ).playSound(
                        event.getPlayer(),
                        (float) yaml.getDouble(path + "volume"),
                        (float) yaml.getDouble(path + "pitch")
                    );
            case OPEN_MENU:
                return event -> yaml.getString(path + "value")
                    .ifPresent(s ->
                        TDG.getAPI().findMenuById(s).open(event.getPlayer(), true)
                    );
            case PARTICLES:
                return event -> {
                    final String value = yaml.getString(path + "value").orElse("smoke");
                    final int speed = yaml.getInt(path + "speed");
                    final int amount = yaml.getInt(path + "amount");
                    final Location location = Utils.getBFLoc(event.getPlayer().getLocation(), 3.5).add(0, 2.5, 0);

                    if (TDG.PARTICLES) {
                        final Particle particle;

                        try {
                            particle = Particle.valueOf(value);
                        } catch (Exception exception) {
                            TDG.getAPI().tdg.getLogger().severe("Unknown particle name: " + value);
                            return;
                        }

                        new Particles1_9(location, particle, amount, speed).spawn();

                        return;
                    }

                    final ParticleEffect particleEffect = ParticleEffect.fromName(value);

                    if (particleEffect == null) {
                        TDG.getAPI().tdg.getLogger().severe("Unknown particle name: " + value);
                        return;
                    }

                    particleEffect.display(0.0f, 0.0f, 0.0f, speed, amount, location, 1000.0);
                };
            case CLOSE_MENU:
                return event -> event.getOpenedMenu().close();
            case NONE:
            default:
                return event -> {};
        }
    }

    @NotNull
    private TargetType fromClass(@NotNull Class<T> tClass) {
        if (MenuOpenEvent.class.isAssignableFrom(tClass)) {
            return TargetType.OPEN;
        } else if (MenuCloseEvent.class.isAssignableFrom(tClass)) {
            return TargetType.CLOSE;
        } else if (IconClickEvent.class.isAssignableFrom(tClass)) {
            return TargetType.CLICK;
        } else if (IconHoverEvent.class.isAssignableFrom(tClass)) {
            return TargetType.HOVER;
        }

        throw new UnsupportedOperationException();
    }

}
