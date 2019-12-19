package io.github.portlek.tdg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.tdg.TDGAPI;
import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.OpenedMenu;
import io.github.portlek.tdg.api.mock.MckMenu;
import io.github.portlek.tdg.api.mock.MckOpenMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@CommandAlias("tdg")
public final class TDGCommand extends BaseCommand {

    @NotNull
    private final TDGAPI api;

    public TDGCommand(@NotNull TDGAPI api) {
        this.api = api;
    }

    @Default
    @CommandPermission("tdg.command.main")
    public void noArg(CommandSender sender) {
        sender.sendMessage(
            api.language.commands
        );
    }

    @HelpCommand()
    @CommandPermission("tdg.command.help")
    public void doHelp(CommandIssuer issuer) {
        issuer.sendMessage(
            api.language.commands
        );
    }

    @Subcommand("reload")
    @CommandPermission("tdg.command.reload")
    public void reload(CommandSender sender) {
        api.reloadPlugin(false);
        sender.sendMessage(
            api.language.generalReloadComplete
        );
    }

    @Subcommand("close")
    @CommandPermission("tdg.command.close")
    public void close(Player player) {
        final OpenedMenu openedMenu = api.menus.opened.getOrDefault(player.getUniqueId(), new MckOpenMenu());

        if (openedMenu instanceof MckOpenMenu) {
            return;
        }

        openedMenu.close();
        api.menus.opened.remove(player.getUniqueId());
    }

    @Subcommand("list")
    @CommandPermission("tdg.command.list")
    public void list(CommandSender sender) {
        sender.sendMessage(api.language.generalAvailableMenus);

        for (String key : api.menus.menus.keySet()) {
            sender.sendMessage(new Colored("> &e" + key).value());
        }
    }

    @Subcommand("open")
    @CommandPermission("tdg.command.open")
    @CommandCompletion("@menus @players")
    public void open(CommandSender sender, @Conditions("player:arg=1") String[] args) {
        final Menu menu;
        final Player target;

        if (args.length > 0) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    target = (Player) sender;
                } else {
                    sender.sendMessage(
                        api.language.errorInGameCommand
                    );
                    return;
                }
            } else {
                target = Objects.requireNonNull(Bukkit.getPlayer(args[1]));
            }

            menu = api.menus.findMenuById(args[0]);
        } else {
            return;
        }

        if (menu instanceof MckMenu) {
            sender.sendMessage(api.language.errorMenuNotFound(args[0]));
            return;
        }

        if (api.menus.opened.containsKey(target.getUniqueId())) {
            sender.sendMessage(api.language.errorAlreadyOpen);
            return;
        }

        menu.open(target, false);
    }

}
