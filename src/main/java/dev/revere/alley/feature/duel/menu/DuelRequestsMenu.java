package dev.revere.alley.feature.duel.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.feature.duel.DuelRequest;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 18:18
 */
public class DuelRequestsMenu extends PaginatedMenu {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&c&lDuel Requests";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        buttons.put(4, new RefreshDuelRequestsButton());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        AlleyPlugin.getInstance().getService(DuelRequestService.class).getDuelRequests()
                .stream()
                .filter(duelRequest -> !duelRequest.getSender().equals(player))
                .forEach(duelRequest -> buttons.put(buttons.size(), new DuelRequestsButton(duelRequest)));


        return buttons;
    }

    @AllArgsConstructor
    private static class DuelRequestsButton extends Button {
        protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
        private DuelRequest duelRequest;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER).name("&c&l" + this.duelRequest.getSender().getName()).durability(0).hideMeta()
                    .lore(
                            "&fKit: &c" + this.duelRequest.getKit().getDisplayName(),
                            "&fArena: &c" + this.duelRequest.getArena().getDisplayName(),
                            "",
                            "&fExpires in: &c" + this.duelRequest.getRemainingTimeFormatted(),
                            "",
                            "&aClick to accept!"
                    )
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (this.duelRequest.hasExpired()) {
                player.sendMessage(CC.translate("&cThis duel request has expired."));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (this.duelRequest.getArena() == null) {
                player.sendMessage(CC.translate("&cThis duel request has no setup arena."));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
                player.sendMessage(CC.translate("&cYou are already in a match."));
                return;
            }

            ServerService serverService = AlleyPlugin.getInstance().getService(ServerService.class);
            if (!serverService.isQueueingAllowed()) {
                player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
                player.closeInventory();
                return;
            }

            AlleyPlugin.getInstance().getService(DuelRequestService.class).acceptPendingRequest(this.duelRequest);
            player.closeInventory();
        }
    }

    @AllArgsConstructor
    private static class RefreshDuelRequestsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD)
                    .name("&a&lRefresh")
                    .lore("&aClick to refresh the duel requests.")
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new DuelRequestsMenu().openMenu(player);
        }
    }
}