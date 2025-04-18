package com.anotherpillow.nomapartcopying;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class Constants {
    public static final NamespacedKey ownerKey = new NamespacedKey(NoMapartCopying.getPlugin(NoMapartCopying.class), "map-owner");
    public static final NamespacedKey lockedKey = new NamespacedKey(NoMapartCopying.getPlugin(NoMapartCopying.class), "is-copy-locked");

    public static String VERSION = "1.4.0";
}
