package com.mortisdevelopment.mortissilo;

import com.mortisdevelopment.mortissilo.block.*;
import com.mortisdevelopment.mortissilo.commands.SiloCommand;
import com.mortisdevelopment.mortissilo.messages.MessageManager;
import com.mortisdevelopment.mortissilo.silo.SiloListener;
import com.mortisdevelopment.mortissilo.silo.SiloManager;
import com.mortisdevelopment.mortissilo.silo.SiloSettings;
import com.mortisdevelopment.mortissilo.utils.ItemBuilder;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public final class MortisSilo extends JavaPlugin {

    private MessageManager messageManager;
    private WeightManager weightManager;
    private BlockManager blockManager;
    private SiloManager siloManager;
    private SiloCommand command;

    @Override
    public void onEnable() {
        // Plugin startup logic
        messageManager = new MessageManager(YamlConfiguration.loadConfiguration(getFile("messages.yml")));

        weightManager = new WeightManager();
        loadWeights();

        FileConfiguration config = YamlConfiguration.loadConfiguration(getFile("config.yml"));

        blockManager = new BlockManager(this, getBlockSettings(config));
        loadBlocks();
        getServer().getPluginManager().registerEvents(new BlockListener(blockManager), this);

        siloManager = new SiloManager(this, blockManager, weightManager, getSiloSettings(config), messageManager.getMessages("silo-messages"));
        getServer().getPluginManager().registerEvents(new SiloListener(siloManager), this);

        command = new SiloCommand(messageManager.getMessages("command-messages"), blockManager);
        command.register(this);
    }

    private void loadWeights() {
        FileConfiguration weights = YamlConfiguration.loadConfiguration(getFile("weights.yml"));
        for (String key : weights.getKeys(false)) {
            ConfigurationSection section = Objects.requireNonNull(weights.getConfigurationSection(key));
            ItemStack item = ItemBuilder.getItem(section);
            double weight = section.getDouble("weight");
            weightManager.addWeighItem(item, weight);
        }
    }

    private void loadBlocks() {
        FileConfiguration blocks = YamlConfiguration.loadConfiguration(getFile("blocks.yml"));
        for (String key : blocks.getKeys(false)) {
            ConfigurationSection section = Objects.requireNonNull(blocks.getConfigurationSection(key));
            ItemStack item = ItemBuilder.getItem(Objects.requireNonNull(section.getConfigurationSection("item")));
            Material material = Material.valueOf(section.getString("block"));
            double storage = section.getDouble("storage-size");
            BlockMode mode = BlockMode.valueOf(section.getString("mode"));
            List<ItemStack> modeList = switch (mode) {
                case NONE -> new ArrayList<>();
                case WHITELIST -> getItems(Objects.requireNonNull(section.getConfigurationSection("whitelist")));
                case BLACKLIST -> getItems(Objects.requireNonNull(section.getConfigurationSection("blacklist")));
            };
            SiloBlock siloBlock = new SiloBlock(key, material, item, storage, mode, modeList);
            if (section.contains("weights")) {
                ConfigurationSection weights = Objects.requireNonNull(section.getConfigurationSection("weights"));
                for (String id : weights.getKeys(false)) {
                    ConfigurationSection weightSection = Objects.requireNonNull(weights.getConfigurationSection(id));
                    ItemStack weighItem = ItemBuilder.getItem(weightSection);
                    double weight = weightSection.getDouble("weight");
                    siloBlock.addWeighItem(weighItem, weight);
                }
            }
            blockManager.addSiloBlock(siloBlock);
        }
    }

    private List<ItemStack> getItems(ConfigurationSection config) {
        List<ItemStack> items = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection(key));
            ItemStack item = ItemBuilder.getItem(section);
            items.add(item);
        }
        return items;
    }

    private SiloSettings getSiloSettings(ConfigurationSection section) {
        String signText = section.getString("sign-text");
        int signLine = section.getInt("sign-line");
        ItemStack filterItem = ItemBuilder.getItem(Objects.requireNonNull(section.getConfigurationSection("filter-item")));
        ItemStack previousPageItem = ItemBuilder.getItem(Objects.requireNonNull(section.getConfigurationSection("previous-page-item")));
        ItemStack nextPageItem = ItemBuilder.getItem(Objects.requireNonNull(section.getConfigurationSection("next-page-item")));
        ItemStack insertItem = ItemBuilder.getItem(Objects.requireNonNull(section.getConfigurationSection("insert-item")));
        return new SiloSettings(signText, signLine, filterItem, previousPageItem, nextPageItem, insertItem);
    }

    private BlockSettings getBlockSettings(ConfigurationSection section) {
        return new BlockSettings(section.getInt("radius"));
    }

    private File getFile(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, true);
        }
        return file;
    }
}
