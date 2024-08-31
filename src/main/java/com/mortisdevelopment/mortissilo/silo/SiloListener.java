package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.block.SiloBlockManager;
import com.mortisdevelopment.mortissilo.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

@Getter
public class SiloListener implements Listener {

    private final SiloManager siloManager;
    private final SiloBlockManager siloBlockManager;

    public SiloListener(SiloManager siloManager, SiloBlockManager siloBlockManager) {
        this.siloManager = siloManager;
        this.siloBlockManager = siloBlockManager;
    }

    @EventHandler
    public void onCreateSilo(SignChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Component rawLine = e.line(0);
        if (rawLine == null) {
            return;
        }
        String line = ColorUtils.color(rawLine);
        if (!line.equalsIgnoreCase(siloManager.getSettings().getSignText())) {
            return;
        }
        Block against = e.getBlock();
        if (!siloBlockManager.isSiloBlock(against)) {
            return;
        }
        siloManager.createSilo();
        Set<Location>
        Set<Location> locations = siloBlockManager.getSiloLocations(center.getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location location : locations) {
                    System.out.println(location);
                    e.getPlayer().sendBlockChange(location, Bukkit.createBlockData(Material.STONE));
                }
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onCreateSilo2(BlockPlaceEvent e) {
        Block center = e.getBlockAgainst();
        Set<Location> locations = siloBlockManager.getSiloLocations(center.getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location location : locations) {
                    System.out.println(location);
                    e.getPlayer().sendBlockChange(location, Bukkit.createBlockData(Material.STONE));
                }
            }
        }.runTask(plugin);
    }
}
