package com.mortisdevelopment.mortissilo;

import com.mortisdevelopment.mortissilo.block.SiloBlockListener;
import com.mortisdevelopment.mortissilo.block.SiloBlockManager;
import com.mortisdevelopment.mortissilo.block.SiloBlockSettings;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class MortisSilo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        SiloBlockManager siloBlockManager = new SiloBlockManager(new SiloBlockSettings(5));
        getServer().getPluginManager().registerEvents(new SiloBlockListener(this, siloBlockManager), this);
    }

    private SiloBlockSettings getSiloBlockSettings(ConfigurationSection section) {
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
