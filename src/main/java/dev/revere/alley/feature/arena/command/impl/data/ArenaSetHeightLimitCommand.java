package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/03/2025
 */
public class ArenaSetHeightLimitCommand extends BaseCommand {
    @CommandData(name = "arena.setheightlimit", aliases = {"arena.limit", "arena.height"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: &e/arena setheightlimit &c<arenaName> <heightLimit>"));
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.STANDALONE) {
            player.sendMessage(CC.translate("&cYou can only set the height limit for standalone arenas!"));
            return;
        }

        int heightLimit;
        try {
            heightLimit = Integer.parseInt(args[1]);
            if (heightLimit < 0 || heightLimit > 256) {
                player.sendMessage(CC.translate("&cHeight limit must be between 0 and 256!"));
                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid height limit! Please enter a valid number."));
            return;
        }

        ((StandAloneArena) arena).setHeightLimit(heightLimit);
        arenaService.saveArena(arena);

        player.sendMessage(ArenaLocale.HEIGHT_LIMIT_SET.getMessage().replace("{arena-name}", arena.getName()).replace("{height-limit}", String.valueOf(heightLimit)));
    }
}