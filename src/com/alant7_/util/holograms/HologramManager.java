package com.alant7_.util.holograms;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.event.HologramCreateEvent;
import com.alant7_.util.event.HologramDeleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class HologramManager {

    private static Map<Class<? extends AlanJavaPlugin>, HologramManager> hologramManagerMap = new HashMap<>();

    private AlanJavaPlugin plugin;

    private Map<Integer, Hologram> holograms = new HashMap<>();

    public HologramManager (AlanJavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Hologram createHologram(Location location) {

        Optional<Integer> maxOptional  = holograms.keySet().stream().max(Integer::compareTo);

        int max = maxOptional.orElse(0);

        Hologram hologram = new Hologram(max + 1, location);

        hologram.setPlugin(plugin);

        holograms.put(max + 1, hologram);

        hologram.save();

        HologramCreateEvent event = new HologramCreateEvent(hologram);

        Bukkit.getPluginManager().callEvent(event);

        return hologram;

    }

    public void removeHologram(int id) {

        if (!holograms.containsKey(id)) {
            return;
        }

        Hologram hologram = holograms.remove(id);

        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File file = new File(dir, id + ".yml");
        file.delete();

        HologramDeleteEvent event = new HologramDeleteEvent(hologram);

        Bukkit.getPluginManager().callEvent(event);

    }

    public void removeHologram(Hologram hologram) {
        removeHologram(hologram.getId());
    }

    public Hologram getHologram(int id) {
        return id < holograms.size() ? holograms.get(id) : null;
    }

    void saveHologram(Hologram hologram) {
        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File holoFile = new File(dir, hologram.getId() + ".yml");
        plugin.createEmptyFile(holoFile);

        FileConfiguration config = YamlConfiguration.loadConfiguration(holoFile);
        config.set("hologram", hologram);

        try {
            config.save(holoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHolograms() {
        holograms.forEach((id, holo) -> saveHologram(holo));
    }

    public void loadHolograms() {

        holograms.clear();

        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                Hologram hologram = config.getObject("hologram", Hologram.class);

                if (hologram != null) {
                    hologram.setPlugin(plugin);

                    holograms.put(hologram.getId(), hologram);
                }

            }
        }

    }

    public void unloadHolograms() {

        saveHolograms();

        holograms.clear();

    }

    public static HologramManager getInstance(Class<? extends AlanJavaPlugin> plugin) {
        return hologramManagerMap.getOrDefault(plugin, null);
    }

    public static void registerPlugin(AlanJavaPlugin plugin) {
        if (hologramManagerMap.containsKey(plugin.getClass()))
            return;

        hologramManagerMap.put(plugin.getClass(), new HologramManager(plugin));

        Bukkit.broadcastMessage("REGISTERED " + plugin.getName() + "! HOLOGRAM MANAGER!");
    }

}
