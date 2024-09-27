package com.mortisdevelopment.mortissilo.commands;

import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.commands.subcommands.GiveCommand;
import com.mortisdevelopment.mortissilo.messages.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SiloCommand extends BaseCommand {

    public SiloCommand(Messages messages, BlockManager blockManager) {
        super("mortissilo");
        setAliases(List.of("silo"));
        addSubCommand(new GiveCommand(messages, blockManager));
    }

    @Override
    public boolean isSender(CommandSender sender, boolean silent) {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return null;
    }
}
