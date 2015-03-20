package com.demigodsrpg.wefixcombat.player;

import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerData {
    // -- META DATA -- //

    private final String LAST_KNOWN_NAME;
    private final String MOJANG_ID;
    private List<AttributeData<Integer, ArmorAttribute>> ARMOR_DATA;

    private transient boolean attacking = false;

    // -- CONSTRUCTOR -- //

    public PlayerData(Player player) {
        LAST_KNOWN_NAME = player.getName();
        MOJANG_ID = player.getUniqueId().toString();
        ARMOR_DATA = WeFixCombat.getArmorRegistry().getData(player.getInventory().getArmorContents());
    }

    // -- GETTERS -- //

    public String getLastKnownName() {
        return LAST_KNOWN_NAME;
    }

    public String getMojangId() {
        return MOJANG_ID;
    }

    public List<AttributeData<Integer, ArmorAttribute>> getArmorData() {
        return ARMOR_DATA;
    }

    public boolean isAttacking() {
        return attacking;
    }

    // -- MUTATORS -- //

    public void resetCurrentArmorData(Player player) {
        ARMOR_DATA = WeFixCombat.getArmorRegistry().getData(player.getInventory().getArmorContents());
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
}
