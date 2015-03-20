package com.demigodsrpg.wefixcombat.registry;

import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.demigodsrpg.wefixcombat.attribute.WeaponAttribute;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WeaponRegistry implements Registry<Double, WeaponAttribute, ItemStack> {
    private static final ListMultimap<Material, AttributeData<Double, WeaponAttribute>> MAP_DATA = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);

    @Override
    public Multimap<Material, AttributeData<Double, WeaponAttribute>> getMap() {
        return MAP_DATA;
    }

    @Override
    public List<AttributeData<Double, WeaponAttribute>> getData(ItemStack type) throws UnsupportedOperationException {
        if (MAP_DATA.containsKey(type.getType())) {
            return MAP_DATA.get(type.getType());
        }
        throw new UnsupportedOperationException("Can only return weapon data from the weapon registry.");
    }

    @Override
    public List<AttributeData<Double, WeaponAttribute>> getData(ItemStack[] typeArray) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Weapons do not stack attributes.");
    }
}
