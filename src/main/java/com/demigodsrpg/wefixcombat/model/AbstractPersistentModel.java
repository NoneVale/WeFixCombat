package com.demigodsrpg.wefixcombat.model;

import java.util.Map;

public abstract class AbstractPersistentModel<P> {
    public abstract P getPersistentId();

    public abstract Map<String, Object> serialize();
}
