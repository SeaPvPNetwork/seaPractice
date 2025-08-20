package dev.revere.alley.feature.kit.command.impl.settings;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.KitSettingService;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class KitSetSettingCommand extends BaseCommand {
    @CommandData(name = "kit.setsetting", aliases = {"kit.setting"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: &e/kit setsetting &c<kit> <setting> <true/false>"));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        String settingName = args[1];
        boolean enabled = Boolean.parseBoolean(args[2]);

        if (this.plugin.getService(KitSettingService.class).getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null) == null) {
            player.sendMessage(CC.translate("&cA setting with that name does not exist."));
            return;
        }

        kit.getKitSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().ifPresent(setting -> setting.setEnabled(enabled));
        this.plugin.getService(KitService.class).saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_SETTING_SET.getMessage()).replace("{setting-name}", settingName).replace("{enabled}", String.valueOf(enabled)).replace("{kit-name}", kit.getName()));
    }
}