package io.github.portlek.tdg;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.tdg.file.*;
import io.github.portlek.tdg.mock.MckOpenMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TDGAPI {

    /**
     * menu id and menu
     */
    public final Map<String, Menu> menus = new HashMap<>();

    /**
     * player uuid and opened menu
     */
    public final Map<UUID, OpenedMenu> opened = new HashMap<>();

    public final List<Entity> entities = new ArrayList<>();

    @NotNull
    private final TDG tdg;

    @NotNull
    private final ConfigOptions configOptions;

    /**
     * @deprecated Use {@link #getConfigs()}
     */
    @Deprecated
    private Config configInstance;

    /**
     * @deprecated Use {@link #getLanguage()} ()}
     */
    @Deprecated
    private Language languageInstance;

    /**
     * @deprecated Use {@link #getLanguageFile()} ()}
     */
    @Deprecated
    private IYaml languageFileInstance;

    @NotNull
    private final IYaml menusFile;

    public TDGAPI(@NotNull TDG tdg) {
        this.tdg = tdg;
        configOptions = new ConfigOptions(
            new YamlOf(tdg, "config")
        );
        menusFile = new YamlOf(tdg, "menus");
    }

    public void init() {
        // TODO read menu file

        new ListenerBasic<>(PlayerJoinEvent.class, event ->
            opened.put(event.getPlayer().getUniqueId(), new MckOpenMenu())
        ).register(tdg);

        new ListenerBasic<>(PlayerArmorStandManipulateEvent.class, event -> {
            if (entities.contains(event.getRightClicked())) {
                event.setCancelled(true);
            }
        }).register(tdg);

        new ListenerBasic<>(EntityDamageEvent.class, event -> {
            if (entities.contains(event.getEntity())) {
                event.setCancelled(true);
            }
        }).register(tdg);

        new ListenerBasic<>(PlayerChangedWorldEvent.class, event -> {
            final OpenedMenu openedMenu = opened.getOrDefault(event.getPlayer().getUniqueId(), new MckOpenMenu());

            if (!(openedMenu instanceof MckOpenMenu)) {
                openedMenu.close();
            }
        }).register(tdg);
    }

    public void reloadPlugin() {
        tdg.getServer().getScheduler().cancelTasks(tdg);
        HandlerList.unregisterAll(tdg);
        entities.forEach(Entity::remove);
        entities.clear();
        menus.clear();
        opened.clear();

        init();
    }

    @NotNull
    public Menu findMenuById(@NotNull String id) {
        return menus.getOrDefault(id, new MckOpenMenu());
    }

    @NotNull
    public OpenedMenu findOpenMenuByUUID(@NotNull UUID uuid) {
        return opened.getOrDefault(uuid, new MckOpenMenu());
    }

    public boolean hasOpen(@NotNull Player player) {
        return !(opened.getOrDefault(player.getUniqueId(), new MckOpenMenu()) instanceof MckOpenMenu);
    }

    @NotNull
    public Config getConfigs() {
        if (configInstance == null) {
            configInstance = configOptions.value();
        }

        return configInstance;
    }

    @NotNull
    public Language getLanguage() {
        if (languageInstance == null) {
            languageInstance = new LanguageOptions(
                getConfigs(),
                getLanguageFile()
            ).value();
        }

        return languageInstance;
    }

    @NotNull
    private IYaml getLanguageFile() {
        if (languageFileInstance == null) {
            languageFileInstance = new YamlOf(
                tdg,
                "languages",
                getConfigs().language
            );
            languageFileInstance.create();
        }

        return languageFileInstance;
    }

}
