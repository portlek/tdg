package io.github.portlek.tdg;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Menu {

    @NotNull
    String getId();

    @NotNull
    List<String> getCommands();

    void open(@NotNull Player player);

}
