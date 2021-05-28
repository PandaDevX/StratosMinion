package com.redspeaks.stratosminion.lib.storage;

import com.redspeaks.stratosminion.StratosMinion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Storage {

    private File file = null;
    private FileConfiguration config = null;
    public static Set<String> prohibitedBlocks = new HashSet<>();
    public Storage(StratosMinion plugin) {
        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        file = new File(plugin.getDataFolder(), "data.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> Optional<T> getRegisteredData(Class<T> type, String path) {
        return Optional.ofNullable(type.cast(getConfig().get(path)));
    }
}
