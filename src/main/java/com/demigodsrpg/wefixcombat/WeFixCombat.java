package com.demigodsrpg.wefixcombat;

import org.bukkit.Bukkit;

public class WeFixCombat {
    // -- IMPORTANT STATIC FIELDS -- //

    private static final WeFixCombatPlugin PLUGIN;
    private static final WeFixCombat INST;

    static {
        PLUGIN = (WeFixCombatPlugin) Bukkit.getServer().getPluginManager().getPlugin("WeFixCombat");
        INST = new WeFixCombat();
    }

    // -- STATIC GETTERS -- //

    public static WeFixCombatPlugin getPlugin() {
        return PLUGIN;
    }

    public static WeFixCombat getInstance() {
        return INST;
    }

    // -- PRIVATE CONSTRUCTOR -- //

    private WeFixCombat() {
    }

    // -- ENABLE/DISABLE METHODS -- //

    void onEnable() {

    }

    void onDisable() {

    }
}
