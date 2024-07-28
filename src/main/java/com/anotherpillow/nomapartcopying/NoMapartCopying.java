package com.anotherpillow.nomapartcopying;

import com.anotherpillow.nomapartcopying.events.CraftItemEventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class NoMapartCopying extends JavaPlugin {

    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public static FileConfiguration config = null;

    public String version = "1.0.1";

    public Logger logger = getLogger();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new CraftItemEventListener(), this);
        // Plugin startup logic
        ItemStack item = new ItemStack(Material.FILLED_MAP);

        ItemMeta meta = item.getItemMeta();

        if (config.getBoolean("config.rename-item"))
            meta.setDisplayName(ChatColor.DARK_RED + (meta.hasDisplayName() ? meta.displayName() : "Map").toString());

        item.setItemMeta(meta);


        NamespacedKey key = new NamespacedKey(this, "lock_filled_map");

        ShapelessRecipe recipe = new ShapelessRecipe(key, item);

        recipe.addIngredient(Material.FILLED_MAP);
        recipe.addIngredient(Material.getMaterial(config.getString("config.locker-item")));

        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
