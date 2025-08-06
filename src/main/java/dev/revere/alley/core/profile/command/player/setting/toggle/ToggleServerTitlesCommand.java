package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ProfileLocale;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class ToggleServerTitlesCommand extends BaseCommand {
    @CommandData(name = "toggleservertitles", cooldown = 1)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setServerTitles(!profile.getProfileData().getSettingData().isServerTitles());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_SERVER_TITLES.getMessage().replace("{status}", profile.getProfileData().getSettingData().isServerTitles() ? "&aenabled" : "&cdisabled")));
    }
}
