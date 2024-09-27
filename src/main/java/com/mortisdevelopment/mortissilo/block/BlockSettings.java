package com.mortisdevelopment.mortissilo.block;

import lombok.Getter;

@Getter
public class BlockSettings {

    private final int radius;

    public BlockSettings(int radius) {
        this.radius = radius;
    }
}
