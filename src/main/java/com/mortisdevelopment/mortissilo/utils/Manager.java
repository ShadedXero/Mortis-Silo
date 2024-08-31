package com.mortisdevelopment.mortissilo.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Manager {

    private final HashMap<String, Component> messageById;

    public Manager() {
        this.messageById = new HashMap<>();
    }

    public Manager(ConfigurationSection section) {
        this.messageById = new HashMap<>();
        loadMessages(section);
    }

    public void loadMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            String id = key.replace("-", "_").toLowerCase();
            String message = section.getString(key);
            messageById.put(id, ColorUtils.getComponent(message));
        }
    }

    public void addMessage(String id, Component message) {
        //TODO: key id
        messageById.put(id, message);
    }

    public String getSimpleMessage(String id) {
        return ColorUtils.color(getMessage(id));
    }

    public Component getMessage(String id) {
        return messageById.get(id);
    }

    public void sendMessage(Player player, String id) {
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        player.sendMessage(message);
    }

    public void sendMessage(CommandSender sender, String id) {
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        sender.sendMessage(message);
    }

    public void sendMessage(UUID uuid, String id) {
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        player.sendMessage(message);
    }

    public void sendMessage(String id) {
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public static Map<String, Component> getMessageById(ConfigurationSection section) {
        Map<String, Component> messageById = new HashMap<>();
        if (section == null) {
            return messageById;
        }
        for (String key : section.getKeys(false)) {
            String id = key.replace("-", "_").toLowerCase();
            String message = section.getString(key);
            messageById.put(id, ColorUtils.getComponent(message));
        }
        return messageById;
    }
    public static Map<String, String> getRawMessageById(ConfigurationSection section) {
        Map<String, String> messageById = new HashMap<>();
        if (section == null) {
            return messageById;
        }
        for (String key : section.getKeys(false)) {
            String id = key.replace("-", "_").toLowerCase();
            String message = section.getString(key);
            messageById.put(id, ColorUtils.color(message));
        }
        return messageById;
    }
}
