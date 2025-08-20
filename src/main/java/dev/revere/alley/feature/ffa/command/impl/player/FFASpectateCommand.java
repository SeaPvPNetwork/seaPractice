package dev.revere.alley.feature.ffa.command.impl.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class FFASpectateCommand extends BaseCommand {
    @CommandData(name = "ffa.spectate", usage = "/spectate <ffaKit>", aliases = {"specffa", "spectateffa"}, description = "Spectate a FFA match")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/spectateffa &c<ffaKit>"));
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot do this right now!"));
            return;
        }

        String ffaKitName = args[0];
        FFAService ffaService = this.plugin.getService(FFAService.class);
        Kit ffaKit = ffaService.getFfaKits().stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(ffaKitName))
                .findFirst()
                .orElse(null);

        if (ffaKit == null) {
            player.sendMessage(CC.translate("&cFFA kit not found: " + ffaKitName));
            return;
        }

        if (!ffaKit.isFfaEnabled()) {
            //should never happen, but just in case
            player.sendMessage(CC.translate("&cFFA kit is not enabled: " + ffaKitName));
            return;
        }

        FFAMatch match = ffaService.getMatches().stream()
                .filter(m -> m.getKit().equals(ffaKit))
                .findFirst()
                .orElse(null);

        if (match == null) {
            player.sendMessage(CC.translate("&cNo active FFA match found for kit: " + ffaKitName));
            return;
        }

        if (match.getSpectators().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are already spectating this FFA match."));
            return;
        }

        match.addSpectator(player);
    }
}