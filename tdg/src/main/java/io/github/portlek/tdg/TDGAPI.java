package io.github.portlek.tdg;

import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import io.github.portlek.tdg.api.type.ClickType;
import io.github.portlek.tdg.file.*;
import io.github.portlek.tdg.util.ListenerBasic;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class TDGAPI {

    @NotNull
    public final TDG tdg;

    @NotNull
    private final ConfigOptions configOptions;

    @NotNull
    private final LanguageOptions languageOptions;

    @NotNull
    private final MenusOptions menusOptions;

    @NotNull
    public Config config;

    @NotNull
    public Language language;

    @NotNull
    public Menus menus;

    public TDGAPI(@NotNull TDG tdg) {
        this.tdg = tdg;
        this.configOptions = new ConfigOptions(
            new YamlOf(tdg, "config")
        );
        this.config = configOptions.value();
        this.languageOptions = new LanguageOptions(
            new YamlOf(tdg, "languages", config.language),
            config
        );
        this.language = languageOptions.value();
        this.menusOptions = new MenusOptions(
            new YamlOf(tdg, "menus")
        );
        this.menus = menusOptions.value();
    }

    public void reloadPlugin(boolean first) {
        disablePlugin();

        if (!first) {
            config = configOptions.value();
            language = languageOptions.value();
            menus = menusOptions.value();
        }

        new ListenerBasic<>(
            PlayerArmorStandManipulateEvent.class,
            event -> menus.entities.contains(event.getRightClicked()),
            event -> event.setCancelled(true)
        ).register(tdg);

        new ListenerBasic<>(
            EntityDamageEvent.class,
            event -> menus.entities.contains(event.getEntity()),
            event -> event.setCancelled(true)
        ).register(tdg);

        new ListenerBasic<>(PlayerChangedWorldEvent.class, event -> {
            final OpenedMenu openedMenu = menus.opened.getOrDefault(event.getPlayer().getUniqueId(), new MckOpenMenu());

            if (!(openedMenu instanceof MckOpenMenu)) {
                openedMenu.close();
                menus.opened.remove(event.getPlayer().getUniqueId());
            }
        }).register(tdg);

        new ListenerBasic<>(PlayerCommandPreprocessEvent.class, event -> {
            final Player player = event.getPlayer();
            final String command = event.getMessage().replace("/", "");
            final Menu menu = menus.findMenuByCommand(command);

            if (menu instanceof MckMenu) {
                return;
            }

            event.setCancelled(true);

            if (menus.opened.containsKey(player.getUniqueId())) {
                player.sendMessage(language.errorAlreadyOpen);
                return;
            }

            if (!menu.hasPermission(player)) {
                player.sendMessage(language.errorPermission);
                return;
            }

            menu.open(player, false);
        }).register(tdg);

        new ListenerBasic<>(PlayerInteractEvent.class,
            event -> menus.opened.containsKey(event.getPlayer().getUniqueId()),
            event ->
                menus.getIconOptional(
                    event.getPlayer()
                ).ifPresent(entry -> {
                    final IconClickEvent iconClickEvent = new IconClickEvent(
                        event.getPlayer(),
                        entry.getKey(),
                        entry.getValue(),
                        ClickType.fromInteractEvent(event)
                    );

                    tdg.getServer().getPluginManager().callEvent(iconClickEvent);

                    if (iconClickEvent.isCancelled()) {
                        return;
                    }

                    entry.getValue().getParent().accept(iconClickEvent);
                })
        ).register(tdg);

        new ListenerBasic<>(PlayerInteractAtEntityEvent.class,
            event -> menus.opened.containsKey(event.getPlayer().getUniqueId()),
            event ->
                menus.getIconOptional(
                    event.getPlayer()
                ).ifPresent(entry -> {
                    final IconClickEvent iconClickEvent = new IconClickEvent(
                        event.getPlayer(),
                        entry.getKey(),
                        entry.getValue(),
                        ClickType.fromInteractEvent(event)
                    );

                    tdg.getServer().getPluginManager().callEvent(iconClickEvent);

                    if (iconClickEvent.isCancelled()) {
                        return;
                    }

                    entry.getValue().getParent().accept(iconClickEvent);
                })
        ).register(tdg);
    }

    public void disablePlugin() {
        menus.clear();
        tdg.getServer().getScheduler().cancelTasks(tdg);
        HandlerList.unregisterAll(tdg);
    }

}
