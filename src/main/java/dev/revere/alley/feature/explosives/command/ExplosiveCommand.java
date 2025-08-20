package dev.revere.alley.feature.explosives.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.explosives.ExplosiveService;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @since 24/06/2025
 */
public class ExplosiveCommand extends BaseCommand {
    @CommandData(name = "explosive", aliases = {"expl"}, isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length != 2) {
            sendHelpMessage(sender);
            return;
        }

        String settingName = args[0].toLowerCase();
        String valueStr = args[1];
        double value;

        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid value. '" + valueStr + "' is not a valid number."));
            return;
        }

        ExplosiveService explosiveService = this.plugin.getService(ExplosiveService.class);

        switch (settingName) {
            case "horizontal":
                explosiveService.setHorizontal(value);
                break;
            case "vertical":
                explosiveService.setVertical(value);
                break;
            case "range":
                explosiveService.setRange(value);
                break;
            case "speed":
                explosiveService.setSpeed(value);
                break;
            case "fuse":
                explosiveService.setTntFuseTicks((int) value);
                break;
            case "explosion":
                explosiveService.setExplosionRange(value);
                break;
            default:
                sendHelpMessage(sender);
                return;
        }

        explosiveService.save();

        sender.sendMessage(CC.translate("&aSuccessfully set the explosive " + settingName + " value to &c" + value + "&a."));
    }

    private void sendHelpMessage(CommandSender sender) {
        List<String> helpMessage = Arrays.asList(
                "",
                "&c&lExplosive Commands Help:",
                " &f● &c/explosive explosion <value> &8- &7Set range of explosion blocks removal.",
                " &f● &c/explosive range <value> &8- &7Set explosion range that affects players.",
                " &f● &c/explosive horizontal <value> &8- &7Set horizontal knockback.",
                " &f● &c/explosive vertical <value> &8- &7Set vertical knockback.",
                " &f● &c/explosive speed <value> &8- &7Set fireball launch speed.",
                " &f● &c/explosive fuse <value> &8- &7Set TNT fuse ticks.",
                ""
        );

        helpMessage.forEach(line -> sender.sendMessage(CC.translate(line)));
    }
}