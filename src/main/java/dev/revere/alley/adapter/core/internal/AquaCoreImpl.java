package dev.revere.alley.adapter.core.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.adapter.core.Core;
import dev.revere.alley.adapter.core.CoreType;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class AquaCoreImpl implements Core {
    protected final AlleyPlugin plugin;
    protected final AquaCoreAPI aquaCoreAPI;

    /**
     * Constructor for the AquaCoreImpl class.
     *
     * @param aquaCoreAPI The AquaCoreAPI instance to use.
     * @param plugin      The Alley bootstrap instance.
     */
    public AquaCoreImpl(AquaCoreAPI aquaCoreAPI, AlleyPlugin plugin) {
        this.aquaCoreAPI = aquaCoreAPI;
        this.plugin = plugin;
    }

    @Override
    public CoreType getType() {
        return CoreType.AQUA;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return this.aquaCoreAPI.getPlayerNameColor(player.getUniqueId());
    }

    @Override
    public String getRankPrefix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getPrefix();
    }

    @Override
    public String getRankSuffix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getSuffix();
    }

    @Override
    public String getRankName(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getName();
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getColor();
    }

    @Override
    public String getTagPrefix(Player player) {
        return this.aquaCoreAPI.getTag(player.getUniqueId()).getPrefix();
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return this.aquaCoreAPI.getTag(player.getUniqueId()).getColor();
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        String prefix = CC.translate(this.getRankPrefix(player));
        String suffix = CC.translate(this.getRankSuffix(player));
        ChatColor nameColor = profile.getNameColor() != null ? profile.getNameColor() : this.getPlayerColor(player);

        String selectedTitle = CC.translate(AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getSelectedTitle());

        if (player.hasPermission("practice.chat.color")) {
            eventMessage = CC.translate(eventMessage);
        }

        return prefix + nameColor + player.getName() + suffix + this.aquaCoreAPI.getTagFormat(player) + separator + eventMessage + selectedTitle;
    }
}
