package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;
import dev.revere.alley.common.text.CC;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public enum ProfileLocale implements Locale {
    TOGGLED_PARTY_INVITES("messages.yml", "model-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "model-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "model-settings.scoreboard"),
    TOGGLED_SCOREBOARD_LINES("messages.yml", "model-settings.scoreboard-lines"),
    TOGGLED_TABLIST("messages.yml", "model-settings.tablist"),
    TOGGLED_PROFANITY_FILTER("messages.yml", "model-settings.profanity-filter"),
    TOGGLED_DUEL_REQUESTS("messages.yml", "model-settings.duel-requests"),
    TOGGLED_LOBBY_MUSIC("messages.yml", "model-settings.lobby-music"),

    IS_BUSY("messages.yml", "error-messages.model.is-busy"),

    ;

    private final String configName, configString;

    /**
     * Constructor for the ProfileLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ProfileLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    /**
     * Gets the String from the config.
     *
     * @return The message from the config.
     */
    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}