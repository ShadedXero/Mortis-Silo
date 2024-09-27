package com.mortisdevelopment.mortissilo.commands.subcommands;

import com.mortisdevelopment.mortissilo.block.BlockManager;
import com.mortisdevelopment.mortissilo.block.SiloBlock;
import com.mortisdevelopment.mortissilo.commands.PermissionCommand;
import com.mortisdevelopment.mortissilo.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand extends PermissionCommand {

    private final BlockManager blockManager;

    public GiveCommand(Messages messages, BlockManager blockManager) {
        super("give", "mortissilo.give", messages);
        this.blockManager = blockManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            getMessages().sendMessage(sender, "wrong_usage");
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            getMessages().sendMessage(sender, "invalid_target");
            return false;
        }
        SiloBlock siloBlock = blockManager.getSiloBlock(args[1]);
        if (siloBlock == null) {
            getMessages().sendMessage(sender, "invalid_id");
            return false;
        }
        siloBlock.give(player);
        getMessages().sendMessage(sender, "command_success");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(blockManager.getBlockById().keySet());
        }
        return null;
    }
}
