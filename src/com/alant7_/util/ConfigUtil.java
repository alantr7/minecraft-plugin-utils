package com.alant7_.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class ConfigUtil {

    private FileConfiguration config;
    private final String path;

    private AlanJavaPlugin plugin;

    public ConfigUtil(AlanJavaPlugin plugin, String path) {
        this.plugin = plugin;
        this.path = path;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveDefaultConfig(path);
        }

        onConfigReady(config = YamlConfiguration.loadConfiguration(file));
    }

    public abstract void onConfigReady(FileConfiguration config);

    public FileConfiguration getConfig() {
        return config;
    }

    public static void register(ConfigUtil util) {
        util.load();
    }

}
