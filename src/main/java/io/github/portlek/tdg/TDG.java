package io.github.portlek.tdg;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TDG extends JavaPlugin {

    private final List<Entity> entities = new ArrayList<>();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        entities.forEach(Entity::remove);
    }

}
