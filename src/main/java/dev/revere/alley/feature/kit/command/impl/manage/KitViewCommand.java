package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.ClickableUtil;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:56
 */
public class KitViewCommand extends BaseCommand {
    @CommandData(name = "kit.view", permission = "practice.admin.view")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/kit view &c<kitName>"));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&c&lKit " + kit.getName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate(" &c● &fDisplay Name: &c" + kit.getDisplayName()));
        player.sendMessage(CC.translate(" &c● &fName: &c" + kit.getName()));
        player.sendMessage(CC.translate(" &c● &fIcon: &c" + kit.getIcon().name().toLowerCase() + " &7(" + kit.getDurability() + ")"));
        player.sendMessage(CC.translate(" &c● &fDisclaimer: &c" + kit.getDisclaimer()));
        player.sendMessage(CC.translate(" &c● &fDescription: &c" + kit.getDescription()));
        player.sendMessage(CC.translate(" &c● &fFFA: &c" + (kit.isFfaEnabled() ? "&aEnabled" : "&cDisabled")));
        player.spigot().sendMessage(ClickableUtil.createComponent(
                "  &a(Click here to view the kit settings)",
                "/kit viewsettings " + kit.getName(),
                "&7Click to view the settings of the kit &c" + kit.getName())
        );
        player.sendMessage("");
    }
}