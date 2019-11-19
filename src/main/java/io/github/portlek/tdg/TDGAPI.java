package io.github.portlek.tdg;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.tdg.api.IconClickedEvent;
import io.github.portlek.tdg.file.Config;
import io.github.portlek.tdg.file.ConfigOptions;
import io.github.portlek.tdg.file.Language;
import io.github.portlek.tdg.file.LanguageOptions;
import io.github.portlek.tdg.mock.MckMenu;
import io.github.portlek.tdg.mock.MckOpenMenu;
import io.github.portlek.tdg.util.TargetMenu;
import io.github.portlek.tdg.util.Targeted;
import io.github.portlek.tdg.util.UpdateChecker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
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
    public final TDG tdg;

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

    public void reloadPlugin() {
        tdg.getServer().getScheduler().cancelTasks(tdg);
        HandlerList.unregisterAll(tdg);
        entities.forEach(Entity::remove);
        entities.clear();
        menus.clear();
        opened.clear();

        init();
    }

    private void init() {
        loadMenus();

        new ListenerBasic<>(PlayerJoinEvent.class, event -> {
            final Player player = event.getPlayer();

            if (getConfigs().updateCheck && player.hasPermission("tdg.version")) {
                final UpdateChecker updater = new UpdateChecker(tdg, 0);

                try {
                    if (updater.checkForUpdates()) {
                        player.sendMessage(
                            new Colored(
                                "&8[&cTDG&8] &bA new update of TDG &f&l(" +
                                    updater.getLatestVersion() +
                                    ") &bis available! Download it at &f&l" + updater.getResourceURL()
                            ).value()
                        );
                    }
                } catch (Exception ex) {
                    tdg.getLogger().severe("[TDG] Update checker failed, could not connect to the API!");
                    ex.printStackTrace();
                }
            }

            opened.put(player.getUniqueId(), new MckOpenMenu());
        }).register(tdg);

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

        new ListenerBasic<>(PlayerCommandPreprocessEvent.class, event -> {
            final Player player = event.getPlayer();
            final String command = event.getMessage();
            final Menu menu = findMenuByCommand(command);

            if (menu instanceof MckMenu) {
                return;
            }

            event.setCancelled(true);

            if (opened.containsKey(player.getUniqueId())) {
                player.sendMessage(getLanguage().errorAlreadyOpen);
                return;
            }

            if (!player.hasPermission("tdg.open." + menu.getId())) {
                player.sendMessage(getLanguage().errorPermission);
                return;
            }

            menu.open(player);
        }).register(tdg);

        new ListenerBasic<>(PlayerInteractEvent.class, event -> {
            if (event.getAction() != Action.LEFT_CLICK_AIR) {
                return;
            }

            final Player player = event.getPlayer();
            final Entity entity = new Targeted(player).value();

            if (entity == player) {
                return;
            }

            final OpenedMenu openedMenu = new TargetMenu(entity).value();

            if (openedMenu instanceof MckOpenMenu) {
                return;
            }

            final List<Icon> icons = openedMenu.getIconsFor();
            Icon clickedIcon = null;

            for (Icon icon : icons) {
                if (icon.is(entity)) {
                    clickedIcon = icon;
                    break;
                }
            }

            if (clickedIcon == null) {
                return;
            }

            final IconClickedEvent iconClickedEvent = new IconClickedEvent(player, openedMenu, clickedIcon);

            tdg.getServer().getPluginManager().callEvent(iconClickedEvent);

            if (iconClickedEvent.isCancelled()) {
                return;
            }

            clickedIcon.acceptClickEvent(player);
        }).register(tdg);
    }

    @NotNull
    public Menu findMenuByCommand(@NotNull String command) {
        for (Menu menu : menus.values()) {
            if (menu.getCommands().contains(command)) {
                return menu;
            }
        }

        return new MckMenu();
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
                getConfigs().pluginPrefix,
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

    private void loadMenus() {

    }

}
