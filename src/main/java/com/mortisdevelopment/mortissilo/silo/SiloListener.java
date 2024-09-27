package com.mortisdevelopment.mortissilo.silo;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.mortisdevelopment.mortissilo.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

@Getter
public class SiloListener implements Listener {

    private final SiloManager siloManager;

    public SiloListener(SiloManager siloManager) {
        this.siloManager = siloManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Inventory inventory = e.getClickedInventory();
        if (inventory == null || !(inventory.getHolder() instanceof SiloMenu menu)) {
            return;
        }
        e.setCancelled(true);
        menu.click(e);
    }

    @EventHandler
    public void onSiloInteract(PlayerInteractEvent e) {
        if (e.useInteractedBlock().equals(Event.Result.DENY)) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null || !(block.getState() instanceof Sign sign)) {
            return;
        }
        SiloData data = siloManager.getSiloData(sign);
        if (data == null) {
            return;
        }
        if (!e.getAction().isRightClick()) {
            return;
        }
        e.setCancelled(true);
        siloManager.open(data, e.getPlayer());
    }

    @EventHandler
    public void onSiloCreate(SignChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Sign sign = (Sign) e.getBlock().getState();
        Component rawLine = e.line(siloManager.getSettings().getSignLine());
        if (rawLine == null) {
            return;
        }
        String line = ColorUtils.color(rawLine);
        if (!line.equalsIgnoreCase(siloManager.getSettings().getSignText())) {
            return;
        }
        Block firstSiloBlock = getFirstSiloBlock(sign);
        if (!siloManager.getSiloBlockManager().isSiloBlock(firstSiloBlock)) {
            return;
        }
        if (siloManager.createSilo(sign, firstSiloBlock) != null) {
            siloManager.getMessages().sendMessage(e.getPlayer(), "created");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        if (!(block.getState() instanceof Sign sign)) {
            return;
        }
        SiloData data = siloManager.getSiloData(sign);
        if (data == null) {
            return;
        }
        e.setCancelled(true);
        e.setDropItems(false);
        e.setExpToDrop(0);
        data.destroy(siloManager.getSiloBlockManager());
    }

    @EventHandler
    public void onBreak(BlockDestroyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        if (!(block.getState() instanceof Sign sign)) {
            return;
        }
        SiloData data = siloManager.getSiloData(sign);
        if (data == null) {
            return;
        }
        e.setCancelled(true);
        data.destroy(siloManager.getSiloBlockManager());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        if (!(block.getState() instanceof Sign sign)) {
            return;
        }
        SiloData data = siloManager.getSiloData(sign);
        if (data == null) {
            return;
        }
        e.setCancelled(true);
    }

    private Block getFirstSiloBlock(Sign sign) {
        return sign.getBlock().getRelative(((Directional) sign.getBlockData()).getFacing().getOppositeFace());
    }
}