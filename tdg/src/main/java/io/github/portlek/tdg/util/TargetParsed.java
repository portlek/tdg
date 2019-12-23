package io.github.portlek.tdg.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.mck.MckFileConfiguration;
import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.TDGAPI;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.Target;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.api.hook.GroupWrapped;
import io.github.portlek.tdg.api.hook.IslandWrapped;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.type.ActionType;
import io.github.portlek.tdg.api.type.ClickType;
import io.github.portlek.tdg.api.type.RequirementType;
import io.github.portlek.tdg.api.type.TargetType;
import io.github.portlek.tdg.hooks.ASkyBlockWrapper;
import io.github.portlek.tdg.hooks.BentoBoxWrapper;
import io.github.portlek.tdg.hooks.PlaceholderAPIWrapper;
import io.github.portlek.tdg.hooks.VaultWrapper;
import io.github.portlek.tdg.nms.v1_9_R1.Particles1_9;
import io.github.portlek.tdg.oldparticle.ParticleEffect;
import io.github.portlek.tdg.requirement.*;
import io.github.portlek.tdg.target.BasicTarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.map.MapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class TargetParsed<T extends MenuEvent> {

    @NotNull
    private final Supplier<Optional<IslandWrapped>> islandWrappedSupplier;

    @NotNull
    private final Supplier<Optional<GroupWrapped>> groupWrappedSupplier;

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final String menuId;

    @NotNull
    private final String iconId;

    @NotNull
    private final TargetType targetType;

    @NotNull
    private final TDGAPI api;

    public TargetParsed(@NotNull Class<T> tClass, @NotNull IYaml yaml, @NotNull String menuId, @NotNull String iconId,
                        @NotNull TDGAPI api) {
        this.yaml = yaml;
        this.menuId = menuId;
        this.iconId = iconId;
        this.targetType = fromClass(tClass);
        this.api = api;
        this.islandWrappedSupplier = () -> {
            final AtomicReference<IslandWrapped> wrapped = new AtomicReference<>();

            api.config.getWrapped("ASkyBlock").ifPresent(aSkyBlockWrapper ->
                wrapped.set((ASkyBlockWrapper) aSkyBlockWrapper));

            api.config.getWrapped("BentoBox").ifPresent(bentoBoxWrapper ->
                wrapped.set((BentoBoxWrapper) bentoBoxWrapper));

            return Optional.ofNullable(wrapped.get());
        };
        this.groupWrappedSupplier = () -> {
            final AtomicReference<GroupWrapped> wrapped = new AtomicReference<>();

            api.config.getWrapped("LuckPerms").ifPresent(groupWrapped ->
                wrapped.set((GroupWrapped) groupWrapped));

            api.config.getWrapped("GroupManager").ifPresent(groupWrapped ->
                wrapped.set((GroupWrapped) groupWrapped));

            api.config.getWrapped("PermissionsEx").ifPresent(groupWrapped ->
                wrapped.set((GroupWrapped) groupWrapped));

            return Optional.ofNullable(wrapped.get());
        };
    }

    public TargetParsed(@NotNull Class<T> tClass, @NotNull IYaml yaml, @NotNull String menuId, @NotNull TDGAPI api) {
        this(tClass, yaml, menuId, "", api);
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
                    final Object clickType = yaml.get(reqPath, "");

                    if (clickType instanceof List) {
                        requirements.add(
                            new ClickTypeReq(
                                new Mapped<>(
                                    ClickType::fromString,
                                    yaml.getStringList(reqPath)
                                )
                            )
                        );
                    } else if (clickType instanceof String) {
                        requirements.add(
                            new ClickTypeReq(
                                new ListOf<>(
                                    ClickType.fromString(
                                        yaml.getString(reqPath).orElse("")
                                    )
                                )
                            )
                        );
                    } else if (clickType instanceof ConfigurationSection) {
                        requirements.add(
                            new ClickTypeReq(
                                new Colored(
                                    yaml.getString(reqPath + ".fallback").orElse("")
                                ).value(),
                                new ListOf<>(
                                    ClickType.fromString(
                                        yaml.getString(reqPath + ".value").orElse("")
                                    )
                                )
                            )
                        );
                    }
                    break;
                case PERMISSIONS:
                    final Object permission = yaml.get(reqPath, "");

                    if (permission instanceof List) {
                        requirements.add(
                            new PermissionReq(
                                yaml.getStringList(reqPath)
                            )
                        );
                    } else if (permission instanceof String) {
                        requirements.add(
                            new PermissionReq(
                                new ListOf<>(
                                    yaml.getString(reqPath).orElse("")
                                )
                            )
                        );
                    } else if (permission instanceof ConfigurationSection) {
                        requirements.add(
                            new PermissionReq(
                                new Colored(
                                    yaml.getString(reqPath + ".fallback").orElse("")
                                ).value(),
                                new ListOf<>(
                                    yaml.getString(reqPath + ".value").orElse("")
                                )
                            )
                        );
                    }
                    break;
                case COOLDOWN:
                    final Object cooldown = yaml.get(reqPath, "");

                    if (cooldown instanceof Integer) {
                        requirements.add(
                            new CooldownReq(
                                yaml.getInt(reqPath)
                            )
                        );
                    } else if (cooldown instanceof ConfigurationSection) {
                        requirements.add(
                            new CooldownReq(
                                new Colored(
                                    yaml.getString(reqPath + ".fallback").orElse("")
                                ).value(),
                                yaml.getInt(reqPath + ".value")
                            )
                        );
                    }
                    break;
                case MONEY:
                    api.config.getWrapped("Vault").ifPresent(wrapped -> {
                        final Object money = yaml.get(reqPath, "");

                        if (money instanceof Integer) {
                            requirements.add(
                                new MoneyReq(
                                    yaml.getInt(reqPath),
                                    (VaultWrapper) wrapped
                                )
                            );
                        } else if (money instanceof ConfigurationSection) {
                            requirements.add(
                                new MoneyReq(
                                    new Colored(
                                        yaml.getString(reqPath + ".fallback").orElse("")
                                    ).value(),
                                    yaml.getInt(reqPath + ".value"),
                                    (VaultWrapper) wrapped
                                )
                            );
                        }
                    });
                    break;
                case GROUP:
                    groupWrappedSupplier.get().ifPresent(groupWrapped -> {
                        final Object group = yaml.get(reqPath, "");

                        if (group instanceof String) {
                            requirements.add(
                                new GroupReq(
                                    yaml.getOrSet(reqPath, ""),
                                    groupWrapped
                                )
                            );
                        } else if (group instanceof ConfigurationSection) {
                            requirements.add(
                                new GroupReq(
                                    new Colored(
                                        yaml.getString(reqPath + ".fallback").orElse("")
                                    ).value(),
                                    yaml.getOrSet(reqPath + ".value", ""),
                                    groupWrapped
                                )
                            );
                        }
                    });
                    break;
                case ISLAND_LEVEL:
                    islandWrappedSupplier.get().ifPresent(islandWrapped -> {
                        final Object level = yaml.get(reqPath, "");

                        if (level instanceof Integer) {
                            requirements.add(
                                new IslandLevelReq(
                                    yaml.getInt(reqPath),
                                    islandWrapped
                                )
                            );
                        } else if (level instanceof ConfigurationSection) {
                            requirements.add(
                                new IslandLevelReq(
                                    new Colored(
                                        yaml.getString(reqPath + ".fallback").orElse("")
                                    ).value(),
                                    yaml.getInt(reqPath + ".value"),
                                    islandWrapped
                                )
                            );
                        }
                    });
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
                            final AtomicReference<String> colored = new AtomicReference<>(
                                new Colored(
                                    list
                                ).value()
                            );

                            placeholder(colored, player);

                            return colored.get().replace("%player_name%", player.getName());
                        },
                        yaml.getStringList(path + "value")
                    );

                    if (!value.isEmpty()) {
                        value.forEach(player::sendMessage);
                        return;
                    }

                    final AtomicReference<String> colored = new AtomicReference<>(
                        new Colored(
                            yaml.getString(path + "value").orElse("")
                        ).value()
                    );

                    if (!colored.get().isEmpty()) {
                        placeholder(colored, player);
                        player.sendMessage(
                            colored.get().replace("%player_name%", player.getName())
                        );
                    }
                };
            case COMMAND:
                return event -> {
                    final List<String> commands = new ArrayList<>();
                    final List<String> value = new Mapped<>(
                        list -> {
                            final AtomicReference<String> cmd = new AtomicReference<>(
                                list
                            );

                            placeholder(cmd, event.getPlayer());

                            return cmd.get().replace("%player_name%", event.getPlayer().getName());
                        },
                        yaml.getStringList(path + "value")
                    );

                    if (!value.isEmpty()) {
                        commands.addAll(value);
                    } else {
                        final AtomicReference<String> command = new AtomicReference<>(
                            yaml.getString(path + "value").orElse("")
                        );

                        if (!command.get().isEmpty()) {
                            placeholder(command, event.getPlayer());
                            commands.add(command.get().replace("%player_name%", event.getPlayer().getName()));
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
                        final Menu menu = TDG.getAPI().menus.findMenuById(s);

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
                    TDG.getAPI().menus.opened.remove(event.getPlayer().getUniqueId());
                };
            case REMOVE_ISLAND_LEVEL:
                return event -> islandWrappedSupplier.get().ifPresent(islandWrapped -> {
                    final UUID uuid = event.getPlayer().getUniqueId();

                    islandWrapped.removeIslandLevel(uuid, yaml.getInt(path + "level"));
                    api.skyBlockFix.skyBlockFix.put(
                        uuid,
                        Math.max(
                            0,
                            api.skyBlockFix.skyBlockFix.getOrDefault(uuid, 0) - yaml.getInt(path + "level")
                        )
                    );
                    api.skyBlockFixOptions.yaml.set(
                        "players." + uuid.toString(),
                        api.skyBlockFix.getOrCreate(uuid)
                    );
                });
            case ADD_ISLAND_LEVEL:
                return event -> islandWrappedSupplier.get().ifPresent(islandWrapped -> {
                    final UUID uuid = event.getPlayer().getUniqueId();

                    islandWrapped.addIslandLevel(uuid, yaml.getInt(path + "level"));
                    api.skyBlockFix.skyBlockFix.put(
                        uuid,
                        api.skyBlockFix.skyBlockFix.getOrDefault(uuid, 0) +
                            yaml.getInt(path + "level")
                    );
                    api.skyBlockFixOptions.yaml.set(
                        "players." + uuid.toString(),
                        api.skyBlockFix.getOrCreate(uuid)
                    );
                });
            case GIVE_MONEY:
                return event -> api.config.getWrapped("Vault").ifPresent(wrapped ->
                    ((VaultWrapper)wrapped).addMoney(event.getPlayer(), yaml.getDouble(path + "value"))
                );
            case TAKE_MONEY:
                return event -> api.config.getWrapped("Vault").ifPresent(wrapped ->
                    ((VaultWrapper)wrapped).removeMoney(event.getPlayer(), yaml.getDouble(path + "value"))
                );
            // TODO: 24/11/2019 More action support
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

    private void placeholder(@NotNull AtomicReference<String> text, @NotNull Player player) {
        TDG.getAPI().config.getWrapped("PlaceholderAPI").ifPresent(wrapped ->
            text.set(((PlaceholderAPIWrapper)wrapped).apply(player, text.get()))
        );
    }

}
