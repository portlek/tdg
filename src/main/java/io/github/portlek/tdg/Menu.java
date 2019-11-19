package io.github.portlek.tdg;

import io.github.portlek.tdg.api.MenuCloseEvent;
import io.github.portlek.tdg.api.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Menu {

    @NotNull
    String getId();

    @NotNull
    List<String> getCommands();

    void open(@NotNull Player player);

    void accept(@NotNull MenuCloseEvent event);

    void accept(@NotNull MenuOpenEvent event);
}
