package dev.revere.alley.feature.party.menu.event.impl.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.PartyLocale;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorButton extends Button {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Kit kit;
    private final Arena arena;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&c&l" + this.arena.getName())
                .lore(
                        " &c● &fKit: &c" + this.kit.getDisplayName(),
                        "",
                        "&aClick to select!"
                )
                .durability(0)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Party party = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getParty();
        if (party == null) {
            player.closeInventory();
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        PartyService partyService = AlleyPlugin.getInstance().getService(PartyService.class);
        partyService.startMatch(this.kit, this.arena, party);
    }
}