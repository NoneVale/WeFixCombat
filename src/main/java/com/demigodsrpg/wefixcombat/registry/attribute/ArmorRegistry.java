package com.demigodsrpg.wefixcombat.registry.attribute;

import com.demigodsrpg.wefixcombat.attribute.ArmorAttribute;
import com.demigodsrpg.wefixcombat.attribute.Attribute;
import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArmorRegistry implements AttributeRegistry<ArmorAttribute, ItemStack> {
    private static final ListMultimap<Material, AttributeData<ArmorAttribute>> MAP_DATA = Multimaps.newListMultimap(new ConcurrentHashMap<>(), ArrayList::new);

    @Override
    public Multimap<Material, AttributeData<ArmorAttribute>> getMap() {
        return MAP_DATA;
    }

    @Override
    public List<AttributeData<ArmorAttribute>> getData(ItemStack item) throws UnsupportedOperationException {
        Material type = item == null ? Material.AIR : item.getType();
        if (MAP_DATA.containsKey(type)) {
            return MAP_DATA.get(type);
        }
        throw new UnsupportedOperationException("Can only return armor data from the armor registry.");
    }

    @Override
    public List<AttributeData<ArmorAttribute>> getData(ItemStack[] items) {
        // Make a map for sorting
        Map<Attribute, AttributeData<ArmorAttribute>> map = new HashMap<>();

        // Iterate over each item
        for (ItemStack item : items) {
            // Iterate over the data for each item
            for (AttributeData<ArmorAttribute> data : getData(item)) {
                Attribute attribute = data.getAttribute();
                // See if the data is already contained in the map
                if (map.containsKey(data.getAttribute())) {
                    // Get the found data
                    AttributeData<ArmorAttribute> found = map.get(attribute);

                    // Set the new data
                    data.setData(found.getData() + data.getData());
                }

                // Add the new data to the map
                map.put(attribute, data);
            }
        }

        // Return the collection of all data
        return new ArrayList<>(map.values());
    }

    public double getData(ArmorAttribute attribute, Material type) {
        double data = 0;
        if (MAP_DATA.containsKey(type)) {
            for (AttributeData<ArmorAttribute> aData : MAP_DATA.get(type)) {
                if (attribute.equals(aData.getAttribute())) {
                    data += aData.getData();
                    break;
                }
            }
        }
        return data;
    }

    public double getData(ArmorAttribute attribute, Material[] types) {
        double data = 0;
        for (Material type : types) {
            data += getData(attribute, type);
        }
        return data;
    }

    public double getData(ArmorAttribute attribute, ItemStack item) {
        return getData(attribute, item.getType());
    }

    public double getData(ArmorAttribute attribute, ItemStack[] items) {
        double data = 0;
        for (ItemStack item : items) {
            if (item != null) {
                data += getData(attribute, item.getType());
            }
        }
        return data;
    }
}