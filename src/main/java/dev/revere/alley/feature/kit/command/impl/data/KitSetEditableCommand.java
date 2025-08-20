package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 04/05/2025
 */
public class KitSetEditableCommand extends BaseCommand {
    @CommandData(name = "kit.seteditable", isAdminOnly = true, usage = "kit seteditable <name> <true/false>", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/kit seteditable &c<name> <true/false>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        boolean editable;
        try {
            editable = Boolean.parseBoolean(args[1]);
        } catch (Exception exception) {
            sender.sendMessage(CC.translate("&cInvalid value for editable! Use true or false."));
            return;
        }

        kit.setEditable(editable);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_SET_EDITABLE.getMessage()
                .replace("{kit-name}", kit.getName())
                .replace("{editable}", String.valueOf(editable))));
    }
}