package io.github.portlek.tdg.api;

import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Menu extends Id, BiAcceptable<MenuOpenEvent, MenuCloseEvent> {

    boolean hasPermission(@NotNull CommandSender sender);

    boolean is(@NotNull String command);

    void open(@NotNull Player player, boolean changed);

}
