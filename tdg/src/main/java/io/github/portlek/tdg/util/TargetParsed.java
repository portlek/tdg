package io.github.portlek.tdg.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.mck.MckFileConfiguration;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.type.ActionType;
import io.github.portlek.tdg.api.type.ClickType;
import io.github.portlek.tdg.api.type.RequirementType;
import io.github.portlek.tdg.api.type.TargetType;
import io.github.portlek.tdg.nms.v1_9_R1.Particles1_9;
import io.github.portlek.tdg.oldparticle.ParticleEffect;
import io.github.portlek.tdg.requirement.ClickTypeReq;
import io.github.portlek.tdg.requirement.CooldownReq;
import io.github.portlek.tdg.requirement.MoneyReq;
import io.github.portlek.tdg.requirement.PermissionReq;
import io.github.portlek.tdg.target.BasicTarget;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.map.MapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    public Map.Entry<List<Requirement>, List<Target<T>>> parse() {
        final String finalPath;

        if (iconId.isEmpty()) {
            finalPath = "menus." + menuId + "." + targetType.getType();
        } else {
            finalPath = "menus." + menuId + ".icons." + iconId + "." + targetType.getType();
        }

        final List<Requirement> requirements = new ArrayList<>();
        final List<Target<T>> targets = new ArrayList<>();
        final ConfigurationSection section = yaml.getSection(finalPath);

        if (section instanceof MckFileConfiguration) {
            return new MapEntry<>(
                requirements,
                targets
            );
        }

        for (String key : section.getKeys(false)) {
            if (key.equalsIgnoreCase("requirements")) {
                requirements.addAll(
                    parseRequirements(finalPath + ".requirements")
                );
                continue;
            }

            targets.add(
                new BasicTarget<>(
                    parseConsumer(finalPath + "." + key + "."),
                    parseRequirements(finalPath + "." + key + ".requirements")
                )
            );
        }

        return new MapEntry<>(
            requirements,
            targets
        );
    }

    @NotNull
    private List<Requirement> parseRequirements(@NotNull String path) {
        final List<Requirement> requirements = new ArrayList<>();
        final ConfigurationSection section = yaml.getSection(path);

        if (section instanceof MckFileConfiguration) {
            return requirements;
        }

        for (String requirement : section.getKeys(false)) {
            final RequirementType requirementType = RequirementType.fromString(requirement);
            final String reqPath = path + "." + requirement;

            switch (requirementType) {
                case CLICK_TYPE:
                    if (!yaml.getStringList(reqPath).isEmpty()) {
                        requirements.add(
                            new ClickTypeReq(
                                new Mapped<>(
                                    ClickType::fromString,
                                    yaml.getStringList(reqPath)
                                )
                            )
                        );
                    } else {
                        requirements.add(
                            new ClickTypeReq(
                                new ListOf<>(
                                    ClickType.fromString(
                                        yaml.getString(reqPath).orElse("")
                                    )
                                )
                            )
                        );
                    }
                    break;
                case PERMISSIONS:
                    if (!yaml.getStringList(reqPath).isEmpty()) {
                        requirements.add(
                            new PermissionReq(
                                yaml.getStringList(reqPath)
                            )
                        );
                    } else {
                        requirements.add(
                            new PermissionReq(
                                new ListOf<>(
                                    yaml.getString(reqPath).orElse("")
                                )
                            )
                        );
                    }
                    break;
                case COOLDOWN:
                    requirements.add(
                        new CooldownReq(
                            yaml.getInt(reqPath)
                        )
                    );
                    break;
                case MONEY:
                    requirements.add(
                        new MoneyReq(
                            yaml.getInt(reqPath)
                        )
                    );
                    break;
                    // TODO: 24/11/2019 More requirement support
                case NONE:
                default:
                    break;
            }
        }

        return requirements;
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
                    final Player player = event.getPlayer();
                    final List<String> value = new Mapped<>(
                        list -> {
                            final String colored = new Colored(
                                list
                            ).value();

                            if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                                return PlaceholderAPI.setPlaceholders(player, colored);
                            }

                            return colored.replaceAll("%player_name%", player.getName());
                        },
                        yaml.getStringList(path + "value")
                    );

                    if (!value.isEmpty()) {
                        value.forEach(player::sendMessage);
                        return;
                    }

                    final String message = yaml.getString(path + "value").orElse("");

                    if (!message.isEmpty()) {
                        if (TDG.getAPI().getConfig().hooksPlaceholderAPI) {
                            player.sendMessage(
                                new Colored(
                                    PlaceholderAPI.setPlaceholders(player, message)
                                ).value()
                            );

                            return;
                        }

                        player.sendMessage(
                            new Colored(
                                message.replaceAll("%player_name%", player.getName())
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
                    } else {
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
                    }

                    if (!TDG.getAPI().tdg.isEnabled()) {
                        TDG.getAPI().tdg.getLogger().severe("You may use command action on the menu close-actions.");
                        TDG.getAPI().tdg.getLogger().severe("Commands cannot use after plugin disable!");
                        return;
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
                return event -> {
                    final float volume = (float) yaml.getDouble(path + "volume");
                    XSound
                        .matchXSound(
                            yaml.getString(path + "value").orElse("BLOCK_GLASS_BREAK"),
                            XSound.BLOCK_GLASS_BREAK
                        ).playSound(
                        event.getPlayer(),
                        volume == 0 ? 1 : volume,
                        (float) yaml.getDouble(path + "pitch")
                    );
                };
            case OPEN_MENU:
                return event -> yaml.getString(path + "value")
                    .ifPresent(s -> {
                        final Menu menu = TDG.getAPI().findMenuById(s);

                        if (menu instanceof MckMenu) {
                            TDG.getAPI().tdg.getLogger().severe(String.format("There is not menu with %s id", s));
                            return;
                        }

                        menu.open(event.getPlayer(), true);
                    });
            case PARTICLES:
                return event -> {
                    final String value = yaml.getString(path + "value").orElse("smoke");
                    final int speed = yaml.getInt(path + "speed");
                    final int amount = yaml.getInt(path + "amount");
                    final Location location = Utils.getBFLoc(event.getPlayer().getLocation(), 3.5).add(0, 2.5, 0);

                    if (TDG.NEW_PARTICLES) {
                        final Particle particle;

                        try {
                            particle = Particle.valueOf(value.toUpperCase(Locale.ENGLISH));
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
                return event -> {
                    event.getOpenedMenu().close();
                    TDG.getAPI().opened.remove(event.getPlayer().getUniqueId());
                };
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
