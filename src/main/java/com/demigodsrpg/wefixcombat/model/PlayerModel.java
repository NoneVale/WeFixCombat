package com.demigodsrpg.wefixcombat.model;

import com.censoredsoftware.library.util.RandomUtil;
import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.Health;
import com.demigodsrpg.wefixcombat.attribute.MaterialAttribute;
import com.demigodsrpg.wefixcombat.util.JsonSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerModel extends AbstractPersistentModel<String> {
    // -- STATIC CONSTANTS -- //

    private static final long ATTACK_INTERVAL_MILLIS = 45000; // 45 seconds
    private static final boolean USE_BLOOD_HEALTH = WeFixCombat.getConfig().getBoolean("optional.blood-health", true);

    // -- META DATA -- //

    private final String MOJANG_ID;
    private String LAST_KNOWN_NAME;
    private Map<Health, Double> HEALTH_DATA;

    private transient long attacking = 0;
    private transient double totalHealthFraction = 1.0;
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

        calculateMaxHealth(player);
        calculateCurrentHealth(player, false);
    }

    public PlayerModel(String mojangId, JsonSection json) {
        MOJANG_ID = mojangId;
        LAST_KNOWN_NAME = json.getString("last-known-name");
        for (Health health : Health.values()) {
            HEALTH_DATA.put(health, json.getDouble(health.name() + "-HP"));
        }
    }

    // -- GETTERS -- //

    public String getMojangId() {
        return MOJANG_ID;
    }

    public String getLastKnownName() {
        return LAST_KNOWN_NAME;
    }

    public Map<Health, Double> getHealthData() {
        return HEALTH_DATA;
    }

    public boolean isAttacking() {
        return System.currentTimeMillis() - attacking <= ATTACK_INTERVAL_MILLIS;
    }

    public double getTotalHealth() {
        return maxHealth * totalHealthFraction;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHelath(Health health) {
        return HEALTH_DATA.getOrDefault(health, 20.0);
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
        damageResistence = WeFixCombat.getArmorRegistry().getData(ArmorAttribute.DAMAGE_RESISTENCE, player.getInventory().getArmorContents());

        calculateMaxHealth(player);
        calculateCurrentHealth(player, false);
    }

    public void damage(Player player, double damage, boolean fresh) {
        damage -= damageResistence; // TODO This might be the wrong place to handle this, also it needs to be balanced
        damage /= 6;
        HEALTH_DATA.put(Health.HEAD, getHelath(Health.HEAD) - damage * 2);
        HEALTH_DATA.put(Health.CHEST, getHelath(Health.HEAD) - damage * 2);
        HEALTH_DATA.put(Health.LEGS, getHelath(Health.HEAD) - damage);
        HEALTH_DATA.put(Health.FEET, getHelath(Health.HEAD) - damage);
        HEALTH_DATA.put(Health.BLOOD, getHelath(Health.BLOOD) + RandomUtil.generateDoubleRange(0.0, 0.25)); // TODO Balance this
        calculateCurrentHealth(player, fresh);
    }

    public void calculateCurrentHealth(Player player, boolean damage) {
        // Get all of the health data
        double head = HEALTH_DATA.get(Health.HEAD);
        double chest = HEALTH_DATA.get(Health.CHEST);
        double legs = HEALTH_DATA.get(Health.LEGS);
        double feet = HEALTH_DATA.get(Health.FEET);

        // Calculate the mean total
        double total = (head * head * chest * chest * legs * feet) / 6;

        // Save the fraction
        totalHealthFraction = total / maxHealth;

        // Set the health
        if (damage) {
            player.damage(player.getHealth() - total);
        } else {
            player.setHealth(getTotalHealth());
        }

        // If it is less than one, count it as zero
        if (totalHealthFraction < 1) {
            totalHealthFraction = 0;
            if (damage) {
                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 1.0));
                player.damage(1.0);
            }
        }
    }

    public void calculateMaxHealth(Player player) {
        // Account for blood damage
        if (USE_BLOOD_HEALTH) {
            double blood = HEALTH_DATA.get(Health.BLOOD);
            maxHealth = 20.0 - blood;
            if (maxHealth < 1) {
                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 1.0));
                player.damage(1.0);
            } else {
                player.setMaxHealth(maxHealth);
            }
        } else {
            maxHealth = player.getMaxHealth();
        }
    }

    public void resetHealth(Player player) {
        for (Health health : Health.values()) {
            HEALTH_DATA.put(health, 20.0);
        }
        HEALTH_DATA.put(Health.BLOOD, 0.0);
        totalHealthFraction = player.getHealth() / maxHealth;
        player.setHealth(20.0);
    }

    public void resetMaxHealth(Player player) {
        HEALTH_DATA.put(Health.BLOOD, 0.0);
        maxHealth = 20.0;
        player.setMaxHealth(20.0);
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
        for (Map.Entry<Health, Double> entry : HEALTH_DATA.entrySet()) {
            map.put(entry.getKey().name() + "-HP", entry.getValue());
        }
        return map;
    }
}
