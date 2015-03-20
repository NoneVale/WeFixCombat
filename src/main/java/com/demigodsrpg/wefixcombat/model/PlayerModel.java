package com.demigodsrpg.wefixcombat.model;

import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.demigodsrpg.wefixcombat.attribute.WeaponAttribute;
import com.demigodsrpg.wefixcombat.health.HealthData;
import com.demigodsrpg.wefixcombat.util.JsonSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerModel extends AbstractPersistentModel<String> {
    // -- STATIC CONSTANTS -- //

    private static final long ATTACK_INTERVAL_MILLIS = 45000; // 45 seconds

    // -- META DATA -- //

    private final String MOJANG_ID;
    private String LAST_KNOWN_NAME;
    private List<HealthData> HEALTH_DATA;

    private transient List<AttributeData<Double, WeaponAttribute>> WEAPON_DATA;
    private transient List<AttributeData<Integer, ArmorAttribute>> ARMOR_DATA;

    private transient long attacking = 0;
    private transient double maxHealth = 20.0;

    // -- CONSTRUCTORS -- //

    public PlayerModel(Player player) {
        LAST_KNOWN_NAME = player.getName();
        MOJANG_ID = player.getUniqueId().toString();
        WEAPON_DATA = WeFixCombat.getWeaponRegistry().getData(player.getItemInHand());
        ARMOR_DATA = WeFixCombat.getArmorRegistry().getData(player.getInventory().getArmorContents());
    }

    public PlayerModel(String mojangId, JsonSection json) {
        MOJANG_ID = mojangId;
        LAST_KNOWN_NAME = json.getString("last-known-name");
        // TODO Health data
    }

    // -- GETTERS -- //

    public String getMojangId() {
        return MOJANG_ID;
    }

    public String getLastKnownName() {
        return LAST_KNOWN_NAME;
    }

    public List<HealthData> getHealthData() {
        return HEALTH_DATA;
    }

    public List<AttributeData<Double, WeaponAttribute>> getWeaponData() {
        return WEAPON_DATA;
    }

    public List<AttributeData<Integer, ArmorAttribute>> getArmorData() {
        return ARMOR_DATA;
    }

    public boolean isAttacking() {
        return System.currentTimeMillis() - attacking <= ATTACK_INTERVAL_MILLIS;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    // -- MUTATORS -- //

    public void resetToCurrent(Player player) {
        WEAPON_DATA = WeFixCombat.getWeaponRegistry().getData(player.getItemInHand());
        ARMOR_DATA = WeFixCombat.getArmorRegistry().getData(player.getInventory().getArmorContents());
    }

    public void calculateMaxHealth() {
        // TODO
    }

    public void setAttacking() {
        this.attacking = System.currentTimeMillis();
    }

    @Override
    public String getPersistentId() {
        return MOJANG_ID;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(LAST_KNOWN_NAME, "last-known-name");
        // TODO Health data
        return map;
    }
}
