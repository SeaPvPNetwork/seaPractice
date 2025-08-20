package dev.revere.alley.core.profile.menu.statistic.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.data.types.ProfileFFAData;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@RequiredArgsConstructor
public class GlobalStatButton extends Button {
    private final OfflinePlayer target;

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId());

        CoreAdapter coreAdapter = AlleyPlugin.getInstance().getService(CoreAdapter.class);
        LevelService levelService = AlleyPlugin.getInstance().getService(LevelService.class);

        int ffaKills = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getKills)
                .sum();
        int ffaDeaths = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getDeaths)
                .sum();

        return new ItemBuilder(Material.SKULL_ITEM)
                .setSkull(target.getName())
                .name("&c&l" + target.getName() + " &r&7┃ &fStats")
                .lore(
                        CC.MENU_BAR,
                        "&7Showing global data.",
                        "",
                        "&c&lUnranked",
                        "&c│ &fWins: &c" + profile.getProfileData().getUnrankedWins(),
                        "&c│ &fLosses: &c" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&c&lRanked",
                        "&c│ &fWins: &c" + profile.getProfileData().getRankedWins(),
                        "&c│ &fLosses: &c" + profile.getProfileData().getRankedLosses(),
                        "&c│ &fElo: &c" + profile.getProfileData().getElo(),
                        "",
                        "&c&lFFA",
                        "&c│ &fKills: &c" + ffaKills,
                        "&c│ &fDeaths: &c" + ffaDeaths,
                        "",
                        "&c&lAccount",
                        "&c│ &fRank: &c" + coreAdapter.getCore().getRankColor(target.getPlayer()) + coreAdapter.getCore().getRankName(target.getPlayer()),
                        "&c│ &fCoins: &c$" + profile.getProfileData().getCoins(),
                        "&c│ &fLevel: &c" + levelService.getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName(),
                        CC.MENU_BAR

                )
                .build();
    }
}
