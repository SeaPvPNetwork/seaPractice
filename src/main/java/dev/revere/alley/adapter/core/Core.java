package dev.revere.alley.adapter.core;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public interface Core {
    /**
     * Retrieves the bootstrap name of the server implementation.
     *
     * @return The bootstrap name as a String.
     */
    CoreType getType();

    /**
     * Retrieves the color associated with a given model.
     *
     * @param player The model whose color is to be retrieved.
     * @return The color as a ChatColor object.
     */
    ChatColor getPlayerColor(Player player);

    /**
     * Retrieves the rank prefix for a given model.
     *
     * @param player The model whose rank prefix is to be retrieved.
     * @return The rank prefix as a String.
     */
    String getRankPrefix(Player player);

    /**
     * Retrieves the rank name for a given model.
     *
     * @param player The model whose rank is to be retrieved.
     * @return The rank name as a String.
     */
    String getRankName(Player player);

    /**
     * Retrieves the rank suffix for a given model.
     *
     * @param player The model whose rank suffix is to be retrieved.
     * @return The rank suffix as a String.
     */
    String getRankSuffix(Player player);

    /**
     * Retrieves the rank color for a given model.
     *
     * @param player The model whose rank color is to be retrieved.
     * @return The rank color as a ChatColor object.
     */
    ChatColor getRankColor(Player player);

    /**
     * Retrieves the tag prefix for a given model.
     *
     * @param player The model whose tag prefix is to be retrieved.
     * @return The tag prefix as a String.
     */
    String getTagPrefix(Player player);

    /**
     * Retrieves the color associated with a given model's tag.
     *
     * @param player The model whose tag color is to be retrieved.
     * @return The tag color as a String.
     */
    ChatColor getTagColor(Player player);

    /**
     * Retrieves the chat format for a given model and message.
     *
     * @param player       The model whose chat format is to be retrieved.
     * @param eventMessage The message to be formatted.
     * @param separator    The separator to be used in the chat format.
     * @return The formatted chat message as a String.
     */
    default String getChatFormat(Player player, String eventMessage, String separator) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        String prefix = CC.translate(this.getRankPrefix(player));
        String suffix = CC.translate(this.getRankSuffix(player));
        String tagPrefix = CC.translate(this.getTagPrefix(player));

        ChatColor nameColor = profile.getNameColor() != null ? profile.getNameColor() : this.getPlayerColor(player);
        ChatColor rankColor = this.getRankColor(player);
        ChatColor tagColor = this.getTagColor(player);

        String selectedTitle = CC.translate(profile.getProfileData().getSelectedTitle());

        if (player.hasPermission("alley.chat.color")) {
            eventMessage = CC.translate(eventMessage);
        }

        return prefix + rankColor + nameColor + player.getName() + suffix + tagColor + tagPrefix + separator + eventMessage + selectedTitle;
    }
}