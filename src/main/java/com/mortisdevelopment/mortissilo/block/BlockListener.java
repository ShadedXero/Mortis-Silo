package com.mortisdevelopment.mortissilo.block;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    private final BlockManager blockManager;

    public BlockListener(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    @EventHandler
    public void onSiloBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        ItemStack item = e.getItemInHand();
        SiloBlock siloBlock = blockManager.getSiloBlock(item);
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
        e.setBuild(false);
        item.subtract();
        Bukkit.getScheduler().runTask(blockManager.getPlugin(), () -> siloBlock.place(blockManager.getPlugin(), e.getBlockPlaced()));
    }

    @EventHandler
    public void onSiloBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        BlockData data = blockManager.getSiloBlockData(block);
        if (data == null) {
            return;
        }
        SiloBlock siloBlock = blockManager.getSiloBlock(data.getId());
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
        e.setDropItems(false);
        e.setExpToDrop(0);
        data.destroy(blockManager);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        BlockData data = blockManager.getSiloBlockData(block);
        if (data == null) {
            return;
        }
        SiloBlock siloBlock = blockManager.getSiloBlock(data.getId());
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
    }
}
