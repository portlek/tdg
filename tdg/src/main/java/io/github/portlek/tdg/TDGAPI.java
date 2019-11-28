package io.github.portlek.tdg;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.mcyaml.mck.MckFileConfiguration;
import io.github.portlek.tdg.api.LiveIcon;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.mock.MckLiveIcon;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import io.github.portlek.tdg.api.type.ClickType;
import io.github.portlek.tdg.api.type.IconType;
import io.github.portlek.tdg.file.Config;
import io.github.portlek.tdg.file.ConfigOptions;
import io.github.portlek.tdg.file.Language;
import io.github.portlek.tdg.file.LanguageOptions;
import io.github.portlek.tdg.icon.BasicIcon;
import io.github.portlek.tdg.menu.BasicMenu;
import io.github.portlek.tdg.util.ListenerBasic;
import io.github.portlek.tdg.util.TargetMenu;
import io.github.portlek.tdg.util.TargetParsed;
import io.github.portlek.tdg.util.Targeted;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * player and last location of the player
     */
    public final Map<Player, Location> lastLocations = new HashMap<>();

    public final List<Entity> entities = new ArrayList<>();

    @NotNull
    public final TDG tdg;

    @NotNull
    private final ConfigOptions configOptions;

    @Nullable
    private Config configInstance;

    @Nullable
    private Language languageInstance;

    @Nullable
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
        disablePlugin();

        menusFile.create();
        if (menusFile.getSection("menus") instanceof MckFileConfiguration) {
            menusFile.createSection("menus");
        }

        init();
    }

    public void disablePlugin() {
        opened.values().forEach(OpenedMenu::close);
        tdg.getServer().getScheduler().cancelTasks(tdg);
        HandlerList.unregisterAll(tdg);
        entities.forEach(Entity::remove);
        entities.clear();
        menus.clear();
        opened.clear();
    }

    @NotNull
    public Menu findMenuByCommand(@NotNull String command) {
        for (Menu menu : menus.values()) {
            if (menu.is(command)) {
                return menu;
            }
        }

        return new MckMenu();
    }

    @NotNull
    public Menu findMenuById(@NotNull String id) {
        return menus.getOrDefault(id, new MckMenu());
    }

    @NotNull
    public OpenedMenu findOpenMenuByUUID(@NotNull UUID uuid) {
        return opened.getOrDefault(uuid, new MckOpenMenu());
    }

    public boolean hasOpen(@NotNull Player player) {
        return !(opened.getOrDefault(player.getUniqueId(), new MckOpenMenu()) instanceof MckOpenMenu);
    }

    @NotNull
    public Config getConfig() {
        if (configInstance == null) {
            configInstance = configOptions.value();
        }

        return configInstance;
    }

    @NotNull
    public Language getLanguage() {
        if (languageInstance == null) {
            languageInstance = new LanguageOptions(
                getConfig().pluginPrefix,
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
                getConfig().language
            );
            languageFileInstance.create();
        }

        return languageFileInstance;
    }

    private void init() {
        getLanguage();

        menus.putAll(
            new MapOf<String, Menu>(
                new Mapped<>(
                    menuId -> new MapEntry<>(
                        menuId,
                        new BasicMenu(
                            menuId,
                            menusFile.getStringList("menus." + menuId + ".commands"),
                            new TargetParsed<>(MenuCloseEvent.class, menusFile, menuId).parse(),
                            new TargetParsed<>(MenuOpenEvent.class, menusFile, menuId).parse(),
                            menusFile.getInt("menus." + menuId + ".distances.x1"),
                            menusFile.getInt("menus." + menuId + ".distances.x2"),
                            menusFile.getInt("menus." + menuId + ".distances.x4"),
                            menusFile.getInt("menus." + menuId + ".distances.x5"),
                            new ListOf<>(
                                new Mapped<>(
                                    iconId -> new BasicIcon(
                                        iconId,
                                        new Colored(
                                            menusFile.getString("menus." + menuId + ".icons." + iconId + ".name")
                                                .orElse("")
                                        ).value(),
                                        IconType.fromString(
                                            menusFile.getString("menus." + menuId + ".icons." + iconId + ".icon-type")
                                                .orElse("")
                                        ),
                                        menusFile.getString("menus." + menuId + ".icons." + iconId + ".material")
                                            .orElse(""),
                                        menusFile.getByte("menus." + menuId + ".icons." + iconId + ".material-data"),
                                        menusFile.getString("menus." + menuId + ".icons." + iconId + ".value")
                                            .orElse(""),
                                        new TargetParsed<>(IconClickEvent.class, menusFile, menuId, iconId).parse(),
                                        new TargetParsed<>(IconHoverEvent.class, menusFile, menuId, iconId).parse(),
                                        menusFile.getInt("menus." + menuId + ".icons." + iconId + ".position-x"),
                                        menusFile.getInt("menus." + menuId + ".icons." + iconId + ".position-y")
                                    ),
                                    menusFile.getSection("menus." + menuId + ".icons").getKeys(false)
                                )
                            )
                        )
                    ),
                    menusFile.getSection("menus").getKeys(false)
                )
            )
        );

        // TODO: 23/11/2019 edit when plugin added to the spigot page
        /*new ListenerBasic<>(PlayerJoinEvent.class, event -> {
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
        }).register(tdg);*/

        new ListenerBasic<>(
            PlayerArmorStandManipulateEvent.class,
            event -> entities.contains(event.getRightClicked()),
            event -> event.setCancelled(true)
        ).register(tdg);

        new ListenerBasic<>(
            EntityDamageEvent.class,
            event -> entities.contains(event.getEntity()),
            event -> event.setCancelled(true)
        ).register(tdg);

        new ListenerBasic<>(PlayerChangedWorldEvent.class, event -> {
            final OpenedMenu openedMenu = opened.getOrDefault(event.getPlayer().getUniqueId(), new MckOpenMenu());

            if (!(openedMenu instanceof MckOpenMenu)) {
                openedMenu.close();
                TDG.getAPI().opened.remove(event.getPlayer().getUniqueId());
            }
        }).register(tdg);

        new ListenerBasic<>(PlayerCommandPreprocessEvent.class, event -> {
            final Player player = event.getPlayer();
            final String command = event.getMessage().replaceAll("/", "");
            final Menu menu = findMenuByCommand(command);

            if (menu instanceof MckMenu) {
                return;
            }

            event.setCancelled(true);

            if (opened.containsKey(player.getUniqueId())) {
                player.sendMessage(getLanguage().errorAlreadyOpen);
                return;
            }

            if (!menu.hasPermission(player)) {
                player.sendMessage(getLanguage().errorPermission);
                return;
            }

            menu.open(player, false);
        }).register(tdg);

        new ListenerBasic<>(PlayerInteractEvent.class,
            event -> opened.containsKey(event.getPlayer().getUniqueId()),
            event ->
                getIconOptional(
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

                    entry.getValue().accept(iconClickEvent);
                })
        ).register(tdg);

        new ListenerBasic<>(PlayerInteractAtEntityEvent.class,
            event -> opened.containsKey(event.getPlayer().getUniqueId()),
            event ->
                getIconOptional(
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

                    entry.getValue().accept(iconClickEvent);
                })
        ).register(tdg);
    }

    @NotNull
    private Optional<Map.Entry<OpenedMenu, LiveIcon>> getIconOptional(@NotNull Player player) {
        final Map.Entry<LiveIcon, OpenedMenu> entry = findIconAndOpenedMenuByPlayer(player);
        final LiveIcon liveIcon = entry.getKey();
        final OpenedMenu openedMenu = entry.getValue();

        if (liveIcon instanceof MckLiveIcon || openedMenu instanceof MckOpenMenu) {
            return Optional.empty();
        }

        return Optional.of(
            new MapEntry<>(
                openedMenu,
                liveIcon
            )
        );
    }

    @NotNull
    private Map.Entry<LiveIcon, OpenedMenu> findIconAndOpenedMenuByPlayer(@NotNull Player player) {
        final Entity targeted = new Targeted(player).value();

        if (targeted == null) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        final OpenedMenu openedMenu = new TargetMenu(
            targeted
        ).value();

        if (openedMenu instanceof MckOpenMenu) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        final LiveIcon liveIcon = openedMenu.findByEntity(targeted);

        if (liveIcon instanceof MckLiveIcon) {
            return new MapEntry<>(
                new MckLiveIcon(),
                new MckOpenMenu()
            );
        }

        return new MapEntry<>(liveIcon, openedMenu);
    }

}
