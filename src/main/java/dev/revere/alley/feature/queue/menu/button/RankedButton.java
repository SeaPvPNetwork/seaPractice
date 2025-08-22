package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class RankedButton extends Button {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = this.queue.getKit();
        return new ItemBuilder(kit.getIcon())
                .name(kit.getMenuTitle())
                .durability(kit.getDurability())
                .hideMeta()
                .lore(this.getLore(kit, player))
                .hideMeta().build();
    }

    /**
     * Get the lore for the kit.
     *
     * @param kit the kit to get the lore for
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit, Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        if (!kit.getDescription().isEmpty()) {
            Collections.addAll(lore,
                    "&7" + kit.getDescription(),
                    ""
            );
        }
        Collections.addAll(lore,
                "&7│ &fPlaying: &c" + this.queue.getQueueFightCount(),
                "&7│ &fQueueing: &c" + this.queue.getProfiles().size(),
                "",
                "&f&lYour ELO: &c" + AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getRankedKitData().get(kit.getName()).getElo(),
                " &f1. &cNULL &f- &cN/A",
                " &f2. &cNULL &f- &cN/A",
                " &f3. &cNULL &f- &cN/A",
                "",
                "&aClick to play.",
                CC.MENU_BAR
        );

        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        ServerService serverService = AlleyPlugin.getInstance().getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
            player.closeInventory();
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getProfileData().isRankedBanned()) {
            player.closeInventory();
            Arrays.asList(
                    "",
                    "&c&lRANKED BAN",
                    "&cYou are currently banned from ranked queues.",
                    "&7You may appeal at &c&ndiscord.gg/alley-practice&7.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false, true);
        player.closeInventory();
        this.playNeutral(player);
        AlleyPlugin.getInstance().getService(HotbarService.class).applyHotbarItems(player);
    }
}
