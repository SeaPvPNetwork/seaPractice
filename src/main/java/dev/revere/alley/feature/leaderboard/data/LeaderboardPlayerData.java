package dev.revere.alley.feature.leaderboard.data;

import dev.revere.alley.feature.kit.Kit;
import lombok.Data;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 3/3/2025
 */
@Data
public class LeaderboardPlayerData {
    private final String name;
    private final UUID uuid;
    private final Kit kit;
    private int value;

    /**
     * Constructor for the LeaderboardEntry class.
     *
     * @param name  The name of the model
     * @param uuid  The UUID of the model
     * @param kit   The kit of the model
     * @param value The value of the model
     */
    public LeaderboardPlayerData(String name, UUID uuid, Kit kit, int value) {
        this.name = name;
        this.uuid = uuid;
        this.kit = kit;
        this.value = value;
    }
}