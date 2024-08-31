package com.mortisdevelopment.mortissilo.data;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@Getter
public class PersistentData {
    
    private final String namespace;
    private final PersistentDataContainer container;

    public PersistentData(JavaPlugin plugin, PersistentDataContainer container) {
        this.namespace = plugin.getName().toLowerCase(Locale.ROOT);
        this.container = container;
    }

    public PersistentData(String namespace, PersistentDataContainer container) {
        this.namespace = namespace;
        this.container = container;
    }

    public <T> boolean is(@NotNull NamespacedKey key, PersistentDataType<?, T> type, T value) {
        T object = get(key, type);
        if (object == null) {
            return false;
        }
        return object.equals(value);
    }

    public <T> boolean is(@NotNull String key, PersistentDataType<?, T> type, T value) {
        return is(new NamespacedKey(namespace, key), type, value);
    }

    public <T> void set(@NotNull String key, PersistentDataType<?, T> type, T value) {
        set(new NamespacedKey(namespace, key), type, value);
    }

    public <T> void set(@NotNull NamespacedKey key, PersistentDataType<?, T> type, T value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, type, value);
    }

    public <T> T get(@NotNull String key, PersistentDataType<?, T> type) {
        return get(new NamespacedKey(namespace, key), type);
    }

    public <T> T get(@NotNull NamespacedKey key, PersistentDataType<?, T> type) {
        return container.get(key, type);
    }

    public void setString(@NotNull String key, String value) {
        setString(new NamespacedKey(namespace, key), value);
    }

    public String getString(@NotNull String key) {
        return getString(new NamespacedKey(namespace, key));
    }

    public void setString(@NotNull NamespacedKey key, String value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.STRING, value);
    }

    public String getString(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.STRING);
    }

    public void setByte(@NotNull String key, Byte value) {
        setByte(new NamespacedKey(namespace, key), value);
    }

    public Byte getByte(@NotNull String key) {
        return getByte(new NamespacedKey(namespace, key));
    }

    public void setByte(@NotNull NamespacedKey key, Byte value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.BYTE, value);
    }

    public Byte getByte(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.BYTE);
    }

    public void setByteArray(@NotNull String key, byte[] value) {
        setByteArray(new NamespacedKey(namespace, key), value);
    }

    public byte[] getByteArray(@NotNull String key) {
        return getByteArray(new NamespacedKey(namespace, key));
    }

    public void setByteArray(@NotNull NamespacedKey key, byte[] value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.BYTE_ARRAY, value);
    }

    public byte[] getByteArray(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.BYTE_ARRAY);
    }

    public void setShort(@NotNull String key, Short value) {
        setShort(new NamespacedKey(namespace, key), value);
    }

    public Short getShort(@NotNull String key) {
        return getShort(new NamespacedKey(namespace, key));
    }

    public void setShort(@NotNull NamespacedKey key, Short value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.SHORT, value);
    }

    public Short getShort(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.SHORT);
    }

    public void setInteger(@NotNull String key, Integer value) {
        setInteger(new NamespacedKey(namespace, key), value);
    }

    public Integer getInteger(@NotNull String key) {
        return getInteger(new NamespacedKey(namespace, key));
    }

    public void setInteger(@NotNull NamespacedKey key, Integer value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.INTEGER, value);
    }

    public Integer getInteger(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.INTEGER);
    }

    public void setIntegerArray(@NotNull String key, int[] value) {
        setIntegerArray(new NamespacedKey(namespace, key), value);
    }

    public int[] getIntegerArray(@NotNull String key) {
        return getIntegerArray(new NamespacedKey(namespace, key));
    }

    public void setIntegerArray(@NotNull NamespacedKey key, int[] value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.INTEGER_ARRAY, value);
    }

    public int[] getIntegerArray(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.INTEGER_ARRAY);
    }

    public void setLong(@NotNull String key, Long value) {
        setLong(new NamespacedKey(namespace, key), value);
    }

    public Long getLong(@NotNull String key) {
        return getLong(new NamespacedKey(namespace, key));
    }

    public void setLong(@NotNull NamespacedKey key, Long value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.LONG, value);
    }

    public Long getLong(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.LONG);
    }

    public void setLongArray(@NotNull String key, long[] value) {
        setLongArray(new NamespacedKey(namespace, key), value);
    }

    public long[] getLongArray(@NotNull String key) {
        return getLongArray(new NamespacedKey(namespace, key));
    }

    public void setLongArray(@NotNull NamespacedKey key, long[] value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.LONG_ARRAY, value);
    }

    public long[] getLongArray(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.LONG_ARRAY);
    }

    public void setFloat(@NotNull String key, Float value) {
        setFloat(new NamespacedKey(namespace, key), value);
    }

    public Float getFloat(@NotNull String key) {
        return getFloat(new NamespacedKey(namespace, key));
    }

    public void setFloat(@NotNull NamespacedKey key, Float value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.FLOAT, value);
    }

    public Float getFloat(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.FLOAT);
    }

    public void setDouble(@NotNull String key, Double value) {
        setDouble(new NamespacedKey(namespace, key), value);
    }

    public Double getDouble(@NotNull String key) {
        return getDouble(new NamespacedKey(namespace, key));
    }

    public void setDouble(@NotNull NamespacedKey key, Double value) {
        if (value == null) {
            remove(key);
            return;
        }
        container.set(key, PersistentDataType.DOUBLE, value);
    }

    public Double getDouble(@NotNull NamespacedKey key) {
        return container.get(key, PersistentDataType.DOUBLE);
    }

    public void remove(String key) {
        remove(new NamespacedKey(namespace, key));
    }

    public void remove(NamespacedKey key) {
        if (key == null) {
            return;
        }
        container.remove(key);
    }

    public void clear() {
        for (NamespacedKey key : container.getKeys()) {
            container.remove(key);
        }
    }
}
