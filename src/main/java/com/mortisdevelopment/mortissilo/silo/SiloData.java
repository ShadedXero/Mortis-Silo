package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.block.SiloBlockData;
import com.mortisdevelopment.mortissilo.data.mortissilo.SiloPersistentData;
import com.mortisdevelopment.mortissilo.utils.LocationUtils;
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

    public List<SiloBlockData> getSiloBlocks() {
        return getSiloLocations().stream().map(location -> new SiloBlockData(plugin, location.getBlock())).collect(Collectors.toList());
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

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (SiloBlockData siloBlockData : getSiloBlocks()) {
            items.addAll(siloBlockData.getItems());
        }
        return items;
    }

    public void give(Player player, ItemStack item, int amount) {
        for (SiloBlockData siloBlockData : getSiloBlocks()) {
            items.addAll(siloBlockData.getItems());
        }
        return items;
    }
}