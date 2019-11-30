package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.IconClickEvent;
import io.github.portlek.tdg.api.events.IconHoverEvent;
import io.github.portlek.tdg.api.events.MenuCloseEvent;
import io.github.portlek.tdg.api.events.MenuOpenEvent;
import io.github.portlek.tdg.api.events.abs.IconEvent;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.util.Cooldown;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CooldownReq implements Requirement {

    @NotNull
    private final String fallback;

    private final int cooldown;

    public CooldownReq(@NotNull String fallback, int cooldown) {
        this.fallback = fallback;
        this.cooldown = cooldown;
    }

    public CooldownReq(int cooldown) {
        this("", cooldown);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final String menuIconId;

        if (event instanceof MenuOpenEvent || event instanceof MenuCloseEvent) {
            menuIconId = event.getOpenedMenu().getParent().getId();
        } else if (event instanceof IconClickEvent || event instanceof IconHoverEvent) {
            menuIconId = event.getOpenedMenu().getParent().getId() + ">"
                + ((IconEvent) event).getLiveIcon().getParent().getId();
        } else {
            return true;
        }

        if (menuIconId.isEmpty()) {
            return true;
        }

        final Player player = event.getPlayer();

        if (!Cooldown.isInCooldown(player.getUniqueId(), menuIconId)) {
            new Cooldown(player.getUniqueId(), menuIconId, cooldown).start();
            return true;
        }

        if (!fallback.isEmpty()) {
            player.sendMessage(fallback
                .replaceAll("%cooldown%", String.valueOf(Cooldown.getTimeLeft(player.getUniqueId(), menuIconId)))
            );
        }

        return false;
    }

}
