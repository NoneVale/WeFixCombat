package com.demigodsrpg.wefixcombat.health;

public class HealthData {
    private Health health;
    private double data;

    public HealthData(Health health, double data) {
        this.health = health;
        this.data = data;
    }

    public Health getHealth() {
        return health;
    }

    public double getData() {
        return data;
    }
}
