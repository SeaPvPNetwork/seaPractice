package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.menu.music.MusicDiscSelectorMenu;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class LobbyMusicCommand extends BaseCommand {
    @CommandData(
            name = "lobbymusic",
            aliases = {"music"},
            permission = "practice.donator.command.lobbymusic"
    )
    @Override
    public void onCommand(CommandArgs command) {
        new MusicDiscSelectorMenu().openMenu(command.getPlayer());
    }
}