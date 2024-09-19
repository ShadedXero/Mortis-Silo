package com.mortisdevelopment.mortissilo.block;

import com.mortisdevelopment.mortissilo.MortisSilo;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<SiloBlockData> getSiloBlocks(Location center) {
        return getSiloLocations(center).stream().map(location -> new SiloBlockData(plugin, location.getBlock())).collect(Collectors.toList());
    }

    public List<Location> getSiloLocations(Location center) {
        List<Location> connected = getDirectlyConnected(center, center);
        if (connected == null) {
            return null;
        }
        List<Location> locations = new ArrayList<>(connected);
        while (!connected.isEmpty()) {
            connected = getIndirectlyConnected(center, connected, locations);
            if (connected == null) {
                return null;
            }
            locations.addAll(connected);
        }
        return locations;
    }

    private List<Location> getIndirectlyConnected(Location center, List<Location> connected, List<Location> locations) {
        List<Location> indirectlyConnected = new ArrayList<>();
        for (Location connectedLocation : connected) {
            List<Location> directlyConnected = getDirectlyConnected(center, connectedLocation);
            if (directlyConnected == null) {
                return null;
            }
            for (Location directlyConnectedLocation : directlyConnected) {
                if (locations.contains(directlyConnectedLocation)) {
                    continue;
                }
                indirectlyConnected.add(directlyConnectedLocation);
            }
        }
        return indirectlyConnected;
    }

    private List<Location> getDirectlyConnected(Location center, Location location) {
        List<Location> connected = new ArrayList<>();
        for (BlockFace face : faces) {
            Block block = location.getBlock().getRelative(face);
            Location blockLocation = block.getLocation();
            if (blockLocation.distance(center) > settings.getRadius()) {
                continue;
            }
            SiloBlockData siloBlockData = new SiloBlockData(plugin, block);
            if (siloBlockData.isTerminalConnected()) {
                return null;
            }
            if (!siloBlockData.isInvalid()) {
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