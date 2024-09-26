package com.mortisdevelopment.mortissilo.block;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

@Getter
public class BlockSettings {

    private final int radius;
    private final Map<Material, Double> weightByMaterial;

    public BlockSettings(int radius, Map<Material, Double> weightByMaterial) {
        this.radius = radius;
        this.weightByMaterial = weightByMaterial;
    }
}
