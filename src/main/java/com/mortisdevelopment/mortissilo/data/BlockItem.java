package com.mortisdevelopment.mortissilo.data;

import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class BlockItem {

    private final int slot;
    private final ItemStack item;
    private int amount;

    public BlockItem(int slot, ItemStack item, int amount) {
        this.slot = slot;
        this.item = item.clone();
        this.item.setAmount(1);
        this.amount = amount;
    }

    public boolean isItem(ItemStack item) {
        return this.item.isSimilar(item);
    }

    public void give(Player player) {
        for (int i = 0; i < amount; i++) {
            ItemUtils.give(player, getItem());
        }
    }

    public void give(Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            ItemUtils.give(player, getItem());
        }
    }

    public ItemStack getItem() {
        return item.clone();
    }
}
