package com.mortisdevelopment.mortissilo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class LocationUtils {

    public static Location getLocation(String rawLocation) {
        String[] raw = rawLocation.split(",");
        World world = Bukkit.getWorld(raw[0]);
        double x = Double.parseDouble(raw[1]);
        double y = Double.parseDouble(raw[2]);
        double z = Double.parseDouble(raw[3]);
        return new Location(world, x, y, z);
    }

    public static String getLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    public static List<Location> getLocations(String rawLocations) {
        List<Location> locations = new ArrayList<>();
        for (String rawLocation : rawLocations.split(":")) {
            locations.add(getLocation(rawLocation));
        }
        return locations;
    }

    public static String getLocations(List<Location> locations) {
        StringJoiner joiner = new StringJoiner(":");
        for (Location location : locations) {
            joiner.add(getLocation(location));
        }
        return joiner.toString();
    }
}
