package dev.revere.alley.core.profile.command.admin.ranked;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
public class RankedCommand extends BaseCommand {
    @CommandData(name = "ranked", isAdminOnly = true, description = "Manage ranked allowance.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                " ",
                "&c&lRanked Commands Help:",
                " &f● &c/ranked ban &8(&7player&8) &7| Ban a player from ranked matches.",
                " &f● &c/ranked unban &8(&7player&8) &7| Unban a player from ranked matches.",
                " "
        ).forEach(message -> player.sendMessage(CC.translate(message)));
    }
}