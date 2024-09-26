package com.mortisdevelopment.mortissilo.weights;

import com.mortisdevelopment.mortissilo.block.SiloBlock;
import com.mortisdevelopment.mortissilo.utils.SiloObjects;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Getter
public class WeightManager {

    private final Set<WeightItem> weightItems = new HashSet<>();

    public Weight getWeight(ItemStack item) {
        for (WeightItem weightItem : weightItems) {
            if (weightItem.isItem(item)) {
                return weightItem.getWeight();
            }
        }
        return null;
    }

    public void addWeighItem(ItemStack item, double weight) {
        weightItems.add(new WeightItem(item, new Weight(weight)));
    }

    public Weight getWeight(SiloBlock block, ItemStack item) {
        return SiloObjects.requireNonNullElse(block.getWeight(item), SiloObjects.requireNonNullElse(getWeight(item), new Weight(1)));
    }
}
