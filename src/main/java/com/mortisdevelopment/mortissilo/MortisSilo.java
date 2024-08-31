package com.mortisdevelopment.mortissilo;

import com.mortisdevelopment.mortissilo.block.SiloBlockListener;
import com.mortisdevelopment.mortissilo.block.SiloBlockManager;
import com.mortisdevelopment.mortissilo.block.SiloBlockSettings;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class MortisSilo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        SiloBlockManager siloBlockManager = new SiloBlockManager(new SiloBlockSettings(5));
        getServer().getPluginManager().registerEvents(new SiloBlockListener(this, siloBlockManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
