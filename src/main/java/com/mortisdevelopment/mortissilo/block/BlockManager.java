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
public class BlockManager {

    private final BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };
    private final MortisSilo plugin;
    private final BlockSettings settings;
    private final Map<String, SiloBlock> blockById = new HashMap<>();

    public BlockManager(MortisSilo plugin, BlockSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public void addSiloBlock(SiloBlock siloBlock) {
        blockById.put(siloBlock.getId(), siloBlock);
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

    public List<BlockData> getSiloBlocks(Location center) {
        return getSiloLocations(center).stream().map(location -> new BlockData(plugin, location.getBlock())).collect(Collectors.toList());
    }

    public Set<Location> getSiloLocations(Location center) {
        Set<Location> connected = getDirectlyConnected(center, center);
        if (connected == null) {
            return null;
        }
        Set<Location> locations = new HashSet<>(connected);
        locations.add(center);
        while (!connected.isEmpty()) {
            connected = getIndirectlyConnected(center, connected, locations);
            if (connected == null) {
                return null;
            }
            locations.addAll(connected);
        }
        return locations;
    }

    private Set<Location> getIndirectlyConnected(Location center, Set<Location> connected, Set<Location> locations) {
        Set<Location> indirectlyConnected = new HashSet<>();
        for (Location connectedLocation : connected) {
            Set<Location> directlyConnected = getDirectlyConnected(center, connectedLocation);
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

    private Set<Location> getDirectlyConnected(Location center, Location location) {
        Set<Location> connected = new HashSet<>();
        for (BlockFace face : faces) {
            Block block = location.getBlock().getRelative(face);
            Location blockLocation = block.getLocation();
            if (blockLocation.distance(center) > settings.getRadius()) {
                continue;
            }
            BlockData siloBlockData = new BlockData(plugin, block);
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
        return !new BlockData(plugin, block).isInvalid();
    }

    public BlockData getSiloBlockData(Block block) {
        BlockData data = new BlockData(plugin, block);
        if (data.isInvalid()) {
            return null;
        }
        return data;
    }
}