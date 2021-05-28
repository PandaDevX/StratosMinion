package com.redspeaks.stratosminion;

import com.redspeaks.stratosminion.commands.MainCommand;
import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.minion.Minion;
import com.redspeaks.stratosminion.lib.minion.MinionAttribute;
import com.redspeaks.stratosminion.lib.minion.MinionManager;
import com.redspeaks.stratosminion.lib.minion.MinionType;
import com.redspeaks.stratosminion.lib.storage.Storage;
import com.redspeaks.stratosminion.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class StratosMinion extends JavaPlugin {
    private static StratosMinion instance = null;
    private Storage storage = null;
    private int mainTask = -1;
    private final static HashMap<String, Boolean> booleanHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;
        storage = new Storage(this);
        saveDefaultConfig();

        // spawn minions
        if(!storage.getConfig().getKeys(false).isEmpty()) {
            for (String key : storage.getConfig().getKeys(false)) {

                Location location = storage.getConfig().getLocation(key + ".location");
                Minion minion = MinionManager.spawnMinion(location, MinionType.getMinion(storage.getRegisteredData(String.class, key + ".type").get()),
                        Bukkit.getOfflinePlayer(UUID.fromString(storage.getRegisteredData(String.class, key + ".spawner").get())),
                        storage.getRegisteredData(Integer.class, key + ".attributes.efficiency").orElse(1),
                        storage.getRegisteredData(Integer.class, key + ".attributes.fortune").orElse(1),
                        storage.getRegisteredData(Integer.class, key + ".fuel").orElse(1),
                        storage.getRegisteredData(Integer.class, key + ".tokens").orElse(1),
                        storage.getConfig().getLong(key + ".fuel_stamp"));
                minion.startMining();

                location = null;
            }
        }
        startTasks();
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new InteractAtMinionListener(), this);
        getServer().getPluginManager().registerEvents(new MineListener(), this);

        getCommand("minion").setExecutor(new MainCommand());
        getCommand("minion").setTabCompleter(new MainCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinionManager.getMinions().stream().filter(Objects::nonNull).forEach(m -> m.saveData(storage));
        MinionManager.getMinions().stream().filter(Objects::nonNull).forEach(Minion::kill);
        MinionManager.getMinions().clear();

        Bukkit.getScheduler().cancelTasks(this);
    }

    public static StratosMinion getInstance() {
        return instance;
    }

    public FileConfiguration getSettings() {
        reloadConfig();
        return getConfig();
    }

    public void startTasks() {
        if(mainTask == -1) {
            mainTask = new BukkitRunnable() {

                @Override
                public void run() {
                    for(Minion m : MinionManager.getMinions()) {
                        if (m.emptyFuel()) {
                            return;
                        }
                        if(m.getFuelRemovalStamp() <= System.currentTimeMillis()) {
                            m.setFuel(m.getFuel() - 1);
                            m.setFuelRemovalStamp(5);
                        }
                        boolean goingUp = booleanHashMap.getOrDefault(m.getArmorStand().getUniqueId().toString(), false);
                        if (m.getArmorStand().getRightArmPose().getX() >= 5.2) {
                            booleanHashMap.put(m.getArmorStand().getUniqueId().toString(), true);
                        }
                        if (m.getArmorStand().getRightArmPose().getX() <= 4.7) {
                            booleanHashMap.put(m.getArmorStand().getUniqueId().toString(), false);
                        }
                        if (goingUp) {
                            m.getArmorStand().setRightArmPose(m.getArmorStand().getRightArmPose().subtract(0.05, 0, 0));
                        } else {
                            m.getArmorStand().setRightArmPose(m.getArmorStand().getRightArmPose().add(0.05, 0, 0));
                        }
                        Storage.prohibitedBlocks.add(ChatUtil.serializeLocation(m.getRelativeLocation()));
                        final Block block = m.getRelativeLocation().getBlock();
                        if(m.getTimeStamp() <= System.currentTimeMillis()) {
                            if (block.getType() != Material.AIR) {
                                Bukkit.getScheduler().runTaskLater(StratosMinion.getInstance(), () -> {
                                    block.setType(Material.AIR);
                                }, 2L);
                                for (int i = 0; i < m.getAttribute(MinionAttribute.EFFICIENCY); i++) {
                                    m.setToken(m.getToken() + (m.getType().getOption().getStartingPrice() * (long) m.getAttribute(MinionAttribute.FORTUNE)));
                                }
                                m.setTimeStamp(3);
                                return;
                            }
                            Bukkit.getScheduler().runTaskLater(StratosMinion.getInstance(), () -> {
                                block.setType(m.getType().getMaterial());
                                m.setTimeStamp(3);
                            }, 2L);
                        }
                    }
                }
            }.runTaskTimerAsynchronously(this, 0, 3).getTaskId();
        }
    }

    public Storage getStorage() {
        return storage;
    }
}
