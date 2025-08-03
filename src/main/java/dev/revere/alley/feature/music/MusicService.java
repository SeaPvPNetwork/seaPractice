package dev.revere.alley.feature.music;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.core.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public interface MusicService extends Service {
    /**
     * Starts a music session for a model.
     * This will first stop any currently playing music for the model. A random song
     * from the model's selected preferences will then be played if their lobby music setting is enabled.
     * The audio is played client-side and appears to emanate from the spawn location.
     *
     * @param player The model to start the music for.
     */
    void startMusic(Player player);

    /**
     * Fully stops a model's music session.
     * This is a "hard stop" that halts the audio, cancels any associated tracking tasks,
     * and completely removes the model's session from memory.
     *
     * @param player The model whose music session should be stopped.
     */
    void stopMusic(Player player);

    /**
     * Retrieves a list of all available music discs defined in the system.
     *
     * @return An immutable list of {@link MusicDisc} representing all available music discs.
     */
    List<MusicDisc> getMusicDiscs();

    /**
     * Selects a random music disc from the entire pool of available discs.
     *
     * @return A random {@link MusicDisc} value.
     */
    MusicDisc getRandomMusicDisc();

    /**
     * Retrieves the set of music discs a model has selected in their profile.
     * This method safely converts disc names stored in the profile to {@link MusicDisc} objects.
     *
     * @param profile The model's profile containing their music preferences.
     * @return A non-null set of {@link MusicDisc} values representing the selected discs.
     */
    Set<MusicDisc> getSelectedMusicDiscs(Profile profile);

    /**
     * Selects a random music disc from a model's personal list of selected discs.
     * If the model has not selected any discs, this will fall back to selecting a
     * random disc from the global pool.
     *
     * @param profile The model's profile.
     * @return A random {@link MusicDisc} from the model's selection.
     */
    MusicDisc getRandomSelectedMusicDisc(Profile profile);

    /**
     * Retrieves the current music session state for a specific model.
     *
     * @param playerUuid The UUID of the model.
     * @return An {@link Optional} containing the {@link MusicSession} if one is active for the model, otherwise empty.
     */
    Optional<MusicSession> getMusicState(UUID playerUuid);
}