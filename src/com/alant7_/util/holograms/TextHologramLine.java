package com.alant7_.util.holograms;

import com.alant7_.util.ColorFormatter;
import com.alant7_.util.event.HologramUpdateEvent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class AbstractHologramLine {

    private EntityArmorStand stand;

    private Hologram hologram;

    private String text;

    public AbstractHologramLine(String text, Hologram hologram, Location location) {
        this.text = text;
        this.hologram = hologram;

        this.stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        this.stand.setCustomNameVisible(true);
        this.stand.setNoGravity(true);
        this.stand.setInvisible(true);

        if (hologram.isVisible())
            Bukkit.getOnlinePlayers().forEach(this::_showTo);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        getHologram()._update();
    }

    public Hologram getHologram() {
        return hologram;
    }

    EntityArmorStand _getStand() {
        return stand;
    }

    void _setLocation(Location location) {
        stand.setLocation(location.getX(), location.getY(), location.getZ(), 0f, 0f);
        hologram._update();
    }

    void _remove() {
        Bukkit.getOnlinePlayers().forEach(this::_hideFrom);
        this.hologram = null;
        this.text = null;
        this.stand = null;
    }

    void _showTo(Player player) {

        PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(stand);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet1);

    }

    void _hideFrom(Player player) {

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

    void _update(Player player, String text) {

        stand.setCustomName(new ChatComponentText(ColorFormatter.format(text)));

        PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);

    }

}
