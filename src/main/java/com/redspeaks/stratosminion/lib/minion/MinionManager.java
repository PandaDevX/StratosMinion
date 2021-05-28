package com.redspeaks.stratosminion.lib.minion;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MinionManager {

    public static Set<Minion> minions = new HashSet<>();

    public static boolean isValidItem(ItemStack itemStack) {
        if(itemStack == null) {
            return false;
        }
        if(itemStack.getItemMeta() == null) {
            return false;
        }
        if(!itemStack.hasItemMeta()) {
            return false;
        }
        if(!itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        if(!ChatUtil.strip(itemStack.getItemMeta().getDisplayName()).endsWith("Minion")) {
            return false;
        }
        if(!itemStack.getItemMeta().hasLore()) {
            return false;
        }
        if(itemStack.getItemMeta().getLore() == null) {
            return false;
        }
        if(itemStack.getItemMeta().getLore().isEmpty()) {
            return false;
        }
        for(String s : itemStack.getItemMeta().getLore()) {
            if(ChatUtil.strip(s).contains("Efficiency") || ChatUtil.strip(s).contains("Fortune")) {
                return true;
            }
        }
        return false;
    }

    public static Minion spawnMinion(ItemStack itemStack, Location location, Player spawner) {
        return new Minion(location, MinionType.parse(itemStack.getItemMeta().getDisplayName()),spawner,
                Integer.parseInt(ChatUtil.strip(itemStack.getItemMeta().getLore().stream().filter(i -> !i.equals(" ")).filter(i -> ChatUtil.strip(i).contains("Efficiency")).findAny().get().split(" ")[3])),
                Integer.parseInt(ChatUtil.strip(itemStack.getItemMeta().getLore().stream().filter(i -> !i.equals(" ")).filter(i -> ChatUtil.strip(i).contains("Fortune")).findAny().get().split(" ")[3])),
                Integer.parseInt(ChatUtil.strip(itemStack.getItemMeta().getLore().stream().filter(i -> !i.equals(" ")).filter(i -> ChatUtil.strip(i).contains("Fuel")).findAny().get().split(" ")[3])), 0);
    }

    public static Minion spawnMinion(Location location, MinionType type, OfflinePlayer spawner) {
        return new Minion(location, type, spawner);
    }

    public static Minion spawnMinion(Location location, MinionType type, OfflinePlayer spawner, int efficiency, int fortune) {
        return new Minion(location, type, spawner, efficiency, fortune);
    }

    public static Minion spawnMinion(Location location, MinionType type, OfflinePlayer spawner, int efficiency, int fortune, int fuel) {
        return new Minion(location, type, spawner, efficiency, fortune, fuel);
    }

    public static Minion spawnMinion(Location location, MinionType type, OfflinePlayer spawner, int efficiency, int fortune, int fuel, int token, long stamp) {
        Minion minion =  new Minion(location, type, spawner, efficiency, fortune, fuel, stamp);
        minion.setToken(token);
        return minion;
    }

    public static Set<Minion> getMinions() {
        return minions;
    }

    /** @deprecated **/
    public static Optional<Minion> getMinionAt(Location location) {
        return getMinions().stream().filter(m -> m.getLocation().equals(location)).findFirst();
    }

    public static Optional<Minion> getMinion(ArmorStand as) {
        return getMinions().stream().filter(m -> m.getArmorStand().getUniqueId().equals(as.getUniqueId())).findFirst();
    }
}
