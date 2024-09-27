package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.block.BlockData;
import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.messages.Messages;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
public class SiloManager {

    private final MortisSilo plugin;
    private final BlockManager siloBlockManager;
    private final WeightManager weightManager;
    private final SiloSettings settings;
    private final Messages messages;

    public SiloManager(MortisSilo plugin, BlockManager siloBlockManager, WeightManager weightManager, SiloSettings settings, Messages messages) {
        this.plugin = plugin;
        this.siloBlockManager = siloBlockManager;
        this.weightManager = weightManager;
        this.settings = settings;
        this.messages = messages;
    }

    public SiloData getSiloData(Sign sign) {
        SiloData data = new SiloData(plugin, sign);
        if (data.isInvalid()) {
            return null;
        }
        return data;
    }

    public SiloData createSilo(Sign sign, Block firstSiloBlock) {
        Set<Location> locations = siloBlockManager.getSiloLocations(firstSiloBlock.getLocation());
        if (locations == null) {
            return null;
        }
        for (Location location : locations) {
            BlockData siloBlockData = new BlockData(plugin, location.getBlock());
            siloBlockData.setTerminal(sign.getLocation());
        }
        SiloData siloData = new SiloData(plugin, sign);
        siloData.create(locations);
        return siloData;
    }

    public void open(SiloData data, Player player) {
        new SiloMenu(this, data).open(player);
    }
}