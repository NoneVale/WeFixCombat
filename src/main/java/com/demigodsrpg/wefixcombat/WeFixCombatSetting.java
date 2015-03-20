package com.demigodsrpg.wefixcombat;

public class WeFixCombatSetting {
    public static final boolean SAVE_PRETTY;

    static {
        SAVE_PRETTY = WeFixCombat.getConfig().getBoolean("save.pretty", false);
    }
}
