package io.github.portlek.tdg.api.mock;

import io.github.portlek.tdg.api.Menu;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MckMenu implements Menu {
    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(@NotNull String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void open(@NotNull Player player, boolean changed) {
        throw new UnsupportedOperationException();
    }
    @NotNull
    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }
    @Override
    public void accept(@NotNull MenuOpenEvent event) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void exec(@NotNull MenuCloseEvent event) {
        throw new UnsupportedOperationException();
    }
}
