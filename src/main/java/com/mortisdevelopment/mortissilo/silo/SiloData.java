package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.data.mortissilo.SiloPersistentData;
import com.mortisdevelopment.mortissilo.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SiloData extends SiloPersistentData {

    private final String siloBlocksKey = "silo_blocks";
    private final String inHoppersKey = "in_hoppers";
    private final String outHoppersKey = "out_hoppers";
    private final Sign sign;

    public SiloData(Sign sign) {
        super(sign.getPersistentDataContainer());
        this.sign = sign;
    }

    public void create(List<Location> siloBlocks) {
        setSiloBlocks(siloBlocks);
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

    public void setSiloBlocks(List<Location> locations) {
        setLocations(siloBlocksKey, locations);
    }

    public List<Location> getSiloBlocks() {
        return getLocations(siloBlocksKey);
    }

    public void setInHoppers(List<Location> inHoppers) {
        setLocations(inHoppersKey, inHoppers);
    }

    public List<Location> getInHoppers() {
        return getLocations(inHoppersKey);
    }

    public void setOutHoppers(List<Location> outHoppers) {
        setLocations(outHoppersKey, outHoppers);
    }

    public List<Location> getOutHoppers() {
        return getLocations(outHoppersKey);
    }
}