package com.anotherpillow.nomapartcopying.commands;

import com.anotherpillow.nomapartcopying.NoMapartCopying;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class NoMapartCopyingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Component response = Component.text(String.format("NoMapartCopying v%s", NoMapartCopying.version))
                .color(NamedTextColor.DARK_PURPLE)
                .append(Component.text("\nAuthor: ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("AnotherPillow (https://github.com/AnotherPillow)").color(TextColor.color(0xE377FF)))
                .append(Component.text("\nSource: ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("https://github.com/AnotherPillow/NoMapartCopying").color(TextColor.color(0xE377FF)));
        sender.sendMessage(response);

        return true;
    }
}
