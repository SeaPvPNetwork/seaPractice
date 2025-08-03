package dev.revere.alley.adapter.placeholder.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.data.ProfileData;
import dev.revere.alley.common.text.CC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Emmy
 * @project Alley
 * @since 21/05/2025
 */
public class AlleyPlaceholderExpansion extends PlaceholderExpansion {

    /*
     * Examples:
     *
     * %alley_division_<kit_name>% | returns the player's division in the specified kit
     * %alley_global-elo% | returns the player's global Elo
     */

    protected final AlleyPlugin plugin;
    protected final String notAvailableString;

    /**
     * Constructor for the AlleyPlaceholderExpansion class.
     *
     * @param plugin The Alley bootstrap instance.
     */
    public AlleyPlaceholderExpansion(AlleyPlugin plugin) {
        this.plugin = plugin;
        this.notAvailableString = "&cN/A";
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.plugin.getService(PluginConstant.class).getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getService(PluginConstant.class).getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileData profileData = profile.getProfileData();

        if (profileData == null) {
            return this.notAvailableString;
        }

        if (params.startsWith("leaderboard-position-")) {
            //String kitName = params.substring("leaderboard-position-".length());
            return this.notAvailableString; // Get Leaderboard entry after implementing the leaderboard system properly
        } else if (params.startsWith("division_")) {
            String kitName = params.substring("division_".length());
            String division = profileData.getUnrankedKitData().get(kitName).getDivision().getName();
            return division == null ? this.notAvailableString : division;
        }

        switch (params.toLowerCase()) {
            case "model-global-elo":
                return String.valueOf(profileData.getElo());
            case "model-unranked-wins":
                return String.valueOf(profileData.getTotalWins());
            case "model-unranked-losses":
                return String.valueOf(profileData.getTotalLosses());
            case "model-ranked-wins":
                return String.valueOf(profileData.getRankedWins());
            case "model-ranked-losses":
                return String.valueOf(profileData.getRankedLosses());
            case "model-level":
                return Objects.requireNonNull(CC.translate(this.plugin.getService(LevelService.class).getLevel(profileData.getElo()).getDisplayName()), this.notAvailableString);
            case "model-coins":
                return String.valueOf(profileData.getCoins());
        }

        return null;
    }
}