package com.mortisdevelopment.mortissilo.commands;

import com.mortisdevelopment.mortissilo.messages.Messages;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public abstract class PlayerCommand extends PermissionCommand {

    public PlayerCommand(String name, String permission, Messages messages) {
        super(name, permission, messages);
    }

    @Override
    public boolean isSender(CommandSender sender, boolean silent) {
        if (!(sender instanceof Player player)) {
            if (!silent) {
                getMessages().sendMessage(sender, "no_console");
            }
            return false;
        }
        return super.isSender(player, silent);
    }
}
