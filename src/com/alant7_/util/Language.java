package com.alant7_.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Language extends ConfigUtil {

    private static HashMap<AlanJavaPlugin, Language> instances = new HashMap<>();

    private Language(AlanJavaPlugin plugin, String path) {
        super(plugin, path);
    }

    public static void init(AlanJavaPlugin plugin, String path) {
        instances.put(plugin, new Language(plugin, path));
        instances.get(plugin).load();
    }

    public static String get(AlanJavaPlugin plugin, String message) {
        String msg = instances.get(plugin).getConfig().getString(message);
        return msg != null ? ColorFormatter.format(msg) : "";
    }

    public static List<String> getList(AlanJavaPlugin plugin, String message, String[] replace, Object[] replaceWith) {
        List<String> list = instances.get(plugin).getConfig().getStringList(message);
        List<String> r = new ArrayList<>();

        for (String line : list) {
            String formatted = ColorFormatter.format(line);
            for (int i = 0; i < replace.length; i++)
                formatted = formatted.replace(replace[i], replaceWith[i].toString());

            r.add(formatted);
        }

        return r;
    }

    public static List<String> getList(AlanJavaPlugin plugin, String message) {
        return getList(plugin, message, new String[0], new String[0]);
    }

    @Override
    public void onConfigReady(FileConfiguration config) {

    }

}
