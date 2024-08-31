package com.mortisdevelopment.mortissilo.silo;

import com.mortisdevelopment.mortissilo.utils.Manager;
import lombok.Getter;
import org.bukkit.block.Sign;

@Getter
public class SiloManager extends Manager {

    private final SiloSettings settings;

    public SiloManager(SiloSettings settings) {
        this.settings = settings;
    }

    public void createSilo(Sign sign) {

    }
}
