package io.github.portlek.tdg.command;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.tdg.TDGAPI;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.cactoos.iterable.Filtered;
import org.cactoos.list.ListOf;
import org.cactoos.list.Mapped;
import org.cactoos.list.Sorted;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TDGCommand implements TabExecutor {

    @NotNull
    private final TDGAPI api;

    public TDGCommand(@NotNull TDGAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("tdg.use")) {
                sender.sendMessage(api.getLanguage().commands);
            } else {
                sender.sendMessage(api.getLanguage().errorPermission);
            }
            return true;
        }

        final String arg1 = args[0];

        if (args.length == 1) {
            switch (arg1) {
                case "open":
                    sender.sendMessage(api.getLanguage().commands);

                    return true;
                case "close":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(api.getLanguage().errorInGameCommand);
                        return true;
                    }

                    final Player player = (Player) sender;
                    final OpenedMenu openedMenu = api.opened.getOrDefault(player.getUniqueId(), new MckOpenMenu());

                    if (openedMenu instanceof MckOpenMenu) {
                        return true;
                    }

                    openedMenu.close();

                    return true;
                case "list":
                    if (!sender.hasPermission("tdg.list")) {
                        sender.sendMessage(api.getLanguage().errorPermission);
                        return true;
                    }

                    sender.sendMessage(api.getLanguage().generalAvailableMenus);

                    for (String key : api.menus.keySet()) {
                        sender.sendMessage(new Colored("> &e" + key).value());
                    }

                    return true;
                case "reload":
                    if (!sender.hasPermission("tdg.reload")) {
                        sender.sendMessage(api.getLanguage().errorPermission);
                        return true;
                    }

                    api.reloadPlugin();
                    sender.sendMessage(api.getLanguage().generalReloadComplete);

                    return true;

                // TODO: 23/11/2019 edit when plugin added to the spigot page
                /*case "version":
                    if (!sender.hasPermission("tdg.reload")) {
                        sender.sendMessage(api.getLanguage().errorPermission);
                        return true;
                    }

                    sender.sendMessage(api.getLanguage().generalPluginVersion);

                    final UpdateChecker updater = new UpdateChecker(api.tdg, 61903);

                    try {
                        if (updater.checkForUpdates()) {
                            sender.sendMessage(api.getLanguage().generalNewVersionFound(updater.getLatestVersion()));
                        } else {
                            sender.sendMessage(api.getLanguage().generalLatestVersion);
                        }
                    } catch (Exception exception) {
                        api.tdg.getLogger().severe("[TDG] Update checker failed, could not connect to the API.");
                        exception.printStackTrace();
                    }

                    return true;*/
                default:
                    sender.sendMessage(api.getLanguage().errorInvalidArgument);

                    return true;
            }
        }

        final String arg2 = args[1];

        if (args.length == 2 && arg1.equalsIgnoreCase("open")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(api.getLanguage().errorInGameCommand);
                return true;
            }

            final Menu menu = api.findMenuById(arg2);

            if (menu instanceof MckMenu) {
                sender.sendMessage(api.getLanguage().errorMenuNotFound(arg2));
                return true;
            }

            if (!sender.hasPermission("tdg.open." + arg2)) {
                sender.sendMessage(api.getLanguage().errorPermission);
                return true;
            }

            final Player player = (Player) sender;

            if (api.opened.containsKey(player.getUniqueId())) {
                player.sendMessage(api.getLanguage().errorAlreadyOpen);
                return true;
            }

            menu.open(player, false);

            return true;
        }

        final String arg3 = args[2];

        if (arg1.equalsIgnoreCase("open")) {
            final Player player = Bukkit.getPlayer(arg3);

            if (player == null) {
                sender.sendMessage(api.getLanguage().errorPlayerNotFound);
                return true;
            }

            final Menu menu = api.findMenuById(arg2);

            if (menu instanceof MckMenu) {
                sender.sendMessage(api.getLanguage().errorMenuNotFound(arg2));
                return true;
            }

            if (api.opened.containsKey(player.getUniqueId())) {
                player.sendMessage(api.getLanguage().errorAlreadyOpen);
                return true;
            }

            menu.open(player, false);

            return true;
        }

        sender.sendMessage(api.getLanguage().errorInvalidArgument);
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String alias,
                                      @NotNull String[] args) {
        if (args.length == 0 || args.length > 3) {
            return new ListOf<>();
        }

        if (args.length == 1) {
            return new ListOf<>(
                // TODO: 23/11/2019 when version argument activated "version",
                "close",
                "reload",
                "list",
                "open"
            );
        }

        final String arg1 = args[0];
        final String lastWord = args[args.length - 1];

        if (args.length == 2) {
            if (!arg1.equalsIgnoreCase("open")) {
                return new ListOf<>();
            }

            return sort(
                new ListOf<>(
                    new Filtered<>(
                        key -> sender.hasPermission("tdg.open." + key),
                        api.menus.keySet()
                    )
                ),
                lastWord
            );
        }

        if (!args[0].equalsIgnoreCase("open")) {
            return new ListOf<>();
        }

        return listPlayer(sender, lastWord);
    }

    @NotNull
    public List<String> sort(@NotNull List<String> args, @NotNull String lastWord) {
        return new Sorted<>(
            String.CASE_INSENSITIVE_ORDER,
            new ListOf<>(
                new Mapped<>(
                    argument -> argument,
                    new Filtered<>(
                        arg -> StringUtil.startsWithIgnoreCase(arg, lastWord),
                        args
                    )
                )
            )
        );
    }

    @NotNull
    public List<String> listPlayer(@NotNull CommandSender sender,
                                   @NotNull String lastWord) {
        final Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        return new Sorted<>(
            String.CASE_INSENSITIVE_ORDER,
            new ListOf<>(
                new Mapped<>(
                    HumanEntity::getName,
                    new Filtered<>(
                        player -> (senderPlayer != null && !senderPlayer.canSee(player)) ||
                            StringUtil.startsWithIgnoreCase(player.getName(), lastWord),
                        Bukkit.getOnlinePlayers()
                    )
                )
            )
        );
    }

}
