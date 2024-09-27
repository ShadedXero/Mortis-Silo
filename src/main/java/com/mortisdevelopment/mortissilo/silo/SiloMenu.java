package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.data.BlockItem;
import com.mortisdevelopment.mortissilo.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class SiloMenu implements InventoryHolder {

    private final int size = 54;
    private final int insertSlot = 49;
    private final int previousPageSlot = 48;
    private final int nextPageSlot = 50;
    private final int inventoryEndingSlot = 44;
    private final int bottomBarStartingSlot = 45;
    private int page = 1;
    private final ConversationFactory factory;
    private final SiloManager siloManager;
    private final SiloData siloData;
    private final Inventory inventory;
    private final Map<Integer, List<ItemStack>> itemsByPage;

    public SiloMenu(SiloManager siloManager, SiloData siloData) {
        this.factory = new ConversationFactory(siloManager.getPlugin());
        this.siloManager = siloManager;
        this.siloData = siloData;
        this.inventory = createInventory();
        this.itemsByPage = createPages();
        update();
    }

    public void update() {
        List<ItemStack> items = itemsByPage.getOrDefault(page, new ArrayList<>());
        for (int i = 0; i < inventoryEndingSlot; i++) {
            if (i < items.size()) {
                inventory.setItem(i, items.get(i));
            }else {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    private Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(this, size, ColorUtils.getComponent("test"));
        for (int i = bottomBarStartingSlot; i < size; i++) {
            inventory.setItem(i, siloManager.getSettings().getFilterItem());
        }
        inventory.setItem(previousPageSlot, siloManager.getSettings().getPreviousPageItem());
        inventory.setItem(nextPageSlot, siloManager.getSettings().getNextPageItem());
        inventory.setItem(insertSlot, siloManager.getSettings().getInsertItem());
        return inventory;
    }

    private Map<Integer, List<ItemStack>> createPages() {
        Map<Integer, List<ItemStack>> itemsByPage = new HashMap<>();
        int page = 1;
        int index = 0;
        for (ItemStack item : getUniqueItems()) {
            if (index >= 53) {
                page++;
                index = 0;
            }
            itemsByPage.computeIfAbsent(page, k -> new ArrayList<>()).add(item);
            index++;
        }
        return itemsByPage;
    }

    private List<ItemStack> getUniqueItems() {
        List<ItemStack> uniqueItems = new ArrayList<>();
        for (BlockItem item : siloData.getUniqueItems()) {
            boolean unique = true;
            for (ItemStack uniqueItem : uniqueItems) {
                if (item.isItem(uniqueItem)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                uniqueItems.add(item.getItem());
            }
        }
        return uniqueItems;
    }

    public void click(InventoryClickEvent e) {
        int slot = e.getSlot();
        if (slot == previousPageSlot) {
            previousPage();
            return;
        }
        if (slot == nextPageSlot) {
            nextPage();
            return;
        }
        Player player = (Player) e.getWhoClicked();
        if (slot == insertSlot) {
            ItemStack cursor = e.getCursor();
            if (cursor == null || cursor.getType().isAir()) {
                return;
            }
            ItemStack clone = cursor.clone();
            clone.setAmount(1);
            int amount = cursor.getAmount();
            while (amount > 0) {
                if (!siloData.store(siloManager.getSiloBlockManager(), siloManager.getWeightManager(), clone)) {
                    break;
                }
                amount--;
            }
            cursor.setAmount(amount);
            update();
            return;
        }
        if (slot >= 0 && slot <= inventoryEndingSlot) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType().isAir()) {
                return;
            }
            close(player);
            factory.withFirstPrompt(new AmountPrompt(siloManager, siloData, item)).buildConversation(player).begin();
        }
    }

    private boolean hasPage(int page) {
        return itemsByPage.get(page) != null;
    }

    public boolean previousPage() {
        int newPage = page - 1;
        if (!hasPage(newPage)) {
            return false;
        }
        this.page = newPage;
        update();
        return true;
    }

    public boolean nextPage() {
        int newPage = page + 1;
        if (!hasPage(newPage)) {
            return false;
        }
        this.page = newPage;
        update();
        return true;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }
}