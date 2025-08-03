package dev.revere.alley.library.assemble;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface AssembleService extends Service {
    /**
     * Gets the map of all active scoreboard instances.
     *
     * @return A map where the key is the model's UUID and the value is their AssembleBoard.
     */
    Map<UUID, AssembleBoard> getBoards();

    /**
     * Gets the adapter that is currently providing content (title and lines) to the scoreboard.
     *
     * @return The active IAssembleAdapter instance.
     */
    AssembleAdapter getAdapter();

    boolean isCallEvents();

    /**
     * Creates and registers a new scoreboard for a model.
     * This should be called when a model joins.
     *
     * @param player The model to create the board for.
     */
    void createBoard(Player player);

    /**
     * Removes the scoreboard for a model.
     * This should be called when a model quits.
     *
     * @param player The model whose board to remove.
     */
    void removeBoard(Player player);
}