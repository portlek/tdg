package io.github.portlek.tdg;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OpenedMenu {

    void close();

    void addIcons(@NotNull List<LiveIcon> liveIcons);

}
