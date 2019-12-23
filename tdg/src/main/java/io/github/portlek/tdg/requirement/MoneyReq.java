package io.github.portlek.tdg.requirement;

import io.github.portlek.tdg.api.Requirement;
import io.github.portlek.tdg.api.events.abs.MenuEvent;
import io.github.portlek.tdg.hooks.VaultWrapper;
import org.jetbrains.annotations.NotNull;

public final class MoneyReq implements Requirement {

    @NotNull
    private final String fallback;

    private final int money;

    @NotNull
    private final VaultWrapper wrapper;

    public MoneyReq(@NotNull String fallback, int money, @NotNull VaultWrapper wrapper) {
        this.fallback = fallback;
        this.money = money;
        this.wrapper = wrapper;
    }

    public MoneyReq(int money, @NotNull VaultWrapper wrapper) {
        this("", money, wrapper);
    }

    @Override
    public boolean control(@NotNull MenuEvent event) {
        final boolean check = wrapper.getMoney(event.getPlayer()) >= money;

        if (!check && !fallback.isEmpty()) {
            event.getPlayer().sendMessage(fallback
                .replace("%money%", String.valueOf(money))
            );
        }

        return check;
    }

}
