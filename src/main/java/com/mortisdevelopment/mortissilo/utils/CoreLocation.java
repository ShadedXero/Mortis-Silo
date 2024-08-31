package com.mortisdevelopment.mortissilo.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

@Getter @Setter
public class CoreLocation extends Location {

    private CoreWorld coreWorld;

    public CoreLocation(CoreWorld coreWorld, double x, double y, double z) {
        super(null, x, y, z);
        this.coreWorld = coreWorld;
    }

    public CoreLocation(CoreWorld coreWorld, double x, double y, double z, float yaw, float pitch) {
        super(null, x, y, z, yaw, pitch);
        this.coreWorld = coreWorld;
    }

    public boolean isWorldLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return coreWorld.getWorld();
    }

    @Override
    public void setWorld(World world) {
        this.coreWorld = new CoreWorld(world);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        CoreLocation other = (CoreLocation) object;
        if (!coreWorld.equals(other.coreWorld)) {
            return false;
        }
        if (Double.doubleToLongBits(getX()) != Double.doubleToLongBits(other.getX())) {
            return false;
        }
        if (Double.doubleToLongBits(getY()) != Double.doubleToLongBits(other.getY())) {
            return false;
        }
        if (Double.doubleToLongBits(getZ()) != Double.doubleToLongBits(other.getZ())) {
            return false;
        }
        if (Float.floatToIntBits(getPitch()) != Float.floatToIntBits(other.getPitch())) {
            return false;
        }
        return Float.floatToIntBits(getYaw()) == Float.floatToIntBits(other.getYaw());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (coreWorld != null ? coreWorld.hashCode() : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(getX()) ^ (Double.doubleToLongBits(getX()) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(getY()) ^ (Double.doubleToLongBits(getY()) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(getZ()) ^ (Double.doubleToLongBits(getZ()) >>> 32));
        hash = 19 * hash + Float.floatToIntBits(getPitch());
        hash = 19 * hash + Float.floatToIntBits(getYaw());
        return hash;
    }

    @Override
    public String toString() {
        return "Location{" + "world=" + coreWorld.getWorldName() + ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + ",pitch=" + getPitch() + ",yaw=" + getYaw() + '}';
    }

    public @NotNull CoreLocation toLocation(@NotNull CoreWorld coreWorld) {
        return new CoreLocation(coreWorld, getX(), getY(), getZ(), getYaw(), getPitch());
    }

    public void serialize(ConfigurationSection section) {
        section.set("world", coreWorld.getWorldName());
        section.set("x", getX());
        section.set("y", getY());
        section.set("z", getZ());
        section.set("pitch", getPitch());
        section.set("yaw", getYaw());
    }

    public static CoreLocation deserialize(ConfigurationSection section) {
        CoreWorld coreWorld = new CoreWorld(section.getString("world"));
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float pitch = section.getInt("pitch");
        float yaw = section.getInt("yaw");
        return new CoreLocation(coreWorld, x, y, z, pitch, yaw);
    }
}