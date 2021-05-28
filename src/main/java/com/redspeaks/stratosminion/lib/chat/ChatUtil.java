package com.redspeaks.stratosminion.lib.chat;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;

public class ChatUtil {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> colorizeList(List<String> list) {
        for(int i = 0; i < list.size(); i++) {
            list.set(i, colorize(list.get(i)));
        }
        return list;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(text);
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + (int)location.getX() + (int)location.getY() + (int)location.getZ();
    }

    public static boolean isInt(String text) {
        try {
            Integer.parseInt(text);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static Integer convertToInt(String text) {
        return Integer.parseInt(text);
    }
}
