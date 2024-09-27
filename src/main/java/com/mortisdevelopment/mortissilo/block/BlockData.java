package com.mortisdevelopment.mortissilo.block;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.data.BlockItem;
import com.mortisdevelopment.mortissilo.data.mortissilo.SiloPersistentData;
import com.mortisdevelopment.mortissilo.silo.SiloData;
import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import com.mortisdevelopment.mortissilo.utils.LocationUtils;
import com.mortisdevelopment.mortissilo.weights.Weight;
import com.mortisdevelopment.mortissilo.weights.WeightManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class BlockData extends SiloPersistentData {

    private final String idKey = "id";
    private final String terminalKey = "terminal";
    private final String maxSlotsKey = "max_slots";
    private final String deletedSlots = "deleted_slots";
    private final MortisSilo plugin;
    private final Block block;

    public BlockData(MortisSilo plugin, Block block) {
        super(new CustomBlockData(block, plugin));
        this.plugin = plugin;
        this.block = block;
    }

    public SiloBlock getSiloBlock(BlockManager siloBlockManager) {
        return siloBlockManager.getSiloBlock(getId());
    }

    public Location getLocation() {
        return block.getLocation();
    }

    public void create(String id) {
        setId(id);
    }

    public boolean isInvalid() {
        return getId() == null;
    }

    public boolean isTerminalConnected() {
        return getTerminal() != null;
    }

    public void setId(String id) {
        setString(idKey, id);
    }

    public String getId() {
        return getString(idKey);
    }

    public void setTerminal(Location location) {
        setString(terminalKey, LocationUtils.getLocation(location));
    }

    public Location getTerminalLocation() {
        String rawLocation = getString(terminalKey);
        if (rawLocation == null) {
            return null;
        }
        return LocationUtils.getLocation(rawLocation);
    }

    public SiloData getTerminal() {
        Location location = getTerminalLocation();
        if (location == null) {
            return null;
        }
        BlockState state = location.getBlock().getState();
        if (!(state instanceof Sign sign)) {
            return null;
        }
        return new SiloData(plugin, sign);
    }

    public int getMaxSlots() {
        Integer slots = getInteger(maxSlotsKey);
        if (slots == null) {
            return 1;
        }
        return slots;
    }

    public void setMaxSlots(int slots) {
        setInteger(maxSlotsKey, slots);
    }

    public int[] getDeletedSlots() {
        return getIntegerArray(deletedSlots);
    }

    public void setDeletedSlots(int[] slots) {
        setIntegerArray(deletedSlots, slots);
    }

    public void addDeletedSlot(int slot) {
        int[] oldSpaces = getDeletedSlots();
        int[] newSpaces = Arrays.copyOf(oldSpaces, oldSpaces.length + 1);
        newSpaces[newSpaces.length - 1] = slot;
        setDeletedSlots(newSpaces);
    }

    public void removeDeletedSlot(int slot) {
        int[] oldSpaces = getDeletedSlots();
        int indexToRemove = -1;
        for (int i = 0; i < oldSpaces.length; i++) {
            if (oldSpaces[i] == slot) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove == -1) {
            return;
        }
        int[] newSpaces = new int[oldSpaces.length - 1];
        System.arraycopy(oldSpaces, 0, newSpaces, 0, indexToRemove);
        System.arraycopy(oldSpaces, indexToRemove + 1, newSpaces, indexToRemove, oldSpaces.length - indexToRemove - 1);
        if (newSpaces.length == 0) {
            setDeletedSlots(null);
            return;
        }
        setDeletedSlots(newSpaces);
    }

    public int getFirstDeletedSlot() {
        int[] spaces = getDeletedSlots();
        if (spaces == null) {
            return 0;
        }
        return spaces[0];
    }

    public int getAvailableSlot() {
        int deletedSlot = getFirstDeletedSlot();
        if (deletedSlot > 0) {
            return deletedSlot;
        }
        int slot = getMaxSlots() + 1;
        setMaxSlots(slot);
        return slot;
    }

    public String getItemKey(int slot) {
        return "item_" + slot;
    }

    public String getAmountKey(int slot) {
        return "amount_" + slot;
    }

    private void setItem(int slot, ItemStack item) {
        setByteArray(getItemKey(slot), item.serializeAsBytes());
    }

    private ItemStack getItem(int slot) {
        byte[] rawItem = getByteArray(getItemKey(slot));
        if (rawItem == null) {
            return null;
        }
        return ItemStack.deserializeBytes(rawItem);
    }

    private void setAmount(int slot, int amount) {
        setInteger(getAmountKey(slot), amount);
    }

    private int getAmount(int slot) {
        Integer amount = getInteger(getAmountKey(slot));
        if (amount == null) {
            return 1;
        }
        return amount;
    }

    private void removeItem(int slot) {
        remove(getItemKey(slot));
        remove(getAmountKey(slot));
        addDeletedSlot(slot);
    }

    public List<BlockItem> getItems() {
        List<BlockItem> dataList = new ArrayList<>();
        for (int i = 1; i <= getMaxSlots(); i++) {
            ItemStack item = getItem(i);
            if (item == null) {
                continue;
            }
            int amount = getAmount(i);
            BlockItem data = new BlockItem(i, item, amount);
            dataList.add(data);
        }
        return dataList;
    }

    public BlockItem getItem(ItemStack item) {
        for (BlockItem data : getItems()) {
            if (!data.getItem().isSimilar(item)) {
                continue;
            }
            return data;
        }
        return null;
    }

    public void addItem(ItemStack item) {
        BlockItem data = getItem(item);
        if (data == null) {
            int slot = getAvailableSlot();
            setItem(slot, item);
            setAmount(slot, item.getAmount());
            return;
        }
        setAmount(data.getSlot(), item.getAmount());
    }

    public void removeItem(BlockItem item) {
        removeItem(item.getSlot());
    }

    public void updateAmount(BlockItem blockItem) {
        setAmount(blockItem.getSlot(), blockItem.getAmount());
    }

    public void clearItems() {
        for (int i = 1; i <= getMaxSlots(); i++) {
            removeItem(i);
        }
        remove(deletedSlots);
        remove(maxSlotsKey);
    }

    public void dumpItems(Location location) {
        for (BlockItem data : getItems()) {
            for (int i = 0; i < data.getAmount(); i++) {
                ItemUtils.drop(location, data.getItem());
            }
        }
        clearItems();
    }

    public boolean canStore(BlockManager blockManager, WeightManager weightManager, ItemStack item) {
        SiloBlock siloBlock = getSiloBlock(blockManager);
        if (!siloBlock.canStore(item)) {
            return false;
        }
        Weight weight = weightManager.getWeight(siloBlock, item);
        double availableWeight = getAvailableWeight(blockManager, weightManager);
        return availableWeight >= weight.getWeight(item.getAmount());
    }

    public double getAvailableWeight(BlockManager blockManager, WeightManager weightManager) {
        double totalWeight = getTotalWeight(blockManager, weightManager);
        return Math.max(0, getSiloBlock(blockManager).getStorage() - totalWeight);
    }

    public double getTotalWeight(BlockManager siloBlockManager, WeightManager weightManager) {
        double totalWeight = 0;
        for (BlockItem data : getItems()) {
            ItemStack item = data.getItem();
            Weight weight = weightManager.getWeight(getSiloBlock(siloBlockManager), item);
            totalWeight += weight.getWeight(data.getAmount());
        }
        return totalWeight;
    }
}