package com.anotherpillow.nomapartcopying.events;

import com.anotherpillow.nomapartcopying.NoMapartCopying;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class CraftItemEventListener implements Listener {

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
        String author = "???";
        if (!viewers.isEmpty()) author = viewers.get(0).getName();


        if (hasGlassPane) {
            ItemMeta meta = map.getItemMeta();

            meta.setLore(List.of(new String[]{
                    "Copying prevented by " + author
            }));

            if (NoMapartCopying.config.getBoolean("config.rename-item"))
                meta.displayName(meta.hasDisplayName()
                        ? Objects.requireNonNull(meta.displayName()).color(NamedTextColor.DARK_RED)
                        : Component.text("Map", NamedTextColor.DARK_RED));

            map.setItemMeta(meta);

            event.getInventory().setResult(map);
        } else if (hasEmptyMap && map.lore() != null) { // if a copying is attempted, and there's lore
            event.getInventory().setResult(null);
        } else if (hasEmptyMap && map.lore() == null) {
            map.setAmount(2);
            event.getInventory().setResult(map);
        }
    }
}
