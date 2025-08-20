package dev.revere.alley.feature.hotbar.command.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 26/07/2025
 */
public class HotbarDeleteCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.delete",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/hotbar delete &c<name>"));
            return;
        }

        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        String name = args[0];

        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(CC.translate("&cNo hotbar item found with the name &e" + name + "&c."));
            return;
        }

        hotbarService.deleteHotbarItem(hotbarItem);
        player.sendMessage(CC.translate("&aHotbar item &e" + name + " &adeleted successfully."));
    }
}
