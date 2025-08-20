package dev.revere.alley.feature.abilities.types;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.common.time.DurationFormatter;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.GlobalCooldown;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.TaskUtil;
import dev.revere.alley.common.text.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class Combo extends Ability {
    private final Set<UUID> COMBO = Sets.newHashSet();
    private final Map<UUID, Integer> HITS = Maps.newHashMap();

    public Combo() {
        super("COMBO");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
            AbilityService abilityService = AlleyPlugin.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(Combo.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &c&lCombo &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Combo.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fYou are on &c&lPartner Item &fcooldown for &c" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(Combo.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player,  10 * 1000);

            this.giveComboEffects(player);

            COMBO.add(player.getUniqueId());
            HITS.put(player.getUniqueId(), 0);

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            if (COMBO.contains(damager.getUniqueId())) {
                if (HITS.containsKey(damager.getUniqueId())) {
                    HITS.put(damager.getUniqueId(), HITS.get(damager.getUniqueId()) + 1);
                }
            }
        }
    }

    private void giveComboEffects(Player player) {
        TaskUtil.runLater(() -> {
            int hits = HITS.get(player.getUniqueId());

            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * hits, 1));
            player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 1F, 1F);
            player.sendMessage(CC.translate("&7You've received Strength II for &4" + hits + " &7seconds."));

            HITS.remove(player.getUniqueId());
            COMBO.remove(player.getUniqueId());
        }, 20 * 6);
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (!isAbility(player.getItemInHand())) return;

            ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(Combo.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Combo.class).getRemainingMillis(player), true)));
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }
}