package com.alant7_.util;

import com.alant7_.util.data.IDataLoader;
import com.alant7_.util.data.IDataSource;
import com.alant7_.util.event.listeners.EventListenerArmorEquip;
import com.alant7_.util.event.listeners.EventListenerBrewing;
import com.alant7_.util.gui.GUIController;
import com.alant7_.util.holograms.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AlanJavaPlugin extends JavaPlugin {

    private static HashMap<Class<? extends AlanJavaPlugin>, AlanJavaPlugin> plugins = new HashMap<>();

    private IDataSource dataSource;

    private IDataLoader dataLoader;

    private static HashMap<Class<? extends ConfigUtil>, ConfigUtil> configs;

    @Override
    public final void onEnable() {
        plugins.put(getClass(), this);

        HologramManager.registerPlugin(this);

        if (dataLoader != null)
            dataLoader.load();

        Bukkit.getPluginManager().registerEvents(new EventListenerBrewing(this), this);
        Bukkit.getPluginManager().registerEvents(new EventListenerArmorEquip(new ArrayList<>()), this);

        onPluginEnable();
    }

    @Override
    public final void onDisable() {
        GUIController.close();

        HologramManager.getInstance(getClass()).unloadHolograms();

        onPluginDisable();
    }

    public abstract void onPluginEnable();

    public abstract void onPluginDisable();

    public static AlanJavaPlugin getInstance(Class<? extends AlanJavaPlugin> plugin) {
        return plugins.getOrDefault(plugin, null);
    }

    public void createEmptyFile(String path) {
        createEmptyFile(new File(getDataFolder(), path));
    }

    public void createEmptyFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDefaultConfig(String name) {
        try {
            InputStream is = this.getResource(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            FileOutputStream fos = new FileOutputStream(new File(getDataFolder(), name));

            String line;
            while ((line = br.readLine()) != null)
                fos.write((line + System.lineSeparator()).getBytes());

            fos.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataLoader(IDataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public IDataLoader getDataLoader() {
        return dataLoader;
    }

    public IDataSource getDataSource() {
        return dataSource;
    }

}
