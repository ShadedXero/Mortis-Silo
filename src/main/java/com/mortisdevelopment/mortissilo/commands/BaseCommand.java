package com.mortisdevelopment.mortissilo.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
public abstract class BaseCommand extends BukkitCommand {

    private final List<BaseCommand> subCommands = new ArrayList<>();

    public BaseCommand(String name) {
        super(name);
    }

    public void addSubCommand(BaseCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    public abstract boolean isSender(CommandSender sender, boolean silent);

    public abstract boolean onCommand(CommandSender sender, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String label, String[] args);

    public void register(JavaPlugin plugin) {
        Bukkit.getCommandMap().register(plugin.getName().toLowerCase(Locale.ROOT), this);
    }

    public boolean isName(String name) {
        return getNames().stream().anyMatch(n -> n.equalsIgnoreCase(name));
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>(getAliases());
        names.add(0, getName());
        return names;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (!isSender(sender, false)) {
            return false;
        }
        if (onCommand(sender, label, args)) {
            return true;
        }
        return executeSubCommand(sender, label, args);
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (!isSender(sender, true)) {
            return super.tabComplete(sender, label, args);
        }
        List<String> tab = onTabComplete(sender, label, args);
        if (tab != null && !tab.isEmpty()) {
            tab.addAll(getTabCompleteSuggestions(args));
            return tab;
        }
        List<String> subTab = executeSubTabComplete(sender, label, args);
        if (subTab != null && !subTab.isEmpty()) {
            return subTab;
        }
        List<String> suggestions = getTabCompleteSuggestions(args);
        if (suggestions != null && !suggestions.isEmpty()) {
            return suggestions;
        }
        return super.tabComplete(sender, label, args);
    }

    public boolean executeSubCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        BaseCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null || !subCommand.isSender(sender, false)) {
            return false;
        }
        String[] newArgs = trimArguments(args);
        if (subCommand.onCommand(sender, label, newArgs)) {
            return true;
        }
        return subCommand.executeSubCommand(sender, label, newArgs);
    }

    public List<String> executeSubTabComplete(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return null;
        }
        BaseCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null || !subCommand.isSender(sender, true)) {
            return null;
        }
        String[] newArgs = trimArguments(args);
        List<String> tab = subCommand.onTabComplete(sender, label, newArgs);
        if (tab != null && !tab.isEmpty()) {
            tab.addAll(subCommand.getTabCompleteSuggestions(newArgs));
            return tab;
        }
        List<String> subTab = subCommand.executeSubTabComplete(sender, label, newArgs);
        if (subTab != null && !subTab.isEmpty()) {
            return subTab;
        }
        List<String> suggestions = subCommand.getTabCompleteSuggestions(newArgs);
        if (suggestions != null && !suggestions.isEmpty()) {
            return suggestions;
        }
        return null;
    }

    private BaseCommand getSubCommand(String name) {
        return subCommands.stream().filter(cmd -> cmd.isName(name)).findFirst().orElse(null);
    }

    public String[] trimArguments(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public List<String> getTabCompleteSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            subCommands.forEach(subCommand -> suggestions.addAll(subCommand.getNames()));
        }
        return suggestions;
    }
}