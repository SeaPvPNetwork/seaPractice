package dev.revere.alley.core.profile.progress;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.core.profile.Profile;

/**
 * @author Remi
 * @project alley-practice
 * @date 3/07/2025
 */
public interface ProgressService extends Service {
    /**
     * Calculates a model's progress for a given kit.
     *
     * @param profile The model's profile, containing their current stats.
     * @param kitName The name of the kit to check progress for.
     * @return A PlayerProgress object containing all calculated data.
     */
    PlayerProgress calculateProgress(Profile profile, String kitName);
}