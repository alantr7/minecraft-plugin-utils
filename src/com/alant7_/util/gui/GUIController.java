package com.alant7_.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GUIController {

    static HashMap<UUID, AbstractGUI> openGuis = new HashMap<>();

    public static AbstractGUI getOpenInventory(Player player) {
        return getOpenInventory(player.getUniqueId());
    }

    public static AbstractGUI getOpenInventory(UUID uuid) {
        return openGuis.getOrDefault(uuid, null);
    }

    public static void disposeInventory(Player player) {
        disposeInventory(player.getUniqueId());
    }

    public static void disposeInventory(UUID uuid) {
        openGuis.remove(uuid).dispose();
    }

    public static List<Player> getPlayersWithGUI(Class<? extends AbstractGUI> guis) {

        List<Player> players = new ArrayList<>();

        openGuis.forEach((uuid, gui) -> {
            if (gui.getClass().equals(guis)) {

                Player player = Bukkit.getPlayer(uuid);
                if (player != null)
                    players.add(player);

            }
        });

        return players;

    }

    public static void close() {
        for (UUID uuid : openGuis.keySet()) {
            AbstractGUI gui = openGuis.get(uuid);
            gui.close();
        }

        openGuis.clear();
    }

}
