package dev.revere.alley.visual.scoreboard.internal.match;

import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public interface MatchScoreboard {
    /**
     * Gets the scoreboard lines for the given profile in a regular solo match.
     *
     * @param profile  The profile to get the scoreboard lines for.
     * @param player   The model whose scoreboard is being displayed.
     * @param you      The model whose scoreboard is being displayed.
     * @param opponent The opponent model.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent);

    /**
     * Gets the corresponding color of the model including the model's name.
     *
     * @param profile The profile of the model.
     * @return The formatted model name with color.
     */
    default String getColoredName(Profile profile) {
        ChatColor nameColor = profile.getNameColor();
        String name = profile.getName();

        if (nameColor != null) {
            return nameColor + name;
        } else {
            return ChatColor.WHITE + name;
        }
    }

    /**
     * Gets the ping of the model by using reflect.
     *
     * @param player The model to get the ping for.
     * @return The ping of the model.
     */
    default int getPing(Player player) {
        if (player == null) {
            return 0;
        }

        return ReflectionUtility.getPing(player);
    }
}