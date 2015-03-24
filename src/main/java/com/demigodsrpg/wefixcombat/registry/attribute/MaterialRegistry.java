package com.demigodsrpg.wefixcombat.registry.attribute;

import com.demigodsrpg.wefixcombat.attribute.AttributeData;
import com.demigodsrpg.wefixcombat.attribute.MaterialAttribute;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialRegistry implements AttributeRegistry<Double, MaterialAttribute, Material> {
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

    public double getData(MaterialAttribute attribute, Material type) {
        double data = 0;
        if (MAP_DATA.containsKey(type)) {
            for (AttributeData<Double, MaterialAttribute> mData : MAP_DATA.get(type)) {
                if (attribute.equals(mData.getAttribute())) {
                    data += mData.getData();
                    break;
                }
            }
        }
        return data;
    }

    public double getData(MaterialAttribute attribute, Material[] types) {
        double data = 0;
        for (Material type : types) {
            data += getData(attribute, type);
        }
        return data;
    }

    public double getData(MaterialAttribute attribute, ItemStack item) {
        return getData(attribute, item.getType());
    }

    public double getData(MaterialAttribute attribute, ItemStack[] items) {
        double data = 0;
        for (ItemStack item : items) {
            if (item != null) {
                data += getData(attribute, item.getType());
            }
        }
        return data;
    }

    @Override
    public List<AttributeData<Double, MaterialAttribute>> getData(Material[] typeArray) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Materials do not stack attributes.");
    }

    static {
        // -- AIR -- //
        register(Material.AIR, MaterialAttribute.MASS, 0);
        register(Material.AIR, MaterialAttribute.TENSILE_STRENGTH, 10);
        register(Material.AIR, MaterialAttribute.DURABILITY, 10);

        // -- LEATHER -- //
        register(Material.LEATHER, MaterialAttribute.MASS, 0.5);
        register(Material.LEATHER, MaterialAttribute.TENSILE_STRENGTH, 0.5);
        register(Material.LEATHER, MaterialAttribute.DURABILITY, 6);

        // -- WOOD -- //
        register(Material.WOOD, MaterialAttribute.MASS, 1);
        register(Material.WOOD, MaterialAttribute.TENSILE_STRENGTH, 1);
        register(Material.WOOD, MaterialAttribute.DURABILITY, 1);

        // -- STONE -- //
        register(Material.STONE, MaterialAttribute.MASS, 2);
        register(Material.STONE, MaterialAttribute.TENSILE_STRENGTH, 4);
        register(Material.STONE, MaterialAttribute.DURABILITY, 8);

        // -- GOLD (GOLD_BLOCK) -- //
        register(Material.GOLD_BLOCK, MaterialAttribute.MASS, 5);
        register(Material.GOLD_BLOCK, MaterialAttribute.TENSILE_STRENGTH, 2);
        register(Material.GOLD_BLOCK, MaterialAttribute.DURABILITY, 10);

        // -- CHAINMAIL (CHAINMAIL_HELMET) -- //
        register(Material.CHAINMAIL_HELMET, MaterialAttribute.MASS, 5);
        register(Material.CHAINMAIL_HELMET, MaterialAttribute.TENSILE_STRENGTH, 9);
        register(Material.CHAINMAIL_HELMET, MaterialAttribute.DURABILITY, 4);

        // -- IRON (IRON_BLOCK) -- //
        register(Material.IRON_BLOCK, MaterialAttribute.MASS, 7);
        register(Material.IRON_BLOCK, MaterialAttribute.TENSILE_STRENGTH, 8);
        register(Material.IRON_BLOCK, MaterialAttribute.DURABILITY, 6);

        // -- DIAMOND -- //
        register(Material.DIAMOND, MaterialAttribute.MASS, 10);
        register(Material.DIAMOND, MaterialAttribute.TENSILE_STRENGTH, 10);
        register(Material.DIAMOND, MaterialAttribute.DURABILITY, 10);
    }

    private static void register(Material material, MaterialAttribute attribute, double data) {
        MAP_DATA.put(material, new AttributeData<>(attribute, data));
    }
}
