package com.mortisdevelopment.mortissilo.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.mortisdevelopment.mortissilo.MortisSilo;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class SiloBlockListener implements Listener {

    private final MortisSilo plugin;
    private final SiloBlockManager siloBlockManager;

    public SiloBlockListener(MortisSilo plugin, SiloBlockManager siloBlockManager) {
        this.plugin = plugin;
        this.siloBlockManager = siloBlockManager;
    }

    @EventHandler
    public void onSiloBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ItemStack item = e.getItemInHand();
        SiloBlock siloBlock = siloBlockManager.getSiloBlock(item);
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
        e.setBuild(false);
        item.subtract();
        siloBlock.place(plugin, e.getBlockPlaced());
    }

    @EventHandler
    public void onSiloBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        SiloBlockData data = siloBlockManager.getSiloBlockData(block);
        if (data == null) {
            return;
        }
        SiloBlock siloBlock = siloBlockManager.getSiloBlock(data.getId());
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
        e.setDropItems(false);
        e.setExpToDrop(0);
        siloBlock.mine(plugin, block);
    }

    @EventHandler
    public void onDestroy(BlockDestroyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        SiloBlockData data = siloBlockManager.getSiloBlockData(block);
        if (data == null) {
            return;
        }
        SiloBlock siloBlock = siloBlockManager.getSiloBlock(data.getId());
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
        siloBlock.mine(plugin, block);
    }
}
