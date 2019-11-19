package io.github.portlek.tdg;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OpenedMenu extends Menu {

    @NotNull
    List<Icon> getIconsFor();

    void close();


}
