package com.mortisdevelopment.mortissilo.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemUtils {

    public static String serialize(ItemStack item) {
        if (item == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(item.serializeAsBytes());
    }

    public static ItemStack deserialize(String rawItem) {
        if (rawItem == null) {
            return null;
        }
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(rawItem));
    }

    public static void give(Player player, ItemStack item, boolean drop) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item);
        }else if (drop) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    public static void giveOnly(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }

    public static void drop(Location location, ItemStack item) {
        location.getWorld().dropItemNaturally(location, item);
    }

    public static void drop(Player player, ItemStack item) {
        drop(player.getLocation(), item);
    }
}
