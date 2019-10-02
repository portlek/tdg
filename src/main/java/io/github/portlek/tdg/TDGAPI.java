package io.github.portlek.tdg;

import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.tdg.file.Config;
import io.github.portlek.tdg.file.ConfigOptions;
import io.github.portlek.tdg.file.Language;
import io.github.portlek.tdg.file.LanguageOptions;
import io.github.portlek.tdg.mck.MckFileMenu;
import io.github.portlek.tdg.mck.MckMenu;
import io.github.portlek.tdg.util.MetaData;
import io.github.portlek.tdg.util.Targeted;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TDGAPI {

    private final Map<String, FileMenu> menus = new HashMap<>();

    private final Map<UUID, Menu> opened = new HashMap<>();

    private final List<Entity> entities = new ArrayList<>();

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

    public TDGAPI(@NotNull TDG tdg) {
        this.tdg = tdg;
        configOptions = new ConfigOptions(
            new YamlOf(tdg, "config")
        );
    }

    public void init() {
        // TODO read menu file

        new ListenerBasic<>(PlayerJoinEvent.class, event ->
            opened.put(event.getPlayer().getUniqueId(), new MckMenu())
        ).register(tdg);

        new ListenerBasic<>(PlayerArmorStandManipulateEvent.class, event -> {
            if (entities.contains(event.getRightClicked()))
                event.setCancelled(true);
        }).register(tdg);

        new ListenerBasic<>(EntityDamageEvent.class, event -> {
            if (entities.contains(event.getEntity()))
                event.setCancelled(true);
        }).register(tdg);

        new ListenerBasic<>(PlayerChangedWorldEvent.class, event -> {
            Menu menu = opened.getOrDefault(event.getPlayer().getUniqueId(), new MckMenu());
            if (!(menu instanceof MckMenu))
                menu.close();
        }).register(tdg);

        menus.values().forEach(fileMenu -> fileMenu.register(tdg));
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
    public List<Entity> getEntities() {
        return entities;
    }

    @NotNull
    public Map<UUID, Menu> getOpened() {
        return opened;
    }

    public boolean hasOpen(@NotNull Player player) {
        return !(opened.getOrDefault(player.getUniqueId(), new MckMenu()) instanceof MckMenu);
    }

    @NotNull
    public Config getConfigs() {
        if (configInstance == null)
            configInstance = configOptions.value();

        return configInstance;
    }

    @NotNull
    public Language getLanguage() {
        if (languageInstance == null)
            languageInstance = new LanguageOptions(
                getConfigs().getPrefix(),
                getLanguageFile()
            ).value();

        return languageInstance;
    }

    @NotNull
    private IYaml getLanguageFile() {
        if (languageFileInstance == null) {
            languageFileInstance = new YamlOf(
                tdg,
                "languages",
                getConfigs().getChosenLanguage()
            );
            languageFileInstance.create();
        }

        return languageFileInstance;
    }

}
