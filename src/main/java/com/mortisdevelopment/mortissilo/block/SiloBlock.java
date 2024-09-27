package com.mortisdevelopment.mortissilo.block;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter @Setter
public class SiloBlock extends WeightManager {

    private final String id;
    private final Material material;
    private final ItemStack item;
    private final double storage;
    private final BlockMode mode;
    private final List<ItemStack> modeList;

    public SiloBlock(String id, Material material, ItemStack item, double storage, BlockMode mode, List<ItemStack> modeList) {
        this.id = id;
        this.material = material;
        this.item = item;
        this.storage = storage;
        this.mode = mode;
        this.modeList = modeList;
    }

    public void give(Player player) {
        ItemUtils.give(player, getItem());
    }

    private boolean isModeItem(ItemStack item) {
        for (ItemStack modeItem : modeList) {
            if (modeItem.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean canStore(ItemStack item) {
        return switch (mode) {
            case NONE -> true;
            case BLACKLIST -> !isModeItem(item);
            case WHITELIST -> isModeItem(item);
        };
    }

    public void place(MortisSilo plugin, Block block) {
        block.setType(material);
        new BlockData(plugin, block).create(id);
    }

    public void place(MortisSilo plugin, Location location) {
        place(plugin, location.getBlock());
    }

    public boolean isItem(ItemStack item) {
        return this.item.isSimilar(item);
    }

    public ItemStack getItem() {
        return item.clone();
    }
}