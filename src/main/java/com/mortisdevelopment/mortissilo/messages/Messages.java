package com.mortisdevelopment.mortissilo.messages;

import com.mortisdevelopment.mortissilo.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @Setter
public class Messages {

    private String prefix;
    private final HashMap<String, Component> messageById;

    public Messages(String prefix, HashMap<String, Component> messageById) {
        this.prefix = prefix;
        this.messageById = messageById;
    }

    public Messages(HashMap<String, Component> messageById) {
        this.prefix = null;
        this.messageById = messageById;
    }

    public Messages(String prefix, ConfigurationSection section) {
        this.prefix = prefix;
        this.messageById = new HashMap<>();
        loadMessages(section);
    }

    public Messages(String prefix) {
        this.prefix = prefix;
        this.messageById = new HashMap<>();
    }

    public Messages() {
        this.messageById = new HashMap<>();
    }

    public void loadMessages(ConfigurationSection section) {
        for (String id : section.getKeys(false)) {
            if (section.isList(id)) {
                addRawMessage(id, section.getStringList(id));
            }else {
                addRawMessage(id, Objects.requireNonNull(section.getString(id)));
            }
        }
    }

    public void addRawMessage(String id, String message) {
        String rawMessage;
        if (prefix != null) {
            rawMessage = message.replaceAll("%prefix%", prefix);
        }else {
            rawMessage = message;
        }
        addMessage(id.replace("-", "_").toLowerCase(), ColorUtils.getComponent(rawMessage));
    }

    public void addRawMessage(String id, List<String> messages) {
        StringJoiner joiner = new StringJoiner("\n");
        for (String message : messages) {
            joiner.add(message);
        }
        addRawMessage(id, joiner.toString());
    }

    public void addMessage(String id, Component message) {
        messageById.put(id, message);
    }

    public void addMessage(String id, List<Component> messages) {
        Component message = Component.empty();
        Iterator<Component> iterator = messages.iterator();
        while (iterator.hasNext()) {
            message = message.append(iterator.next());
            if (iterator.hasNext()) {
                message = message.append(Component.text("\n"));
            }
        }
        addMessage(id, message);
    }

    public void addMessages(HashMap<String, Component> messageById) {
        this.messageById.putAll(messageById);
    }

    public Component getMessage(String id) {
        return messageById.get(id);
    }

    public void sendMessage(Audience audience, String id) {
        if (audience == null) {
            return;
        }
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        audience.sendMessage(message);
    }

    public void broadcastMessage(String id) {
        Component message = getMessage(id);
        if (message == null) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public String getSimpleMessage(String id) {
        Component message = getMessage(id);
        if (message == null) {
            return null;
        }
        return ColorUtils.color(message);
    }

    public void clear() {
        prefix = null;
        messageById.clear();
    }
}