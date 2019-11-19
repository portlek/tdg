package io.github.portlek.tdg.command;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.tdg.Menu;
import io.github.portlek.tdg.TDGAPI;
import io.github.portlek.tdg.mock.MckMenu;
import io.github.portlek.tdg.util.UpdateChecker;
import net.sf.cglib.asm.$ByteVector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

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
                case "list":
                    if (!sender.hasPermission("tdg.list")) {
                        sender.sendMessage(api.getLanguage().errorPermission);
                        return true;
                    }

                    sender.sendMessage(api.getLanguage().generalAvailableMenus);

                    for (String key : api.menus.keySet()) {
                        sender.sendMessage(new Colored("6e" + key).value());
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
                case "version":
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
                    } catch (Exception ex) {
                        api.tdg.getLogger().severe("[TDG] Update checker failed, could not connect to the API.");
                        ex.printStackTrace();
                    }

                    return true;
                default:
                    sender.sendMessage(api.getLanguage().errorInvalidArgument);
                    return true;
            }
        }

        final String arg2 = args[1];

        if (args[0].equals("open")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(api.getLanguage().errorInGameCommand);
                return true;
            }

            final Menu menu = api.findMenuById(arg1);

            if (menu instanceof MckMenu) {
                sender.sendMessage(api.getLanguage().errorInvalidMenu);
                return true;
            }

            if (!sender.hasPermission("tdg.open." + args[1])) {
                sender.sendMessage(api.getLanguage().errorPermission);
                return true;
            }

            final Player player = (Player) sender;

            if (api.opened.containsKey(player.getUniqueId())) {
                player.sendMessage(api.getLanguage().errorAlreadyOpen);
                return true;
            }

            menu.open(player);
            
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


        return new ListOf<>();
    }
}
