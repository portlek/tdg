package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.TDG;
import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import org.jetbrains.annotations.NotNull;

public final class MoneyReq implements Requirement {

    @NotNull
    private final String fallback;

    private final int money;

    public MoneyReq(@NotNull String fallback, int money) {
        this.fallback = fallback;
        this.money = money;
    }

    public MoneyReq(int money) {
        this("", money);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final boolean check = !TDG.getAPI().getConfig().hooksVault ||
            TDG.getAPI().getConfig().vault.get().getBalance(event.getPlayer()) >= money;

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(fallback
                .replaceAll("%money%", String.valueOf(money))
            );
        }

        return check;
    }

}
