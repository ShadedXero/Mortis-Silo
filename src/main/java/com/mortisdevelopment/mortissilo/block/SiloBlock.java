package com.mortisdevelopment.mortissilo.block;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class SiloBlock {

    private final String id;
    private final Material material;
    private final ItemStack item;
    private final double storage;
    private final boolean combinable;
    private final Map<Material, Double> weightByMaterial;
    private final SiloBlockMode mode;
    private final List<ItemStack> modeList;

    public SiloBlock(String id, Material material, ItemStack item, double storage, boolean combinable, Map<Material, Double> weightByMaterial, SiloBlockMode mode, List<ItemStack> modeList) {
        this.id = id;
        this.material = material;
        this.item = item;
        this.storage = storage;
        this.combinable = combinable;
        this.weightByMaterial = weightByMaterial;
        this.mode = mode;
        this.modeList = modeList;
    }

    public void place(MortisSilo plugin, Block block) {
        block.setType(material);
        new SiloBlockData(plugin, block).create(id);
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

    public void mine(MortisSilo plugin, Player player, Block block) {
        block.setType(Material.AIR);
        SiloBlockData data = new SiloBlockData(plugin, block);
        data.dumpItems(block.getLocation());
        data.clear();
        ItemUtils.give(player, getItem(), true);
    }
}