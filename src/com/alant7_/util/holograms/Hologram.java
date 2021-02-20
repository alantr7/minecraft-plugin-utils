package com.alant7_.util.holograms;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.ColorFormatter;
import com.alant7_.util.nms.NMSUtil;
import com.alant7_.util.reflections.Pair;
import com.alant7_.util.reflections.ReflectionsUtil;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram implements ConfigurationSerializable {

    private List<String> lines = new ArrayList<>();

    private List<EntityArmorStand> stands = new ArrayList<>();

    private boolean isVisible = true;

    private Location location;

    private int id;

    private AlanJavaPlugin plugin;

    Hologram (int id, Location location) {
        this.id = id;
        this.location = location;
    }

    public AlanJavaPlugin getPlugin() {
        return plugin;
    }

    final void setPlugin(AlanJavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addLine(String line) {
        lines.add(line);
        save();

        reshow();
    }

    public void removeLine(int index) {
        lines.remove(index);
        save();

        reshow();
    }

    public void setLine(int index, String text) {
        if (lines.size() < index)
            return;

        lines.set(index, text);
        save();

        reshow();
    }

    public void clearLines() {
        lines.clear();
    }

    public int getLinesCount() {
        return lines.size();
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }

    public void setVisibleFor(Player player, boolean b) {

    }

    public void setVisible(boolean b) {
        isVisible = b;
    }


    public void update() {
        if (lines.size() != stands.size())
            reshow();
    }

    public void reshow() {

        if (stands.size() > lines.size()) {
            for (int i = lines.size(); i < stands.size(); i++) {
                deleteArmorStand(stands.get(i));
            }

            stands = stands.subList(0, lines.size());
        }

        for (int i = 0; i < lines.size(); i++) {

            EntityArmorStand stand;

            if (i < stands.size()) {
                stand = stands.get(i);
            } else {
                stand = createArmorStand();
            }

            stand.setLocation(location.getX(), location.getY() - 0.6 * i, location.getZ(), 0f, 0f);

            stand.setCustomName(new ChatComponentText(lines.get(i)));

            Bukkit.getOnlinePlayers().forEach((player) -> {

                setVisibleFor(player, stand, false);

                setVisibleFor(player, stand, true);

            });

        }

    }

    private EntityArmorStand createArmorStand() {

        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), 0, 0, 0);

        stand.setCustomNameVisible(true);

        stand.setNoGravity(true);

        stand.setInvisible(true);

        stand.setInvulnerable(true);

        return stand;

    }

    private void deleteArmorStand(EntityArmorStand stand) {
        Bukkit.getOnlinePlayers().forEach((player) -> setVisibleFor(player, stand, false));
    }

    private void setVisibleFor(Player player, EntityArmorStand stand, boolean b) {

        if (b) {

            PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(stand);

            PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet1);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);

        } else {

            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        }

    }

    public void save() {
        HologramManager.getInstance(plugin.getClass()).saveHologram(this);
    }

    public void remove() {
        HologramManager.getInstance(plugin.getClass()).removeHologram(this);
    }

    public int getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        map.put("id", id);

        map.put("lines", lines);

        map.put("location", location);

        return map;

    }

    public static Hologram deserialize(Map<String, Object> map) {

        List<String> lines = (List<String>) map.get("lines");

        Hologram hologram = new Hologram((int) map.get("id"), (Location) map.get("location"));

        lines.forEach(hologram::addLine);

        return hologram;

    }

    public static Hologram valueOf(Map<String, Object> map) {
        return deserialize(map);
    }

}
