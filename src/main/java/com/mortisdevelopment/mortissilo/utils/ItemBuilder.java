package com.mortisdevelopment.mortissilo.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder() {
        this.item = new ItemStack(Material.COBBLESTONE);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public static ItemStack getItem(ConfigurationSection section) {
        ItemBuilder builder = new ItemBuilder();
        if (section.contains("material")) builder.setMaterial(section.getString("material"));
        if (section.contains("texture")) builder.setTexture(section.getString("texture"));
        if (section.contains("amount")) builder.setAmount(section.getInt("amount"));
        if (section.contains("damage")) builder.setDamage(section.getInt("damage"));
        if (section.contains("name")) builder.setName(section.getString("name"));
        if (section.contains("lore")) builder.setLore(section.getStringList("lore"));
        if (section.contains("flags")) builder.setFlags(section.getStringList("flags"));
        if (section.contains("enchants")) builder.addEnchants(section.getStringList("enchants"));
        return builder.getItem();
    }

    public void setMaterial(String rawMaterial) {
        item.setType(Material.valueOf(rawMaterial));
    }

    public void setTexture(String texture) {
        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            return;
        }
        try {
            PlayerProfile profile = Bukkit.createProfile(null, null);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(new URL("https://textures.minecraft.net/texture/" + texture));
            profile.setTextures(textures);
            meta.setPlayerProfile(profile);
        }catch (MalformedURLException exp) {
            throw new RuntimeException(exp);
        }
        item.setItemMeta(meta);
    }

    public void setAmount(int amount) {
        item.setAmount(Math.max(1, amount));
    }

    public void setDamage(int damage) {
        if (!(item.getItemMeta() instanceof Damageable meta)) {
            return;
        }
        meta.setDamage(Math.max(0, damage));
        item.setItemMeta(meta);
    }

    public void setName(String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.displayName(ColorUtils.getComponent(name));
        item.setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.lore(lore.stream().map(ColorUtils::getComponent).collect(Collectors.toList()));
        item.setItemMeta(meta);
    }

    public void addEnchants(List<String> rawEnchants) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        for (String line : rawEnchants) {
            String[] raw = line.split(":");
            Enchantment enchantment = Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(raw[0])));
            int level = Integer.parseInt(raw[1]);
            item.addEnchantment(enchantment, level);
        }
        item.setItemMeta(meta);
    }

    public void setFlags(List<String> flags) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        for (String rawFlag : flags) {
            meta.addItemFlags(ItemFlag.valueOf(rawFlag));
        }
        item.setItemMeta(meta);
    }
}
