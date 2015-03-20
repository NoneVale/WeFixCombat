package com.demigodsrpg.wefixcombat.registry.persistent;

import com.demigodsrpg.wefixcombat.model.PlayerModel;
import com.demigodsrpg.wefixcombat.util.JsonSection;

public class PlayerRegistry extends AbstractPersistentRegistry<PlayerModel> {
    @Override
    protected PlayerModel valueFromData(String stringKey, JsonSection data) {
        return new PlayerModel();
    }

    @Override
    protected String getFileName() {
        return "players.wfcdat";
    }
}
