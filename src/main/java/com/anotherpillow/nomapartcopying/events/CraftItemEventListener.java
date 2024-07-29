package com.anotherpillow.nomapartcopying.events;

import com.anotherpillow.nomapartcopying.NoMapartCopying;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class CraftItemEventListener implements Listener {
    public static final NamespacedKey ownerKey = new NamespacedKey(NoMapartCopying.getPlugin(NoMapartCopying.class), "map-owner");
    public static final NamespacedKey lockedKey = new NamespacedKey(NoMapartCopying.getPlugin(NoMapartCopying.class), "is-copy-locked");

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemCraft(PrepareItemCraftEvent event) {

        ItemStack map = null;
        boolean hasGlassPane = false;
        boolean hasEmptyMap = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item == null) continue;
            if (item.getType() == Material.FILLED_MAP) {
                System.out.println("Found the filled map!");
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


        ItemMeta meta = map.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        String mapExistingAuthor = container.get(ownerKey, PersistentDataType.STRING);
        @Nullable Integer isExistingLocked = container.get(lockedKey, PersistentDataType.INTEGER);

        if (hasGlassPane) {
            meta.setLore(List.of(new String[]{
                    "Copying prevented by " + player
            }));

            container.set(ownerKey, PersistentDataType.STRING, playerUUID);
            container.set(lockedKey, PersistentDataType.INTEGER, 1);

            if (NoMapartCopying.config.getBoolean("config.rename-item"))
                meta.displayName(meta.hasDisplayName()
                        ? Objects.requireNonNull(meta.displayName()).color(NamedTextColor.DARK_RED)
                        : Component.text("Map", NamedTextColor.DARK_RED));

            map.setItemMeta(meta);

            event.getInventory().setResult(map);
        } else if (hasEmptyMap && !Objects .equals(mapExistingAuthor, playerUUID)) { // if a copying is attempted,  and the player is NOT the locker of the map
            event.getInventory().setResult(null);
        } else if (hasEmptyMap && (isExistingLocked != 1 /*unlocked*/ || Objects.equals(mapExistingAuthor, playerUUID))) { // if a copying is attempted, and the author on the map is either nobody (unlocked) or it's the author trying to copy it
            map.setAmount(2);
            event.getInventory().setResult(map);
        }
    }
}
