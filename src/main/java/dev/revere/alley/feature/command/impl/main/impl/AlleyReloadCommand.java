package dev.revere.alley.feature.command.impl.main.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class AlleyReloadCommand extends BaseCommand {
    @Override
    @CommandData(name = "practice.reload", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&eReloading &c&lPractice Core&e..."));
        this.plugin.getService(ConfigService.class).reloadConfigs();
        player.sendMessage(CC.translate("&c&lPractice Core &ehas been reloaded."));
        player.sendMessage("");
    }
}