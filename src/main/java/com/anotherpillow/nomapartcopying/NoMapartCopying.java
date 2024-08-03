package com.anotherpillow.nomapartcopying;

import com.anotherpillow.nomapartcopying.commands.NoMapartCopyingCommand;
import com.anotherpillow.nomapartcopying.events.EventListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;
import java.util.Iterator;

public final class NoMapartCopying extends JavaPlugin {

    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public static FileConfiguration config = null;

    public static String version = "1.1.0";

    public Logger logger = getLogger();


    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new EventListeners(), this);
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

        this.getCommand("nomapartcopying").setExecutor(new NoMapartCopyingCommand());

//        Iterator<Recipe> it = Bukkit.recipeIterator();
//        while (it.hasNext()) {
//            Recipe next = it.next();
//            it.remove();
////            if (next != null && next.getResult() == new ItemStack(Material.FILLED_MAP, 1)) {
////                it.remove();
////                logger.info("Unregistered filled_map recipe");
////            }
//        }
//            Bukkit.getServer().removeRecipe(NamespacedKey.fromString("map_cloning"));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
