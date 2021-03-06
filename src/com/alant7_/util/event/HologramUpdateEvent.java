package com.alant7_.util.event;

import com.alant7_.util.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class HologramDisplayEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();

    private Player player;

    private Hologram hologram;

    private List<String> lines;

    private boolean isCancelled = false;

    public HologramDisplayEvent (Player player, Hologram hologram) {

    }

    public Player getPlayer() {
        return player;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
