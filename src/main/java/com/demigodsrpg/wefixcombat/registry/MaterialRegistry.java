package com.demigodsrpg.wefixcombat.registry;

import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.demigodsrpg.wefixcombat.attribute.MaterialAttribute;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialRegistry implements Registry<Double, MaterialAttribute, Material> {
    private static final ListMultimap<Material, AttributeData<Double, MaterialAttribute>> MAP_DATA = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);

    @Override
    public Multimap<Material, AttributeData<Double, MaterialAttribute>> getMap() {
        return MAP_DATA;
    }

    @Override
    public List<AttributeData<Double, MaterialAttribute>> getData(Material type) throws UnsupportedOperationException {
        if (MAP_DATA.containsKey(type)) {
            return MAP_DATA.get(type);
        }
        throw new UnsupportedOperationException("Can only return material data from the material registry.");
    }

    @Override
    public List<AttributeData<Double, MaterialAttribute>> getData(Material[] typeArray) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Materials do not stack attributes.");
    }

    static {
        // -- WOOD -- //
        registerMaterial(Material.WOOD, MaterialAttribute.MASS, 0.5);
    }

    private static void registerMaterial(Material material, MaterialAttribute attribute, double data) {
        MAP_DATA.put(material, new AttributeData<>(attribute, data));
    }
}
