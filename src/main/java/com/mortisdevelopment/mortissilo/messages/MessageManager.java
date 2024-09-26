package com.mortisdevelopment.mortissilo.messages;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

@Getter
public class MessageManager {

    private final HashMap<String, Messages> messagesById = new HashMap<>();

    public MessageManager(ConfigurationSection section) {
        loadMessages(section);
    }

    public MessageManager() {
    }

    public void loadMessages(ConfigurationSection section) {
        String prefix = section.getString("prefix");
        for (String id : section.getKeys(false)) {
            if (id.equals("prefix")) {
                continue;
            }
            Messages messages = messagesById.computeIfAbsent(id, k -> new Messages());
            messages.clear();
            messages.setPrefix(prefix);
            messages.loadMessages(Objects.requireNonNull(section.getConfigurationSection(id)));
        }
    }

    public void addMessages(String id, Messages messages) {
        messagesById.put(id.toLowerCase(Locale.ROOT), messages);
    }

    public Messages getMessages(String id) {
        return messagesById.get(id);
    }
}