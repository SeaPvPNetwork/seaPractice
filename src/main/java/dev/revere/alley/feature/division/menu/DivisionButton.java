package dev.revere.alley.feature.division.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.title.menu.TitleMenu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 25/01/2025
 */
@Getter
@AllArgsConstructor
public class DivisionButton extends Button {
    private final Division division;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.division.getIcon())
                .durability(this.division.getDurability())
                .name("&c&l" + this.division.getDisplayName() + " Division")
                .lore(
                        CC.MENU_BAR,
                        "&f&l● &cTiers: &f" + this.division.getTiers().size(),
                        "  &7▶ (" + this.division.getTiers().get(0).getRequiredWins() + " - " + this.division.getTotalWins() + " Wins)",
                        "",
                        " &fFor each kit, you will have",
                        " &fa division based on your",
                        " &cUnranked &fwins.",
                        "",
                        "&aClick to see your titles.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new TitleMenu(AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}