package com.mortisdevelopment.mortissilo.silo;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

@Getter
public class SiloSettings {

    private final String signText;
    private final int signLine;
    private final Component menuTitle;
    private final ItemStack filterItem;
    private final ItemStack previousPageItem;
    private final ItemStack nextPageItem;
    private final ItemStack insertItem;

    public SiloSettings(String signText, int signLine, Component menuTitle, ItemStack filterItem, ItemStack previousPageItem, ItemStack nextPageItem, ItemStack insertItem) {
        this.signText = signText;
        this.signLine = signLine - 1;
        this.menuTitle = menuTitle;
        this.filterItem = filterItem;
        this.previousPageItem = previousPageItem;
        this.nextPageItem = nextPageItem;
        this.insertItem = insertItem;
    }
}
