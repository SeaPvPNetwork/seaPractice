package dev.revere.alley.feature.combat;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CombatService extends Service {
    /**
     * Gets the raw map of all active combat tags.
     * <p>
     * Warning: This provides direct access to the underlying data.
     * It's recommended to use other methods on this service for standard operations.
     *
     * @return An unmodifiable map of model UUIDs to their Combat data.
     */
    Map<UUID, Combat> getCombatMap();

    /**
     * Records a combat event between two players. This will tag both the victim
     * and the attacker.
     *
     * @param victim   The model who was damaged.
     * @param attacker The model who dealt the damage.
     */
    void setLastAttacker(Player victim, Player attacker);

    /**
     * Gets the last model who attacked the given victim, if the tag is still active.
     *
     * @param victim The model whose last attacker is being requested.
     * @return The attacking Player, or null if the tag has expired or doesn't exist.
     */
    Player getLastAttacker(Player victim);

    /**
     * Removes the combat tag for a specific model.
     *
     * @param player     The model whose tag to remove.
     * @param removeBoth If true, also removes the combat tag from the other model involved in the combat.
     */
    void removeLastAttacker(Player player, boolean removeBoth);

    /**
     * Checks if a model is currently considered in combat.
     *
     * @param uuid The UUID of the model to check.
     * @return True if the model has an active combat tag, false otherwise.
     */
    boolean isPlayerInCombat(UUID uuid);

    /**
     * Checks if a model's combat tag has expired.
     * Note: This method will also remove the tag if it is found to be expired.
     *
     * @param player The model to check.
     * @return true if the model has no active combat tag.
     */
    boolean isExpired(Player player);

    /**
     * Gets the remaining time on a model's combat tag.
     *
     * @param victim The model to check.
     * @return The remaining time in milliseconds, or 0 if not in combat.
     */
    long getRemainingTime(Player victim);

    /**
     * Gets the remaining time on a model's combat tag, formatted as a string (e.g., "4.2s").
     *
     * @param victim The model to check.
     * @return A formatted string representing the remaining time.
     */
    String getRemainingTimeFormatted(Player victim);

    /**
     * Forcibly removes a model's combat tag.
     *
     * @param player The model whose combat tag should be reset.
     */
    void resetCombatLog(Player player);
}