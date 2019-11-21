package io.github.portlek.tdg;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Icon {

    @NotNull
    LiveIcon createFor(@NotNull Player player);

}
