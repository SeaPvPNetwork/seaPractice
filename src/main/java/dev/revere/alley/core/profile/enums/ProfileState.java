package dev.revere.alley.core.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public enum ProfileState {
    LOBBY("Lobby", "The model is in the lobby"),
    WAITING("Waiting", "The model is waiting to queue for an opponent"),
    PLAYING("Playing", "The model is playing a match"),
    FIGHTING_BOT("Fighting Bot", "The model is fighting a bot"),
    SPECTATING("Spectating", "The model is spectating a match"),
    EDITING("Editing", "The model is editing a kit"),
    PLAYING_TOURNAMENT("Tournament", "The model is in a tournament"),
    PLAYING_EVENT("Event", "The model is in an event"),
    FFA("FFA", "The model is in the FFA lobby"),

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumProfileState enum.
     *
     * @param name        The name of the profile state.
     * @param description The description of the profile state.
     */
    ProfileState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}