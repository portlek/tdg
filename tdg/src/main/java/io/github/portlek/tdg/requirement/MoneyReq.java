package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public final class MoneyReq implements Requirement {

    private final int money;

    public MoneyReq(int money) {
        this.money = money;
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        return !TDG.getAPI().getConfig().hooksVault ||
            TDG.getAPI().getConfig().vault.get().getBalance(event.getPlayer()) >= money;
    }

}
