package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaDeleteCommand extends BaseCommand {

    @CompleterData(name = "arena.delete")
    public List<String> arenaDeleteCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        ArenaService arenaService = this.plugin.getService(ArenaService.class);


        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            arenaService.getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.delete", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        ArenaService arenaService = this.plugin.getService(ArenaService.class);

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/arena delete &c<arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (arenaService.getArenaByName(arenaName) == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        player.sendMessage(ArenaLocale.DELETED.getMessage().replace("{arena-name}", arenaName));
        arenaService.deleteArena(arenaService.getArenaByName(arenaName));
    }
}
