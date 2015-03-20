package com.demigodsrpg.wefixcombat;

import org.bukkit.plugin.java.JavaPlugin;

public class WeFixCombatPlugin extends JavaPlugin {
    // -- MINIMAL PLUGIN OBJECT -- //

    @Override
    public void onEnable() {
        WeFixCombat.getInstance().onEnable();
    }

    @Override
    public void onDisable() {
        WeFixCombat.getInstance().onDisable();
    }
}
