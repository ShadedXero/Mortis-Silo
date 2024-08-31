package com.mortisdevelopment.mortissilo.silo;

import lombok.Getter;

@Getter
public class SiloSettings {

    private final String signText;

    public SiloSettings(String signText) {
        this.signText = signText;
    }
}
