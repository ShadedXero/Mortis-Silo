package com.mortisdevelopment.mortissilo.data.mortissilo;

import com.mortisdevelopment.mortissilo.data.PersistentData;
import org.bukkit.persistence.PersistentDataContainer;

public class SiloPersistentData extends PersistentData {

    public SiloPersistentData(PersistentDataContainer container) {
        super("mortissilo", container);
    }
}
