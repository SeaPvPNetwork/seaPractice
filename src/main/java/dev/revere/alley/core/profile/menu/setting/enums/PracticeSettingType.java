package dev.revere.alley.core.profile.menu.setting.enums;

import dev.revere.alley.core.profile.data.types.ProfileSettingData;
import dev.revere.alley.common.text.LoreHelper;
import dev.revere.alley.common.text.CC;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
public enum PracticeSettingType {
    PARTY_MESSAGES(10, "&c&lToggle Party Messages", Material.FEATHER,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7See party chat messages.",
                    "",
                    LoreHelper.displayEnabled(settings.isPartyMessagesEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    PARTY_INVITES(11, "&c&lToggle Party Invites", Material.NAME_TAG,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Receive party invites.",
                    "",
                    LoreHelper.displayEnabled(settings.isPartyInvitesEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    SIDEBAR_VISIBILITY(12, "&c&lSidebar Visibility", Material.CARPET, 5,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7See the scoreboard.",
                    "",
                    LoreHelper.displayShown(settings.isScoreboardEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    TAB_VISIBILITY(13, "&c&lTablist Visibility", Material.ITEM_FRAME,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7See the tablist.",
                    "",
                    LoreHelper.displayShown(settings.isTablistEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    WORLD_TIME(14, "&c&lWorld time", Material.WATCH, settings -> Arrays.asList(
            CC.MENU_BAR,
            "&7Change your world time.",
            "",
            formatTime("Default", settings.isDefaultTime(), "&a&l"),
            formatTime("Day", settings.isDayTime(), "&e&l"),
            formatTime("Sunset", settings.isSunsetTime(), "&c&l"),
            formatTime("Night", settings.isNightTime(), "&4&l"),
            "",
            "&aClick to toggle.",
            CC.MENU_BAR
    )),

    SCOREBOARD_LINES(19, "&c&lShow Scoreboard Lines", Material.STRING,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Show scoreboard lines.",
                    "",
                    LoreHelper.displayShown(settings.isShowScoreboardLines()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    PROFANITY_FILTER(20, "&c&lProfanity Filter", Material.ROTTEN_FLESH,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Hide rude and offensive words.",
                    "",
                    LoreHelper.displayEnabled(settings.isProfanityFilterEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    DUEL_REQUESTS(21, "&c&lDuel Requests", Material.DIAMOND_SWORD,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Receive duel requests.",
                    "",
                    LoreHelper.displayEnabled(settings.isReceiveDuelRequestsEnabled()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),
    SERVER_TITLES(22, "&c&lServer Titles", Material.PAPER,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Display titles sent by the server.",
                    "",
                    LoreHelper.displayShown(settings.isServerTitles()),
                    "",
                    "&aClick to toggle.",
                    CC.MENU_BAR
            )
    ),

    MATCH_SETTINGS(16, "&c&lMatch Settings", Material.BOOK,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Adjust your match settings.",
                    "",
                    "&aClick to view.",
                    CC.MENU_BAR
            )
    ),

    COSMETICS(25, "&c&lCosmetics", Material.NETHER_STAR,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Customize your cosmetics.",
                    "",
                    "&aClick to view.",
                    CC.MENU_BAR
            )
    ),

    LOBBY_MUSIC(34, "&c&lLobby Music", Material.JUKEBOX,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Customize your lobby music.",
                    "",
                    "&aClick to view.",
                    CC.MENU_BAR
            )
    ),

    ;

    public final int slot;
    public final String displayName;
    public final Material material;
    public final int durability;
    public final Function<ProfileSettingData, List<String>> loreProvider;

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    PracticeSettingType(int slot, String displayName, Material material, Function<ProfileSettingData, List<String>> loreProvider) {
        this(slot, displayName, material, 0, loreProvider);
    }

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param durability   The durability of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    PracticeSettingType(int slot, String displayName, Material material, int durability, Function<ProfileSettingData, List<String>> loreProvider) {
        this.slot = slot;
        this.displayName = displayName;
        this.material = material;
        this.durability = durability;
        this.loreProvider = loreProvider;
    }

    /**
     * Formats the time string based on the active status.
     *
     * @param label       The label to display.
     * @param active      Whether the time is active or not.
     * @param activeColor The color for the active state.
     * @return The formatted time string.
     */
    private static String formatTime(String label, boolean active, String activeColor) {
        return " &f● " + (active ? activeColor : "&7") + label;
    }
}
