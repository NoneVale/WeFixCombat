package com.demigodsrpg.wefixcombat;

import com.demigodsrpg.wefixcombat.registry.attribute.ArmorRegistry;
import com.demigodsrpg.wefixcombat.registry.attribute.MaterialRegistry;
import com.demigodsrpg.wefixcombat.registry.attribute.WeaponRegistry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class WeFixCombat {
    // -- IMPORTANT STATIC FIELDS -- //

    private static final WeFixCombat INST;
    private static final WeFixCombatPlugin PLUGIN;
    private static final FileConfiguration CONFIG;
    private static final Logger LOGGER;

    private static final String DATA_PATH;

    // -- REGISTRIES -- //

    private static final ArmorRegistry ARMOR_REGISTRY;
    private static final MaterialRegistry MATERIAL_REGISTRY;
    private static final WeaponRegistry WEAPON_REGISTRY;

    static {
        INST = new WeFixCombat();
        PLUGIN = (WeFixCombatPlugin) Bukkit.getServer().getPluginManager().getPlugin("WeFixCombat");
        CONFIG = PLUGIN.getConfig();
        LOGGER = PLUGIN.getLogger();

        DATA_PATH = PLUGIN.getDataFolder().getPath() + "/";

        ARMOR_REGISTRY = new ArmorRegistry();
        MATERIAL_REGISTRY = new MaterialRegistry();
        WEAPON_REGISTRY = new WeaponRegistry();
    }

    // -- STATIC GETTERS -- //

    public static WeFixCombat getInstance() {
        return INST;
    }

    public static WeFixCombatPlugin getPlugin() {
        return PLUGIN;
    }

    public static FileConfiguration getConfig() {
        return CONFIG;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static String getDataPath() {
        return DATA_PATH;
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
