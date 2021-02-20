package com.alant7_.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class AbstractGUI {

    private Player player;
    private Inventory inventory;

    private String title;

    private HashMap<Integer, Runnable> leftClickCallbacks = new HashMap<>();
    private HashMap<Integer, Runnable> rightClickCallbacks = new HashMap<>();

    private boolean isDisposed = false;
    private boolean isCancelled = false;

    private boolean isItemInteractionEnabled = true;

    private InventoryClickEvent event;

    public AbstractGUI(Player player) {
        this(player, true);
    }

    public AbstractGUI(Player player, boolean init) {
        this.player = player;
        if (init) {
            init();
        }
    }

    protected abstract void init();

    protected abstract void fill(Inventory inventory);

    public void refill() {
        getInventory().clear();
        fill(getInventory());
    }

    protected void createInventory(String title, int size) {
        this.title = title;
        inventory = Bukkit.createInventory(player, size, title);
    }

    public void open() {
        fill(getInventory());

        getPlayer().openInventory(getInventory());
        GUIController.openGuis.put(player.getUniqueId(), this);

        onInventoryOpen();
    }

    protected abstract void onInventoryOpen();

    protected abstract void onInventoryClose();

    public final void close() {
        getPlayer().closeInventory();
        onInventoryClose();
    }

    public void onDisposed() {}

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public ItemStack getItem(int slot) {
        if (isOutside(slot)) {
            return null;
        }
        if (hasClickEvent()) {
            return event.getInventory().getSize() > slot
                            ? event.getInventory().getItem(slot) : _getItem(slot);
        }
        return inventory.getSize() > slot ? inventory.getItem(slot) : _getItem(slot);
    }

    private ItemStack _getItem(int rawslot) {
        int slot = rawslot - inventory.getSize() + 9;
        if (slot > 35) {
            slot = slot - 36;
        }

        return player.getInventory().getItem(slot);
    }

    public void addLeftClickCallback(int slot, Runnable onItemClicked) {
        leftClickCallbacks.put(slot, onItemClicked);
    }

    public void addRightClickCallback(int slot, Runnable onItemClicked) {
        rightClickCallbacks.put(slot, onItemClicked);
    }

    public void removeLeftClickCallbacks(int slot) {
        leftClickCallbacks.remove(slot);
    }

    public void removeRightClickCallbacks(int slot) {
        rightClickCallbacks.remove(slot);
    }

    public abstract void onLeftClicked(int slot, @Nullable ItemStack stack);

    public abstract void onRightClicked(int slot, @Nullable ItemStack stack);

    public abstract void onOtherClicked(int slot, ClickType type, @Nullable ItemStack stack);

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void leftClick(int slot) {
        onLeftClicked(slot, getItem(slot));

        Runnable r = leftClickCallbacks.getOrDefault(slot, null);
        if (r != null) r.run();
    }

    public void rightClick(int slot) {
        onRightClicked(slot, getItem(slot));

        Runnable r = rightClickCallbacks.getOrDefault(slot, null);
        if (r != null) r.run();
    }

    private void _interact(int slot, ClickType type, InventoryClickEvent event) {
        this.event = event;
        if (!isItemInteractionEnabled() && event != null)
            event.setCancelled(true);
        if (type.isLeftClick()) {
            this.leftClick(slot);
        } else if (type.isRightClick()) {
            this.rightClick(slot);
        } else {
            this.onOtherClicked(slot, type, getItem(slot));
        }
    }

    public void interact(int slot, ClickType type) {
        _interact(slot, type, null);
    }

    public void interact(InventoryClickEvent event) {
        _interact(event.getRawSlot(), event.getClick(), event);
    }

    public void cancel() {
        isCancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean belongsToPlayer(int slot) {
        return slot >= inventory.getSize() && slot < inventory.getSize() + 36;
    }

    public boolean belongsToGUI(int slot) {
        return slot < inventory.getSize();
    }

    public boolean isOutside(int slot) {
        return !belongsToGUI(slot) && !belongsToPlayer(slot);
    }

    public boolean hasClickEvent() {
        return event != null;
    }

    public boolean isItemInteractionEnabled() {
        return isItemInteractionEnabled;
    }

    public void setItemInteractionEnabled(boolean itemInteractionEnabled) {
        isItemInteractionEnabled = itemInteractionEnabled;
    }

    public void clearLeftClickCallbacks() {
        leftClickCallbacks.clear();
    }

    public void clearRightClickCallbacks() {
        rightClickCallbacks.clear();
    }

    public InventoryClickEvent getClickEvent() {
        return event;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractGUI) {
            return ((AbstractGUI) object).inventory.equals(inventory);
        }
        if (object instanceof Inventory) {
            return inventory.equals(object);
        }

        return false;
    }

    public void dispose() {
        leftClickCallbacks.clear();
        rightClickCallbacks.clear();

        player = null;
        inventory = null;

        isDisposed = true;
        onDisposed();
    }

}
