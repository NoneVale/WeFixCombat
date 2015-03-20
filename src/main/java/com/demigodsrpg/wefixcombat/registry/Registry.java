package com.demigodsrpg.wefixcombat.registry;

import com.demigodsrpg.wefixcombat.attribute.Attribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.google.common.collect.Multimap;
import org.bukkit.Material;

import java.util.Collection;

public interface Registry<D, A extends Attribute<D>, T> {
    Multimap<Material, AttributeData<D, A>> getMap();

    Collection<AttributeData<D, A>> getData(T type);

    Collection<AttributeData<D, A>> getData(T[] typeArray);
}
