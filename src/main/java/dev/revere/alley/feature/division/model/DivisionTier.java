package dev.revere.alley.feature.division.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
@Getter
@Setter
public class DivisionTier {
    private final String name;
    private int requiredWins;

    /**
     * Constructor for the DivisionTier class.
     *
     * @param name         The level of the division model.
     * @param requiredWins The required wins of the division model.
     */
    public DivisionTier(String name, int requiredWins) {
        this.name = name;
        this.requiredWins = requiredWins;
    }
}