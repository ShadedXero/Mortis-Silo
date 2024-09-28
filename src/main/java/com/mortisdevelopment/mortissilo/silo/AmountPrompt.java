package com.mortisdevelopment.mortissilo.silo;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AmountPrompt extends ValidatingPrompt {

    private final SiloManager siloManager;
    private final SiloData data;
    private final ItemStack item;

    public AmountPrompt(SiloManager siloManager, SiloData data, ItemStack item) {
        this.siloManager = siloManager;
        this.data = data;
        this.item = item;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        Player player = (Player) context.getForWhom();
        siloManager.getMessages().sendMessage(player, "enter_amount");
        return "";
    }

    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        Player player = (Player) context.getForWhom();
        if (!isValid(input)) {
            siloManager.getMessages().sendMessage(player, "invalid_amount");
            return false;
        }
        return true;
    }
    
    private boolean isValid(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, @NotNull String input) {
        Player player = (Player) context.getForWhom();
        int amount = Integer.parseInt(input);
        data.give(player, item, amount);
        siloManager.getMessages().sendMessage(player, "item_received");
        siloManager.open(data, player);
        return null;
    }
}
