package io.github.portlek.tdg.util;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.*;
import io.github.portlek.tdg.events.IconClickEvent;
import io.github.portlek.tdg.events.IconHoverEvent;
import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import io.github.portlek.tdg.events.abs.MenuEvent;
import io.github.portlek.tdg.particle.Particles;
import io.github.portlek.tdg.requirements.ClickTypeReq;
import io.github.portlek.tdg.requirements.PermissionReq;
import io.github.portlek.tdg.target.BasicTarget;
import org.bukkit.Bukkit;
import org.cactoos.collection.Filtered;
import org.cactoos.list.Joined;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
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
                    final String reqPath = path + ".requirements." + requirement;

                    switch (requirementType) {
                        case CLICK_TYPE:
                            final List<String> clickTypesString = yaml.getStringList(reqPath);

                            if (!clickTypesString.isEmpty()) {
                                return new ClickTypeReq(
                                    new Mapped<>(
                                        ClickType::fromString,
                                        clickTypesString
                                    )
                                );
                            }

                            return yaml.getString(reqPath)
                                .flatMap(s -> Optional.of(new ClickTypeReq(ClickType.fromString(s))))
                                .orElse(new ClickTypeReq(ClickType.ANY));
                        case PERMISSIONS:
                            final List<String> permissionsString = yaml.getStringList(reqPath);

                            if (!permissionsString.isEmpty()) {
                                return new PermissionReq(permissionsString);
                            }

                            return yaml.getString(reqPath)
                                .flatMap(s -> Optional.of(new PermissionReq(s)))
                                .orElse(new PermissionReq());
                        case NONE:
                        default:
                            return event -> true;
                    }
                },
                yaml.getSection(path + ".requirements").getKeys(false)
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
                return event -> new Joined<>(
                    new ListOf<>(
                        new Filtered<>(
                            filtered -> !filtered.isEmpty(),
                            new ListOf<>(
                                yaml.getString(path + "value").orElse("")
                            )
                        )
                    ),
                    yaml.getStringList(path + "value")
                ).forEach(s -> event.getPlayer().sendMessage(
                    new Colored(s).value()
                ));
            case COMMAND:
                return event -> new Mapped<>(
                    command -> new Colored(
                        command.replaceAll("%player%", event.getPlayer().getName())
                    ).value(),
                    new Joined<>(
                        new ListOf<>(
                            new Filtered<>(
                                filtered -> !filtered.isEmpty(),
                                new ListOf<>(
                                    yaml.getString(path + "value").orElse("")
                                )
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

                        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                    }
                ));
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
                return event -> new Particles(
                    Utils.getBFLoc(event.getPlayer().getLocation(), 3.5).add(0, 2.5, 0),
                    yaml.getString(path + "value").orElse("clound"),
                    yaml.getInt(path + "amount"),
                    yaml.getInt(path + "speed")
                ).spawnParticles();
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
