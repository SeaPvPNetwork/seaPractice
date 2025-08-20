package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ChatChannel;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 12:14
 */
public class ChatCommand extends BaseCommand {
    @CommandData(name = "chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/chat &c<chat-channel>"));
            player.sendMessage(CC.translate("&cAvailable chat channels: " + ChatChannel.getChatChannelsSorted()));
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (ChatChannel.getExactChatChannel(args[0], true) == null) {
            player.sendMessage(CC.translate("&cThe chat channel &c" + args[0] + " &cdoes not exist."));
            return;
        }

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(args[0])) {
            player.sendMessage(CC.translate("&cYou're already in the " + args[0] + " chat channel."));
            return;
        }

        profile.getProfileData().getSettingData().setChatChannel(ChatChannel.getExactChatChannel(args[0], true));
        player.sendMessage(CC.translate("&aSet your chat channel to &c" + args[0] + "&a."));
    }
}