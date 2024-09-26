package com.mortisdevelopment.mortissilo.weights;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class WeightItem {

    private final ItemStack item;
    private final Weight weight;

    public WeightItem(ItemStack item, Weight weight) {
        this.item = item;
        this.weight = weight;
    }

    public boolean isItem(ItemStack item) {
        return this.item.isSimilar(item);
    }
}
