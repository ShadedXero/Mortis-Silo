package com.mortisdevelopment.mortissilo.block;

import com.mortisdevelopment.mortissilo.MortisSilo;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class SiloBlockManager {

    private final BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };
    private final MortisSilo plugin;
    private final SiloBlockSettings settings;
    private final Map<String, SiloBlock> blockById;

    public SiloBlockManager(MortisSilo plugin, SiloBlockSettings settings, Map<String, SiloBlock> blockById) {
        this.plugin = plugin;
        this.settings = settings;
        this.blockById = blockById;
    }

    public SiloBlock getSiloBlock(ItemStack item) {
        for (SiloBlock block : getSiloBlocks()) {
            if (block.isItem(item)) {
                return block;
            }
        }
        return null;
    }

    public SiloBlock getSiloBlock(String id) {
        return blockById.get(id);
    }

    public List<SiloBlock> getSiloBlocks() {
        return new ArrayList<>(blockById.values());
    }

    public Set<Location> getSiloLocations(Location center) {
        List<Location> connected = getDirectlyConnected(center, center);
        Set<Location> locations = new HashSet<>(connected);
        while (!connected.isEmpty()) {
            connected = getIndirectlyConnected(center, connected, locations);
            locations.addAll(connected);
        }
        return locations;
    }

    public List<Location> getIndirectlyConnected(Location center, List<Location> connected, Set<Location> locations) {
        List<Location> indirectlyConnected = new ArrayList<>();
        for (Location connectedLocation : connected) {
            List<Location> directlyConnected = getDirectlyConnected(center, connectedLocation);
            for (Location directlyConnectedLocation : directlyConnected) {
                if (locations.contains(directlyConnectedLocation)) {
                    continue;
                }
                indirectlyConnected.add(directlyConnectedLocation);
            }
        }
        return indirectlyConnected;
    }

    public List<Location> getDirectlyConnected(Location center, Location location) {
        List<Location> connected = new ArrayList<>();
        for (BlockFace face : faces) {
            Block block = location.getBlock().getRelative(face);
            Location blockLocation = block.getLocation();
            if (blockLocation.distance(center) > settings.getRadius()) {
                continue;
            }
            if (isSiloBlock(block)) {
                connected.add(blockLocation);
            }
        }
        return connected;
    }

    public boolean isSiloBlock(Block block) {
        return !new SiloBlockData(plugin, block).isInvalid();
    }

    public SiloBlockData getSiloBlockData(Block block) {
        SiloBlockData data = new SiloBlockData(plugin, block);
        if (data.isInvalid()) {
            return null;
        }
        return data;
    }
}