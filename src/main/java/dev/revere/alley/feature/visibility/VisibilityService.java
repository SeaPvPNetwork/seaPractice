package dev.revere.alley.feature.visibility;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface VisibilityService extends Service {
    /**
     * Updates the visibility for a specific model in relation to all other online players,
     * and also updates how all other players see this specific model.
     * <p>
     * This is the main method to call whenever a model's state changes.
     *
     * @param player The model whose visibility state needs a full update.
     */
    void updateVisibility(Player player);

}