package io.github.portlek.tdg.hooks;

import io.github.portlek.tdg.api.Hook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook implements Hook<Economy> {

    private static Economy economy;

    @Override
    public boolean initiate() {
        final RegisteredServiceProvider<Economy> economyProvider =
            Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return economy != null;
    }

    @NotNull
    @Override
    public Economy get() {
        if (economy == null) {
            throw new RuntimeException("Vault not initiated! Use VaultHook#initiate() method.");
        }

        return economy;
    }

}
