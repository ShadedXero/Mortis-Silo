package com.mortisdevelopment.mortissilo.silo;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AmountPrompt extends ValidatingPrompt {

    private final SiloData data;
    private final ItemStack item;

    public AmountPrompt(SiloData data, ItemStack item) {
        this.data = data;
        this.item = item;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        Player player = (Player) context.getForWhom();
        player.sendMessage("Enter an amount");
        return "Enter an amount";
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        Player player = (Player) context.getForWhom();
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid amount");
            return false;
        }
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, @NotNull String input) {
        Player player = (Player) context.getForWhom();
        int amount = Integer.parseInt(input);
        data.give(player, item, amount);
        player.sendMessage("Successfully renamed the region");
        return null;
    }
}
