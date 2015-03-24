package com.demigodsrpg.wefixcombat.model;

import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.MaterialAttribute;
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

    private transient long attacking = 0;
    private transient double maxHealth = 20.0;
    private transient int fatigueLevel = 0;

    private transient double mass = 1;

    private transient int easeOfMovement = 1;
    private transient int fatigueRate = 0;
    private transient int damageResistence = 0;

    // -- CONSTRUCTORS -- //

    public PlayerModel(Player player) {
        LAST_KNOWN_NAME = player.getName();
        MOJANG_ID = player.getUniqueId().toString();
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

    public boolean isAttacking() {
        return System.currentTimeMillis() - attacking <= ATTACK_INTERVAL_MILLIS;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public double getMass() {
        return mass;
    }

    public int getEaseOfMovement() {
        return easeOfMovement;
    }

    public int getFatigueRate() {
        return fatigueRate;
    }

    public int getDamageResistence() {
        return damageResistence;
    }

    // -- MUTATORS -- //

    public void resetToCurrent(Player player) {
        mass = 1 + WeFixCombat.getMaterialRegistry().getData(MaterialAttribute.MASS, player.getItemInHand());
        mass += WeFixCombat.getMaterialRegistry().getData(MaterialAttribute.MASS, player.getInventory().getArmorContents());

        easeOfMovement = WeFixCombat.getArmorRegistry().getData(ArmorAttribute.EASE_OF_MOVEMENT, player.getInventory().getArmorContents());
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
