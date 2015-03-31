package com.demigodsrpg.wefixcombat.registry.persistent;

import com.demigodsrpg.wefixcombat.model.PlayerModel;
import com.demigodsrpg.wefixcombat.util.JsonSection;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerRegistry extends AbstractPersistentRegistry<PlayerModel> {
    @Override
    protected PlayerModel valueFromData(String stringKey, JsonSection data) {
        return new PlayerModel(stringKey, data);
    }

    @Override
    protected String getFileName() {
        return "players.wfcdat";
    }

    public PlayerModel fromPlayer(Player player) {
        Optional<PlayerModel> maybeModel = getRegistered().stream().
                filter(model -> model.getMojangId().equals(player.getUniqueId().toString())).findAny();
        if (maybeModel.isPresent()) {
            return maybeModel.get();
        }
        PlayerModel model = new PlayerModel(player);
        register(model);
        return model;
    }
}
