package com.mortisdevelopment.mortissilo;

import com.mortisdevelopment.mortissilo.block.BlockListener;
import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.block.BlockSettings;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class MortisSilo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BlockManager siloBlockManager = new BlockManager(new BlockSettings(5));
        getServer().getPluginManager().registerEvents(new BlockListener(this, siloBlockManager), this);
    }

    private BlockSettings getSiloBlockSettings(ConfigurationSection section) {
        return null;
    }

    private File getFile(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, true);
        }
        return file;
    }
}
