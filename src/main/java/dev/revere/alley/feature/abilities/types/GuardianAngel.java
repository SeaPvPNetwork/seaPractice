package dev.revere.alley.feature.abilities.types;

import com.google.common.collect.Sets;
import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.common.time.DurationFormatter;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.GlobalCooldown;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;
import java.util.UUID;

public class GuardianAngel extends Ability {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Set<UUID> guardians = Sets.newHashSet();

    public GuardianAngel() {
        super("GUARDIAN_ANGEL");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
            AbilityService abilityService = AlleyPlugin.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(GuardianAngel.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &c&lGuardian Angel &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(GuardianAngel.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fYou are on &c&lPartner Item &fcooldown for &c" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(GuardianAngel.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player,  10 * 1000);

            guardians.add(player.getUniqueId());

            abilityService.sendPlayerMessage(player, this.getAbility());
            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
        }
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(GuardianAngel.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (guardians.contains(event.getEntity().getUniqueId())) {
                Player player = (Player) event.getEntity();
                if (player.getHealth() < 3.0 || player.getHealth() == 3.0) {
                    player.setHealth(player.getMaxHealth());
                    player.playSound(player.getLocation(), Sound.DRINK, 1F, 1F);
                    guardians.remove(player.getUniqueId());
                }
            }
        }
    }
}