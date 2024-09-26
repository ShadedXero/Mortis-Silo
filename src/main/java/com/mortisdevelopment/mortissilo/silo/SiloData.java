package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.block.BlockData;
import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.data.BlockItem;
import com.mortisdevelopment.mortissilo.data.mortissilo.SiloPersistentData;
import com.mortisdevelopment.mortissilo.utils.LocationUtils;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SiloData extends SiloPersistentData {

    private final String siloBlocksKey = "silo_blocks";
    private final String inHoppersKey = "in_hoppers";
    private final String outHoppersKey = "out_hoppers";
    private final MortisSilo plugin;
    private final Sign sign;

    public SiloData(MortisSilo plugin, Sign sign) {
        super(sign.getPersistentDataContainer());
        this.plugin = plugin;
        this.sign = sign;
    }

    public Location getLocation() {
        return sign.getLocation();
    }

    public void create(List<Location> siloBlocks) {
        setSiloLocations(siloBlocks);
    }

    public boolean isInvalid() {
        return getSiloLocations().isEmpty();
    }

    public List<Location> getLocations(String key) {
        String rawLocations = getString(key);
        if (rawLocations == null) {
            return new ArrayList<>();
        }
        return LocationUtils.getLocations(rawLocations);
    }

    public void setLocations(String key, List<Location> locations) {
        setString(key, LocationUtils.getLocations(locations));
    }

    public void addLocation(String key, Location location) {
        List<Location> locations = getSiloLocations();
        locations.add(location);
        setLocations(key, locations);
    }

    public void removeLocation(String key, Location location) {
        List<Location> locations = getSiloLocations();
        if (locations.remove(location)) {
            setLocations(key, locations);
        }
    }

    public void setSiloLocations(List<Location> locations) {
        setLocations(siloBlocksKey, locations);
    }

    public List<Location> getSiloLocations() {
        return getLocations(siloBlocksKey);
    }

    public List<BlockData> getSiloBlocks() {
        return getSiloLocations().stream().map(location -> new BlockData(plugin, location.getBlock())).collect(Collectors.toList());
    }

    public void removeSiloBlock(Location siloBlock) {
        removeLocation(siloBlocksKey, siloBlock);
    }

    public void setInHoppers(List<Location> inHoppers) {
        setLocations(inHoppersKey, inHoppers);
    }

    public List<Location> getInHoppers() {
        return getLocations(inHoppersKey);
    }

    public void addInHopper(Location inHopper) {
        addLocation(inHoppersKey, inHopper);
    }

    public void removeInHopper(Location inHopper) {
        removeLocation(inHoppersKey, inHopper);
    }

    public void setOutHoppers(List<Location> outHoppers) {
        setLocations(outHoppersKey, outHoppers);
    }

    public List<Location> getOutHoppers() {
        return getLocations(outHoppersKey);
    }

    public void addOutHopper(Location outHopper) {
        addLocation(outHoppersKey, outHopper);
    }

    public void removeOutHopper(Location outHopper) {
        removeLocation(outHoppersKey, outHopper);
    }

    public boolean store(BlockManager blockManager, WeightManager weightManager, ItemStack item) {
        for (BlockData data : getSiloBlocks()) {
            if (data.canStore(blockManager, weightManager, item)) {
                data.addItem(item);
                return true;
            }
        }
        return false;
    }

    public List<BlockItem> getUniqueItems() {
        List<BlockItem> items = new ArrayList<>();
        for (BlockData siloBlockData : getSiloBlocks()) {
            for (BlockItem blockItem : siloBlockData.getItems()) {
                BlockItem found = findMatchingItem(items, blockItem);
                if (found != null) {
                    found.setAmount(found.getAmount() + blockItem.getAmount());
                } else {
                    items.add(blockItem);
                }
            }
        }
        return items;
    }

    private BlockItem findMatchingItem(List<BlockItem> items, BlockItem blockItem) {
        for (BlockItem item : items) {
            if (item.isItem(blockItem.getItem())) {
                return item;
            }
        }
        return null;
    }

    public void give(Player player, ItemStack item, int amount) {
        for (BlockData siloBlockData : getSiloBlocks()) {
            BlockItem blockItem = siloBlockData.getItem(item);
            if (blockItem == null) {
                continue;
            }
            int blockAmount = blockItem.getAmount();
            if (blockAmount < amount) {
                blockItem.give(player);
                siloBlockData.removeItem(blockItem);
                amount -= blockAmount;
            }else {
                blockItem.give(player, amount);
                if (blockAmount == amount) {
                    siloBlockData.removeItem(blockItem);
                }else {
                    siloBlockData.updateAmount(blockItem);
                }
                break;
            }
        }
    }
}