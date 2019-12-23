package io.github.portlek.tdg.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.tdg.TDGAPI;
import io.github.portlek.tdg.api.Icon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.type.IconType;
import io.github.portlek.tdg.icon.BasicIcon;
import io.github.portlek.tdg.menu.BasicMenu;
import io.github.portlek.tdg.util.TargetParsed;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MenusOptions implements Scalar<Menus> {

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final TDGAPI api;

    public MenusOptions(@NotNull IYaml yaml, @NotNull TDGAPI api) {
        this.yaml = yaml;
        this.api = api;
    }

    @Override
    public Menus value() {
        yaml.create();

        final Map<String, Menu> menus = new HashMap<>();

        for (String menuId : yaml.getSection("menus").getKeys(false)) {
            final List<Icon> icons = new ArrayList<>();

            for (String iconId : yaml.getSection("menus." + menuId + ".icons").getKeys(false)) {
                icons.add(
                    new BasicIcon(
                        iconId,
                        new Colored(
                            yaml.getString("menus." + menuId + ".icons." + iconId + ".name")
                                .orElse("")
                        ).value(),
                        IconType.fromString(
                            yaml.getString("menus." + menuId + ".icons." + iconId + ".icon-type")
                                .orElse("")
                        ),
                        yaml.getString("menus." + menuId + ".icons." + iconId + ".material")
                            .orElse(""),
                        yaml.getByte("menus." + menuId + ".icons." + iconId + ".material-data"),
                        yaml.getString("menus." + menuId + ".icons." + iconId + ".value")
                            .orElse(""),
                        new TargetParsed<>(IconClickEvent.class, yaml, menuId, iconId, api).parse(),
                        new TargetParsed<>(IconHoverEvent.class, yaml, menuId, iconId, api).parse(),
                        yaml.getInt("menus." + menuId + ".icons." + iconId + ".position-x"),
                        yaml.getInt("menus." + menuId + ".icons." + iconId + ".position-y")
                    )
                );
            }

            menus.put(
                menuId,
                new BasicMenu(
                    menuId,
                    yaml.getStringList("menus." + menuId + ".commands"),
                    new TargetParsed<>(MenuCloseEvent.class, yaml, menuId, api).parse(),
                    new TargetParsed<>(MenuOpenEvent.class, yaml, menuId, api).parse(),
                    yaml.getInt("menus." + menuId + ".distances.x1"),
                    yaml.getInt("menus." + menuId + ".distances.x2"),
                    yaml.getInt("menus." + menuId + ".distances.x4"),
                    yaml.getInt("menus." + menuId + ".distances.x5"),
                    icons
                )
            );
        }

        return new Menus(
            menus
        );
    }
}
