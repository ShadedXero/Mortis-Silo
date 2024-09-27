package com.mortisdevelopment.mortissilo.commands;

import com.mortisdevelopment.mortissilo.messages.Messages;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public abstract class PermissionCommand extends BaseCommand {

    private final String permission;
    private final Messages messages;

    public PermissionCommand(String name, String permission, Messages messages) {
        super(name);
        this.permission = permission;
        this.messages = messages;
    }

    @Override
    public boolean isSender(CommandSender sender, boolean silent) {
        if (permission == null) {
            return true;
        }
        if (!sender.hasPermission(permission)) {
            if (!silent) {
                messages.sendMessage(sender, "no_permission");
            }
            return false;
        }
        return true;
    }
}
