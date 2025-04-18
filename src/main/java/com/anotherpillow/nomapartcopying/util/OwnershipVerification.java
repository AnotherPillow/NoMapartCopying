package com.anotherpillow.nomapartcopying.util;

import com.anotherpillow.nomapartcopying.Constants;
import com.anotherpillow.nomapartcopying.NoMapartCopying;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class OwnershipVerification {

    public static boolean ShouldPreventCopy(ItemStack map, String playerUUID) {
        if (map == null) return true; // fail closed.
        ItemMeta meta = map.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        String mapExistingAuthor = container.get(Constants.ownerKey, PersistentDataType.STRING);
        @Nullable Integer isExistingLocked = container.get(Constants.lockedKey, PersistentDataType.INTEGER);

        if (isExistingLocked == null || isExistingLocked != 1) return false; // if nobody locked it, it therefore must be unlocked
        if (Objects.equals(mapExistingAuthor, playerUUID)) return false; // if the map's author and the player uuid are the same, the author is allowed to copy

        return true;
    }
}
