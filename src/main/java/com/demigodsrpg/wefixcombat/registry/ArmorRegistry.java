package com.demigodsrpg.wefixcombat.registry;

import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.Attribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ArmorRegistry implements Registry<Integer, ArmorAttribute, ItemStack> {
    private static final ListMultimap<Material, AttributeData<Integer, ArmorAttribute>> MAP_DATA = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);

    @Override
    public Multimap<Material, AttributeData<Integer, ArmorAttribute>> getMap() {
        return MAP_DATA;
    }

    @Override
    public List<AttributeData<Integer, ArmorAttribute>> getData(ItemStack type) throws UnsupportedOperationException {
        if (MAP_DATA.containsKey(type.getType())) {
            return MAP_DATA.get(type.getType());
        }
        throw new UnsupportedOperationException("Can only return armor data from the armor registry.");
    }

    @Override
    public Collection<AttributeData<Integer, ArmorAttribute>> getData(ItemStack[] items) {
        // Make a map for sorting
        Map<Attribute, AttributeData<Integer, ArmorAttribute>> map = new HashMap<>();

        // Iterate over each item
        for (ItemStack item : items) {
            // Iterate over the data for each item
            for (AttributeData<Integer, ArmorAttribute> data : getData(item)) {
                Attribute attribute = data.getAttribute();
                // See if the data is already contained in the map
                if (map.containsKey(data.getAttribute())) {
                    // Get the found data
                    AttributeData<Integer, ArmorAttribute> found = map.get(attribute);

                    // Set the new data
                    data.setData(found.getData() + data.getData());
                }

                // Add the new data to the map
                map.put(attribute, data);
            }
        }

        // Return the collection of all data
        return map.values();
    }
}