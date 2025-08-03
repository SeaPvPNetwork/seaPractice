package dev.revere.alley.feature.match.snapshot;

import dev.revere.alley.bootstrap.lifecycle.Service;

import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface SnapshotService extends Service {
    Map<UUID, Snapshot> getSnapshots();

    /**
     * Adds a post-match snapshot to the repository.
     * If a snapshot for the same model already exists, it will be overwritten.
     *
     * @param snapshot The snapshot to add.
     */
    void addSnapshot(Snapshot snapshot);

    /**
     * Retrieves a snapshot by the model's UUID.
     *
     * @param uuid The UUID of the model.
     * @return The Snapshot object, or null if not found.
     */
    Snapshot getSnapshot(UUID uuid);

    /**
     * Retrieves a snapshot by the model's username (case-insensitive).
     *
     * @param username The username of the model.
     * @return The Snapshot object, or null if not found.
     */
    Snapshot getSnapshot(String username);

    /**
     * Removes a snapshot from the repository, typically after it has been viewed
     * or has expired.
     *
     * @param uuid The UUID of the model whose snapshot should be removed.
     */
    void removeSnapshot(UUID uuid);
}