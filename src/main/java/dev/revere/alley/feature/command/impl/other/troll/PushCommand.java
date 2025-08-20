package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/06/2024 - 20:26
 */
public class PushCommand extends BaseCommand {
    @CommandData(name = "push", permission = "alley.command.troll.push", usage = "push <player> <value>", description = "Push a player")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: &e/push &c<player> <value>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        double value;

        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (value > 10) {
            player.sendMessage(CC.translate("&cValue cannot be greater than 10."));
            return;
        }

        target.setVelocity(player.getLocation().getDirection().multiply(value));

        player.sendMessage(CC.translate("&fYou've pushed &c" + target.getName()));
        target.sendMessage(CC.translate("&fYou've been pushed by &c" + player.getName()));
    }
}