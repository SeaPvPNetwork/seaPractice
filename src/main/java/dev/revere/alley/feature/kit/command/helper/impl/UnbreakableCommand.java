package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import dev.revere.alley.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class UnbreakableCommand extends BaseCommand {
    @CommandData(
            name = "unbreakable",
            description = "Set the unbreakable state of the item in your hand",
            usage = "unbreakable <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/unbreakable &c<true|false>"));
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to modify unbreakable state."));
            return;
        }

        boolean unbreakable = Boolean.parseBoolean(args[0]);

        ItemStack unbreakAbleItem = ReflectionUtility.setUnbreakable(item, unbreakable);
        player.setItemInHand(unbreakAbleItem);

        player.sendMessage(CC.translate("&aSuccessfully set the unbreakable state of the item to &c" + unbreakable + "&a."));
    }
}