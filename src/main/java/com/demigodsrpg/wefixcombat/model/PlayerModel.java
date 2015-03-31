package com.demigodsrpg.wefixcombat.model;

import com.censoredsoftware.library.util.RandomUtil;
import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.Health;
import com.demigodsrpg.wefixcombat.attribute.MaterialAttribute;
import com.demigodsrpg.wefixcombat.util.JsonSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerModel extends AbstractPersistentModel<String> {
    // -- STATIC CONSTANTS -- //

    private static final long ATTACK_INTERVAL_MILLIS = 45000; // 45 seconds
    private static final boolean USE_BLOOD_HEALTH = WeFixCombat.getConfig().getBoolean("optional.blood-health", true);

    // -- META DATA -- //

    private final String MOJANG_ID;
    private String LAST_KNOWN_NAME;
    private double DEFAULT_MAX_HEALTH;
    private Map<Health, Double> HEALTH_DATA = new ConcurrentHashMap<>();

    private transient double totalHealth;
    private transient double maxHealth;

    private transient long attacking = 0;
    private transient int fatigueLevel = 0;

    private transient double mass = 1;

    private transient double easeOfMovement = 1;
    private transient double fatigueRate = 0;
    private transient double damageResistance = 0;

    // -- CONSTRUCTORS -- //

    public PlayerModel(Player player) {
        LAST_KNOWN_NAME = player.getName();
        MOJANG_ID = player.getUniqueId().toString();
        DEFAULT_MAX_HEALTH = player.getMaxHealth(); // TODO This might cause issues
        totalHealth = DEFAULT_MAX_HEALTH;
        maxHealth = DEFAULT_MAX_HEALTH;

        calculateMaxHealth(player);
        calculateCurrentHealth(player, false);
    }

    public PlayerModel(String mojangId, JsonSection json) {
        MOJANG_ID = mojangId;
        LAST_KNOWN_NAME = json.getString("last-known-name");
        DEFAULT_MAX_HEALTH = json.getDouble("default-max-health");
        totalHealth = DEFAULT_MAX_HEALTH;
        maxHealth = DEFAULT_MAX_HEALTH;
        for (Health health : Health.values()) {
            HEALTH_DATA.put(health, json.getDouble(health.name() + "-HP", DEFAULT_MAX_HEALTH));
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
        return totalHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getHealth(Health health) {
        return HEALTH_DATA.getOrDefault(health, 20.0);
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public double getMass() {
        return mass;
    }

    public double getEaseOfMovement() {
        return easeOfMovement;
    }

    public double getFatigueRate() {
        return fatigueRate;
    }

    public double getDamageResistance() {
        return damageResistance;
    }

    // -- MUTATORS -- //

    public void setDefaultMaxHealth(double maxHealth) {
        DEFAULT_MAX_HEALTH = maxHealth;
    }

    public void resetToCurrent(Player player) {
        mass = 1 + WeFixCombat.getMaterialRegistry().getData(MaterialAttribute.MASS, player.getItemInHand());
        mass += WeFixCombat.getMaterialRegistry().getData(MaterialAttribute.MASS, player.getInventory().getArmorContents());

        easeOfMovement = WeFixCombat.getArmorRegistry().getData(ArmorAttribute.EASE_OF_MOVEMENT, player.getInventory().getArmorContents());
        damageResistance = WeFixCombat.getArmorRegistry().getData(ArmorAttribute.DAMAGE_RESISTENCE, player.getInventory().getArmorContents());

        calculateMaxHealth(player);
        calculateCurrentHealth(player, false);
    }

    public double damage(Player player, double damage, double armor, double enchant, double block) {
        // Recalculate the damage
        // damage -= armor * damageResistance;
        // damage -= enchant * damageResistance;
        // damage -= block * damageResistance; // TODO This should deal with weapons not armor

        // Split the damage up and add to respective spots
        damage /= 6;
        HEALTH_DATA.put(Health.HEAD, getHealth(Health.HEAD) - damage * 2);
        HEALTH_DATA.put(Health.CHEST, getHealth(Health.HEAD) - damage * 2);
        HEALTH_DATA.put(Health.LEGS, getHealth(Health.HEAD) - damage);
        HEALTH_DATA.put(Health.FEET, getHealth(Health.HEAD) - damage);

        // Add blood damage
        HEALTH_DATA.put(Health.BLOOD, getHealth(Health.BLOOD) + RandomUtil.generateDoubleRange(0.0, 0.15)); // TODO Balance this

        // Calculate the current health
        calculateMaxHealth(player);
        return player.getHealth() - calculateCurrentHealth(player, false);
    }

    public double calculateCurrentHealth(Player player, boolean fresh) {
        // Get all of the health data
        double head = HEALTH_DATA.getOrDefault(Health.HEAD, DEFAULT_MAX_HEALTH);
        double chest = HEALTH_DATA.getOrDefault(Health.CHEST, DEFAULT_MAX_HEALTH);
        double legs = HEALTH_DATA.getOrDefault(Health.LEGS, DEFAULT_MAX_HEALTH);
        double feet = HEALTH_DATA.getOrDefault(Health.FEET, DEFAULT_MAX_HEALTH);

        // Calculate the mean total
        double damage = (head * chest * legs * feet) / 6;
        double total = player.getHealth() - damage;

        // Save the fraction
        if (total > maxHealth) {
            totalHealth = maxHealth;
        } else {
            totalHealth = total;
        }

        // Set the health
        if (fresh) {
            player.damage(damage);
        }

        return totalHealth;
    }

    public void calculateMaxHealth(Player player) {
        // Account for blood damage
        if (USE_BLOOD_HEALTH) {
            double blood = HEALTH_DATA.getOrDefault(Health.BLOOD, 0.0);
            maxHealth = DEFAULT_MAX_HEALTH - blood;
            if (maxHealth < 0) {
                player.setMaxHealth(0.1);
                player.damage(1);
                return;
            }
            if (player.getHealth() > maxHealth) {
                player.setHealth(maxHealth);
            }
            player.setMaxHealth(maxHealth);
        } else {
            maxHealth = player.getMaxHealth();
        }
    }

    public void resetHealth(Player player) {
        for (Health health : Health.values()) {
            HEALTH_DATA.put(health, DEFAULT_MAX_HEALTH);
        }
        HEALTH_DATA.put(Health.BLOOD, 0.0);
        totalHealth = DEFAULT_MAX_HEALTH;
        player.setHealth(DEFAULT_MAX_HEALTH);
    }

    public void resetMaxHealth(Player player) {
        HEALTH_DATA.put(Health.BLOOD, 0.0);
        maxHealth = DEFAULT_MAX_HEALTH;
        player.setMaxHealth(DEFAULT_MAX_HEALTH);
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
        map.put("last-known-name", LAST_KNOWN_NAME);
        map.put("default-max-health", DEFAULT_MAX_HEALTH);
        for (Map.Entry<Health, Double> entry : HEALTH_DATA.entrySet()) {
            map.put(entry.getKey().name() + "-HP", entry.getValue());
        }
        return map;
    }
}
