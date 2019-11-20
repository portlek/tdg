package io.github.portlek.tdg;

import io.github.portlek.tdg.events.MenuCloseEvent;
import io.github.portlek.tdg.events.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Menu {

    @NotNull
    String getId();

    @NotNull
    List<String> getCommands();

    @NotNull
    List<Icon> getIcons();

    void open(@NotNull Player player);

    void accept(@NotNull MenuOpenEvent event);

    void acceptClose(@NotNull MenuCloseEvent event);

}
