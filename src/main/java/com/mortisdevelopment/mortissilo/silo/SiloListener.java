package com.mortisdevelopment.mortissilo.silo;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.mortisdevelopment.mortissilo.block.BlockData;
import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.block.SiloBlock;
import com.mortisdevelopment.mortissilo.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class SiloListener implements Listener {

    private final SiloManager siloManager;

    public SiloListener(SiloManager siloManager) {
        this.siloManager = siloManager;
    }

    @EventHandler
    public void onSiloInteract(PlayerInteractEvent e) {
        if (e.useInteractedBlock().equals(Event.Result.DENY)) {
            return;
        }
        
    }

    @EventHandler
    public void onSiloCreate(SignChangeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Component rawLine = e.line(siloManager.getSettings().getSignLine());
        if (rawLine == null) {
            return;
        }
        String line = ColorUtils.color(rawLine);
        if (!line.equalsIgnoreCase(siloManager.getSettings().getSignText())) {
            return;
        }
        Sign sign = (Sign) e.getBlock().getState();
        Block firstSiloBlock = getFirstSiloBlock(sign);
        if (!siloManager.getSiloBlockManager().isSiloBlock(firstSiloBlock)) {
            return;
        }
        siloManager.createSilo(sign, firstSiloBlock);
    }

    @EventHandler
    public void onDestroy(BlockDestroyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();

        BlockData data = siloManager.getSiloBlockData(block);
        if (data == null) {
            return;
        }
        SiloBlock siloBlock = siloBlockManager.getSiloBlock(data.getId());
        if (siloBlock == null) {
            return;
        }
        e.setCancelled(true);
    }

    private Block getFirstSiloBlock(Sign sign) {
        return sign.getBlock().getRelative(((Directional) sign.getBlockData()).getFacing().getOppositeFace());
    }
}