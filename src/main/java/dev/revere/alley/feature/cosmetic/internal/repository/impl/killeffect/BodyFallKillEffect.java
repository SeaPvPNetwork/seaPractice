package dev.revere.alley.feature.cosmetic.internal.repository.impl.killeffect;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.cosmetic.model.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.DeathReflectionServiceImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Body Fall", description = "Let's the opponent's body fall.", permission = "bodyfall", icon = Material.DIAMOND_SWORD, slot = 16)
public class BodyFallKillEffect extends BaseCosmetic {

    @Override
    public void execute(Player player) {
        AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(DeathReflectionServiceImpl.class).animateDeath(player);
    }
}