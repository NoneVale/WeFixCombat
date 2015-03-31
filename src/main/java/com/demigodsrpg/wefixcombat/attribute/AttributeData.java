package com.demigodsrpg.wefixcombat.attribute;

public class AttributeData<A extends Attribute> {
    private A attribute;
    private double data;

    public AttributeData(A attribute, double data) {
        this.attribute = attribute;
        this.data = data;
    }

    public A getAttribute() {
        return attribute;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
