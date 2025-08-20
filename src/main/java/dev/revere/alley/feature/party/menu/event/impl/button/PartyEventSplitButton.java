package dev.revere.alley.feature.party.menu.event.impl.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.PartyLocale;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.menu.event.impl.PartyEventSplitArenaSelectorMenu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitButton extends Button {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon())
                .name("&c&l" + this.kit.getDisplayName())
                .lore(
                        CC.MENU_BAR,
                        "&aClick to select!",
                        CC.MENU_BAR
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (player.hasPermission("alley.party.arena.selector")) {
            new PartyEventSplitArenaSelectorMenu(this.kit).openMenu(player);
            return;
        }

        Party party = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getParty();
        if (party == null) {
            player.closeInventory();
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        Arena arena = AlleyPlugin.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        AlleyPlugin.getInstance().getService(PartyService.class).startMatch(this.kit, arena, party);
    }
}