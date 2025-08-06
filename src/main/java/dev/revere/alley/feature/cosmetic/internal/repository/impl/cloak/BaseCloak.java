package dev.revere.alley.feature.cosmetic.internal.repository.impl.cloak;

import dev.revere.alley.feature.cosmetic.model.BaseCosmetic;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 4/08/2025
 */
public abstract class BaseCloak extends BaseCosmetic {
    /**
     * Renders the particle effect for this specific cloak.
     * This method is called repeatedly by the CloakService while the player stands still.
     *
     * @param player The player to render the cloak for.
     */
    public abstract void render(Player player);
}