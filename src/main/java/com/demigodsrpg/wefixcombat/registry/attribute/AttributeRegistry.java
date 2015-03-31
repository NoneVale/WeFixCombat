package com.demigodsrpg.wefixcombat.registry.attribute;

import com.demigodsrpg.wefixcombat.attribute.Attribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.google.common.collect.Multimap;
import org.bukkit.Material;

import java.util.List;

public interface AttributeRegistry<A extends Attribute, T> {
    Multimap<Material, AttributeData<A>> getMap();

    List<AttributeData<A>> getData(T type);

    List<AttributeData<A>> getData(T[] typeArray);
}
