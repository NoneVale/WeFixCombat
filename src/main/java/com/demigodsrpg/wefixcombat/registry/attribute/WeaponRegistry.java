package com.demigodsrpg.wefixcombat.registry.attribute;

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

public class WeaponRegistry implements AttributeRegistry<WeaponAttribute, ItemStack> {
    private static final ListMultimap<Material, AttributeData<WeaponAttribute>> MAP_DATA = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);

    @Override
    public Multimap<Material, AttributeData<WeaponAttribute>> getMap() {
        return MAP_DATA;
    }

    @Override
    public List<AttributeData<WeaponAttribute>> getData(ItemStack item) {
        Material type = item == null ? Material.AIR : item.getType();
        if (MAP_DATA.containsKey(type)) {
            return MAP_DATA.get(type);
        }
        // FIXME Anything that isn't a weapon is treated as air.
        return getData(new ItemStack(Material.AIR));
    }

    @Override
    public List<AttributeData<WeaponAttribute>> getData(ItemStack[] typeArray) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Weapons do not stack attributes.");
    }
}
