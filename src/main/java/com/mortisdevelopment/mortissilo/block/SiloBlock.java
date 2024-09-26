package com.mortisdevelopment.mortissilo.block;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.silo.SiloData;
import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter @Setter
public class SiloBlock extends WeightManager {

    private final String id;
    private final Material material;
    private final ItemStack item;
    private final double storage;
    private final boolean combinable;
    private final BlockMode mode;
    private final List<ItemStack> modeList;

    public SiloBlock(String id, Material material, ItemStack item, double storage, boolean combinable, BlockMode mode, List<ItemStack> modeList) {
        this.id = id;
        this.material = material;
        this.item = item;
        this.storage = storage;
        this.combinable = combinable;
        this.mode = mode;
        this.modeList = modeList;
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

    public void mine(MortisSilo plugin, Block block) {
        block.setType(Material.AIR);
        BlockData data = new BlockData(plugin, block);
        data.dumpItems(block.getLocation());
        if (data.isTerminalConnected()) {
            SiloData siloData = data.getTerminal();
            siloData.removeSiloBlock(block.getLocation());
        }
        data.clear();
        ItemUtils.drop(block.getLocation(), getItem());
    }
}