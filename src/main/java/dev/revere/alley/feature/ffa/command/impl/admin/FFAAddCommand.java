package dev.revere.alley.feature.ffa.command.impl.admin;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.ffa.internal.DefaultFFAMatch;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class FFAAddCommand extends BaseCommand {
    @CommandData(name = "ffa.add", isAdminOnly = true, usage = "/ffa add <player> <kit>", description = "Add a player to an FFA match", aliases = {"ffa.addplayer", "ffa.addp"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: &e/ffa add &c<player> <kit>"));
            return;
        }

        String targetName = args[0];
        FFAService ffaService = this.plugin.getService(FFAService.class);
        FFAMatch match = ffaService.getMatches().stream()
                .filter(m -> m.getKit().getName().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (match == null) {
            player.sendMessage(CC.translate("&cNo FFA match found for kit: " + args[1]));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found: " + targetName));
            return;
        }

        if (match.getPlayers().size() >= match.getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThe FFA match is full!"));
            return;
        }

        DefaultFFAMatch defaultMatch = (DefaultFFAMatch) match;
        defaultMatch.forceJoin(targetPlayer);
        player.sendMessage(CC.translate("&aSuccessfully added " + targetName + " to the FFA match for kit: " + args[1]));
    }
}