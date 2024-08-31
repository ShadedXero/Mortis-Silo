package com.mortisdevelopment.mortissilo.block;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mortisdevelopment.mortissilo.MortisSilo;
import com.mortisdevelopment.mortissilo.data.mortissilo.SiloPersistentData;
import com.mortisdevelopment.mortissilo.utils.ItemUtils;
import com.mortisdevelopment.mortissilo.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SiloBlockData extends SiloPersistentData {

    private final String idKey = "id";
    private final String terminalKey = "terminal";
    private final String maxSlotsKey = "max_slots";
    private final String deletedSlots = "deleted_slots";

    public SiloBlockData(MortisSilo plugin, Block block) {
        super(new CustomBlockData(block, plugin));
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

    public Location getTerminal() {
        String rawLocation = getString(terminalKey);
        if (rawLocation == null) {
            return null;
        }
        return LocationUtils.getLocation(rawLocation);
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

    public String getItemKey(int slot) {
        return "item_" + slot;
    }

    public void setItem(int slot, ItemStack item) {
        setString(getItemKey(slot), ItemUtils.serialize(item));
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

    public void addItem(ItemStack item) {
        setItem(getAvailableSlot(), item);
    }

    public void removeItem(int slot) {
        remove(getItemKey(slot));
        addDeletedSlot(slot);
    }

    public void dumpItems(Location location) {
        //TODO
    }
}