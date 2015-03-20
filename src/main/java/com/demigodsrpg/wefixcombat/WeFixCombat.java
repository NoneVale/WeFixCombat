package com.demigodsrpg.wefixcombat;

import com.demigodsrpg.wefixcombat.registry.ArmorRegistry;
import com.demigodsrpg.wefixcombat.registry.MaterialRegistry;
import com.demigodsrpg.wefixcombat.registry.WeaponRegistry;
import org.bukkit.Bukkit;

public class WeFixCombat {
    // -- IMPORTANT STATIC FIELDS -- //

    private static final WeFixCombatPlugin PLUGIN;
    private static final WeFixCombat INST;

    // -- REGISTRIES -- //

    private static final ArmorRegistry ARMOR_REGISTRY;
    private static final MaterialRegistry MATERIAL_REGISTRY;
    private static final WeaponRegistry WEAPON_REGISTRY;

    static {
        PLUGIN = (WeFixCombatPlugin) Bukkit.getServer().getPluginManager().getPlugin("WeFixCombat");
        INST = new WeFixCombat();

        ARMOR_REGISTRY = new ArmorRegistry();
        MATERIAL_REGISTRY = new MaterialRegistry();
        WEAPON_REGISTRY = new WeaponRegistry();
    }

    // -- STATIC GETTERS -- //

    public static WeFixCombatPlugin getPlugin() {
        return PLUGIN;
    }

    public static WeFixCombat getInstance() {
        return INST;
    }

    public static ArmorRegistry getArmorRegistry() {
        return ARMOR_REGISTRY;
    }

    public static MaterialRegistry getMaterialRegistry() {
        return MATERIAL_REGISTRY;
    }

    public static WeaponRegistry getWeaponRegistry() {
        return WEAPON_REGISTRY;
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
