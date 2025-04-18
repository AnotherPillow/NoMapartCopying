package com.anotherpillow.nomapartcopying.events;

import com.anotherpillow.nomapartcopying.Constants;
import com.anotherpillow.nomapartcopying.NoMapartCopying;
import com.anotherpillow.nomapartcopying.util.OwnershipVerification;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import com.anotherpillow.nomapartcopying.NoMapartCopying;

import java.util.*;

public class EventListeners implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onItemCraft(PrepareItemCraftEvent event) {
        ItemStack map = null;
        boolean hasGlassPane = false;
        boolean hasEmptyMap = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;
            if (item.getType() == Material.FILLED_MAP) {
                map = item.clone();
            } else if (item.getType() == Material.getMaterial(NoMapartCopying.config.getString("config.locker-item"))) {
                hasGlassPane = true;
            } else if (item.getType() == Material.MAP) {
                hasEmptyMap = true;
            }
        }

        if (map == null) return;

        List<HumanEntity> viewers = event.getViewers();
        String player = null;
        String playerUUID = null;
        if (!viewers.isEmpty()) {
            HumanEntity user = viewers.get(0);
            player = user.getName();
            playerUUID = user.getUniqueId().toString();
        }
        if (playerUUID == null) return; // if somehow there is not a player, that is bad.

        ItemMeta meta = map.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        String mapExistingAuthor = container.get(Constants.ownerKey, PersistentDataType.STRING);
        @Nullable Integer isExistingLocked = container.get(Constants.lockedKey, PersistentDataType.INTEGER);

        if (hasGlassPane) {
            meta.setLore(List.of(new String[]{
                    "Copying prevented by " + player
            }));

            container.set(Constants.ownerKey, PersistentDataType.STRING, playerUUID);
            container.set(Constants.lockedKey, PersistentDataType.INTEGER, 1);

            if (NoMapartCopying.config.getBoolean("config.rename-item"))
                meta.displayName(meta.hasDisplayName()
                        ? Objects.requireNonNull(meta.displayName()).color(NamedTextColor.DARK_RED)
                        : Component.text("Map", NamedTextColor.DARK_RED));

            map.setItemMeta(meta);

            event.getInventory().setResult(map);
        } else if (hasEmptyMap && OwnershipVerification.ShouldPreventCopy(map, playerUUID)) { // if a copying is attempted,  and the player is NOT the locker of the map
            event.getInventory().setResult(null);
        } else if (hasEmptyMap && !OwnershipVerification.ShouldPreventCopy(map, playerUUID)) { // if a copying is attempted, and the author on the map is either nobody (unlocked) or it's the author trying to copy it
            map.setAmount(2);
            event.getInventory().setResult(map);
        }
    }

    private void inventoryCheck(InventoryEvent event) {
        if (!NoMapartCopying.config.getBoolean("config.close-cartography-attempted-use")) return; // if closing cartography is disabled, ignore rest
        HumanEntity human = ((InventoryClickEvent)event).getWhoClicked(); // even though not always castable, .getWhoClicked() always exists
        String playerUUID = human.getUniqueId().toString();

        if (event.getInventory().getType() == InventoryType.CARTOGRAPHY) {
            CartographyInventory inventory = (CartographyInventory) event.getInventory();

            if (inventory.getSize() < 2) {
                return;
            }

            ItemStack item1 = inventory.getItem(0);
            ItemStack item2 = inventory.getItem(1);

            if (item1 == null) return; // can't copy if you didn't give a map

            if (playerUUID == null && item1.getType() == Material.FILLED_MAP) {
                inventory.close();
                return;
            }

            if (OwnershipVerification.ShouldPreventCopy(item1, playerUUID)) {
                // based on the input map, should prevent copy and the second item is either empty or an empty map.
                inventory.close();
                return;
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        this.inventoryCheck(event);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryInteract(InventoryInteractEvent event) {
        this.inventoryCheck(event);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event) {
        this.inventoryCheck(event);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (event.getSource().getType() == InventoryType.CARTOGRAPHY) event.setCancelled(true);
    }
}
