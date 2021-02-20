package com.alant7_.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class EventListenerGUI implements Listener {

    private static EventListenerGUI eventListener = null;

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(
                eventListener == null ? eventListener = new EventListenerGUI() : eventListener,
                plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        AbstractGUI gui = GUIController.getOpenInventory(event.getWhoClicked().getUniqueId());
        if (gui == null) return;

        try {
            gui.interact(event);

            if (gui.isCancelled()) {
                event.setCancelled(true);
                gui.setCancelled(false);
            }
        } catch (Exception e) {
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        AbstractGUI gui = GUIController.getOpenInventory(event.getPlayer().getUniqueId());
        if (gui == null) return;

        gui.onInventoryClose();
        GUIController.disposeInventory(event.getPlayer().getUniqueId());

    }

}
