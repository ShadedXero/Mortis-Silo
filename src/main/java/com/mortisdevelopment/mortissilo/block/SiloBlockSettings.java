package com.mortisdevelopment.mortissilo.block;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

@Getter
public class SiloBlockSettings {

    private final int radius;
    private final Map<Material, Double> weightByMaterial;

    public SiloBlockSettings(int radius, Map<Material, Double> weightByMaterial) {
        this.radius = radius;
        this.weightByMaterial = weightByMaterial;
    }
}
