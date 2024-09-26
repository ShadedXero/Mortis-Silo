package com.mortisdevelopment.mortissilo.utils;

public class SiloObjects {

    public static <T> T requireNonNullElse(T object, T object2) {
        if (object != null) {
            return object;
        }
        return object2;
    }
}
