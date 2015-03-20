package com.demigodsrpg.wefixcombat.attribute;

public class AttributeData<D, A extends Attribute<D>> {
    private A attribute;
    private D data;

    public AttributeData(A attribute, D data) {
        this.attribute = attribute;
        this.data = data;
    }

    public A getAttribute() {
        return attribute;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
