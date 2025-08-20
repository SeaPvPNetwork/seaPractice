package dev.revere.alley.feature.title.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.title.model.TitleRecord;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.progress.ProgressService;
import dev.revere.alley.core.profile.progress.PlayerProgress;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@AllArgsConstructor
public class TitleButton extends Button {
    private final Profile profile;
    private final TitleRecord title;

    @Override
    public ItemStack getButtonItem(Player player) {

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name("&c&l" + this.title.getKit().getName())
                    .lore(
                            this.progress()
                    )
                    .durability(14)
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(this.title.getKit().getIcon())
                .name("&c&l" + this.title.getKit().getName())
                .lore(
                        CC.MENU_BAR,
                        " &a&lUNLOCKED",
                        "",
                        "&aClick to select!",
                        CC.MENU_BAR
                )
                .durability(this.title.getKit().getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cTo select this title, you need to unlock it first."));
            return;
        }

        if (this.profile.getProfileData().getSelectedTitle().equals(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cYou already have this title selected."));
            return;
        }

        this.profile.getProfileData().setSelectedTitle(this.title.getKit().getName());
        player.sendMessage(CC.translate("&aYou have selected the &c" + this.title.getKit().getName() + " &atitle."));
    }

    /**
     * Get the progress of the title.
     *
     * @return A list of strings representing the progress.
     */
    private List<String> progress() {
        PlayerProgress progress = AlleyPlugin.getInstance().getService(ProgressService.class).calculateProgress(this.profile, this.title.getKit().getName());

        return Arrays.asList(
                CC.MENU_BAR,
                " &c&lLOCKED",
                "",
                String.format(" &fUnlock &c%s &fwith %d more %s.",
                        progress.getNextRankName(),
                        progress.getWinsRequired(),
                        progress.getWinOrWins()
                ),
                "&f " + progress.getProgressBar(12, "■") + " &7" + progress.getProgressPercentage(),
                "",
                "&fRequires &c&l" + this.title.getRequiredDivision().getName().toUpperCase() + " &fdivision.",
                CC.MENU_BAR
        );
    }
}