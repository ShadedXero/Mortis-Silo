package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.block.SiloBlockData;
import com.mortisdevelopment.mortissilo.block.SiloBlockManager;
import com.mortisdevelopment.mortissilo.utils.Manager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.List;

@Getter
public class SiloManager extends Manager {

    private final MortisSilo plugin;
    private final SiloBlockManager siloBlockManager;
    private final SiloSettings settings;

    public SiloManager(MortisSilo plugin, SiloBlockManager siloBlockManager, SiloSettings settings) {
        this.plugin = plugin;
        this.siloBlockManager = siloBlockManager;
        this.settings = settings;
    }

    public SiloData createSilo(Sign sign, Block firstSiloBlock) {
        sign.setEditable(false);
        List<Location> locations = siloBlockManager.getSiloLocations(firstSiloBlock.getLocation());
        for (Location location : locations) {
            SiloBlockData siloBlockData = new SiloBlockData(plugin, location.getBlock());
            siloBlockData.setTerminal(sign.getLocation());
        }
        SiloData siloData = new SiloData(sign);
        siloData.create(locations);
        return siloData;
    }

    public void open(SiloData siloData) {

    }
}