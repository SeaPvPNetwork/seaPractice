package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:46
 */
public class KitSetDescriptionCommand extends BaseCommand {
    @CommandData(name = "kit.description", aliases = "kit.setdesc", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/kit description &c<kitName> <description/clear>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        if (args[1].equalsIgnoreCase("clear")) {
            kit.setDescription("");
            this.plugin.getService(KitService.class).saveKit(kit);
            sender.sendMessage(CC.translate(KitLocale.KIT_DESCRIPTION_CLEARED.getMessage().replace("{kit-name}", kit.getName())));
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDescription(description);
        this.plugin.getService(KitService.class).saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_DESCRIPTION_SET.getMessage().replace("{kit-name}", kit.getName()).replace("{description}", description)));
    }
}