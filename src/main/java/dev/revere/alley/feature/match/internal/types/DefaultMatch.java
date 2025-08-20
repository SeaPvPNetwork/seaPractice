package dev.revere.alley.feature.match.internal.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.raiding.BaseRaidingService;
import dev.revere.alley.feature.kit.setting.types.mechanic.KitSettingDropItemsImpl;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRaiding;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRespawnTimer;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.feature.match.model.MatchGamePlayerData;
import dev.revere.alley.feature.match.model.BaseRaiderRole;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.utility.MatchUtility;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.progress.ProgressService;
import dev.revere.alley.core.profile.progress.PlayerProgress;
import dev.revere.alley.common.elo.EloCalculator;
import dev.revere.alley.common.elo.EloResult;
import dev.revere.alley.common.elo.OldEloResult;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.common.InventoryUtil;
import dev.revere.alley.common.ListenerUtil;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class DefaultMatch extends Match {
    private final GameParticipant<MatchGamePlayer> participantA;
    private final GameParticipant<MatchGamePlayer> participantB;

    public final ChatColor teamAColor;
    public final ChatColor teamBColor;

    private GameParticipant<MatchGamePlayer> winner;
    private GameParticipant<MatchGamePlayer> loser;

    /**
     * Constructor for the MatchRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public DefaultMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayer> participantA, GameParticipant<MatchGamePlayer> participantB) {
        super(queue, kit, arena, ranked);
        this.participantA = participantA;
        this.participantB = participantB;
        this.teamAColor = ChatColor.BLUE;
        this.teamBColor = ChatColor.RED;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);
        this.applyColorKit(player);

        Location spawnLocation = this.getParticipantA().containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        player.teleport(spawnLocation);

        if (this.getKit().isSettingEnabled(KitSettingRaiding.class)) {
            this.determineRolesAndGiveKit(player);
        }
    }

    @Override
    public List<GameParticipant<MatchGamePlayer>> getParticipants() {
        return Arrays.asList(getParticipantA(), getParticipantB());
    }

    /**
     * Get the team color of a participant.
     *
     * @param participant The participant to get the team color of.
     * @return The team color of the participant.
     */
    public ChatColor getTeamColor(GameParticipant<MatchGamePlayer> participant) {
        return participant == this.getParticipantA() ? this.teamAColor : this.teamBColor;
    }

    /**
     * Applies the wool color to the player based on their team.
     *
     * @param player The player to apply the wool color to.
     */
    public void applyColorKit(Player player) {
        GameParticipant<MatchGamePlayer> participant = this.getParticipant(player);
        if (participant == null) {
            return;
        }

        final InventoryUtil.TeamColor colorToApply = (participant == this.getParticipantA())
                ? InventoryUtil.TeamColor.BLUE
                : InventoryUtil.TeamColor.RED;

        participant.getPlayers().stream()
                .map(MatchGamePlayer::getTeamPlayer)
                .filter(p -> p != null && p.isOnline())
                .forEach(teamPlayer -> InventoryUtil.applyTeamColorToInventory(teamPlayer, colorToApply));
    }

    @Override
    protected boolean shouldHandleRegularRespawn(Player player) {
        return !this.getKit().isSettingEnabled(KitSettingRespawnTimer.class);
    }

    @Override
    public void handleRoundEnd() {
        final boolean teamADead = this.getParticipantA().isAllEliminated() || this.getParticipantA().isAllDead();
        final GameParticipant<MatchGamePlayer> winner = teamADead ? this.getParticipantB() : this.getParticipantA();
        final GameParticipant<MatchGamePlayer> loser = teamADead ? this.getParticipantA() : this.getParticipantB();

        this.winner = winner;
        this.loser = loser;

        broadcastMatchOutcome(winner, loser);
        processStatistics(winner, loser);

        if (!this.getSpectators().isEmpty()) {
            this.broadcastAndStopSpectating();
        }

        super.handleRoundEnd();
    }

    /**
     * Handles all player-facing messages at the end of a match, including titles and results.
     */
    private void broadcastMatchOutcome(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        winner.getPlayers().forEach(gamePlayer -> sendVictory(gamePlayer.getTeamPlayer()));
        loser.getPlayers().forEach(gamePlayer -> sendDefeat(gamePlayer.getTeamPlayer()));

        if (this.isTeamMatch()) {
            MatchUtility.sendConjoinedMatchResult(this, winner, loser);
        } else {
            MatchGamePlayer winnerPlayer = winner.getLeader();
            MatchGamePlayer loserPlayer = loser.getLeader();
            MatchUtility.sendMatchResult(
                    this,
                    winnerPlayer.getUsername(),
                    loserPlayer.getUsername(),
                    winnerPlayer.getUuid(),
                    loserPlayer.getUuid()
            );
        }
    }

    /**
     * Processes all backend statistics if the match is configured to affect them.
     */
    private void processStatistics(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        if (!this.isAffectStatistics()) {
            return;
        }

        handleMatchData(winner, loser);

        if (!this.isRanked()) {
            this.sendProgressToWinner(winner.getLeader().getTeamPlayer());
        }
    }

    /**
     * Routes to the correct statistics handling method based on the match type.
     *
     * @param winner The winning participant.
     * @param loser  The losing participant.
     */
    private void handleMatchData(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        if (this.isTeamMatch()) {
            return;
        }

        if (this.isRanked()) {
            updateRankedStats(winner, loser);
        } else {
            updateUnrankedStats(winner, loser);
        }
    }

    /**
     * Updates player profiles and Elo for a ranked match.
     */
    private void updateRankedStats(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        OldEloResult result = this.getOldEloResult(winner, loser);
        EloResult eloResult = this.getEloResult(result.getOldWinnerElo(), result.getOldLoserElo());

        this.handleWinner(eloResult.getNewWinnerElo(), winner);
        this.handleLoser(eloResult.getNewLoserElo(), loser);

        this.sendEloResult(
                winner.getLeader().getTeamPlayer().getName(),
                loser.getLeader().getTeamPlayer().getName(),
                result.getOldWinnerElo(),
                result.getOldLoserElo(),
                eloResult.getNewWinnerElo(),
                eloResult.getNewLoserElo()
        );
    }

    /**
     * Updates player profiles with wins/losses for an unranked match.
     */
    private void updateUnrankedStats(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);

        Profile winnerProfile = profileService.getProfile(winner.getLeader().getUuid());
        winnerProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementUnrankedWins();
        winnerProfile.getProfileData().determineTitles();

        Profile loserProfile = profileService.getProfile(loser.getLeader().getUuid());
        loserProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementUnrankedLosses();
    }

    /**
     * Send the winner title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendVictory(Player player) {
        AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&a&lVICTORY!",
                "&fYou have won the match!"
        );
    }

    /**
     * Send the loser title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendDefeat(Player player) {
        AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&c&lDEFEAT!",
                "&fYou have lost the match!"
        );
    }

    /**
     * Sends the elo result message.
     *
     * @param winnerName   The name of the winner.
     * @param loserName    The name of the loser.
     * @param oldEloWinner The old elo of the winner.
     * @param oldEloLoser  The old elo of the loser.
     * @param newEloWinner The new elo of the winner.
     * @param newEloLoser  The new elo of the loser.
     */
    public void sendEloResult(String winnerName, String loserName, int oldEloWinner, int oldEloLoser, int newEloWinner, int newEloLoser) {
        FileConfiguration config = AlleyPlugin.getInstance().getService(ConfigService.class).getMessagesConfig();

        List<String> list = config.getStringList("match.ended.elo-changes.format");
        String winnerIndicatorColor = config.getString("match.ended.elo-changes.winner-indicator-color", "&a");
        String loserIndicatorColor = config.getString("match.ended.elo-changes.loser-indicator-color", "&c");

        list.replaceAll(string -> string
                .replace("{winner}", winnerName)
                .replace("{loser}", loserName)
                .replace("{old-winner-elo}", String.valueOf(oldEloWinner))
                .replace("{old-loser-elo}", String.valueOf(oldEloLoser))
                .replace("{new-winner-elo}", String.valueOf(newEloWinner))
                .replace("{new-loser-elo}", String.valueOf(newEloLoser))
                .replace("{winner-indicator}", newEloWinner > oldEloWinner ? "+" : "-")
                .replace("{loser-indicator}", newEloLoser > oldEloLoser ? "+" : "-")
                .replace("{winner-color}", newEloWinner > oldEloWinner ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{loser-color}", newEloLoser > oldEloLoser ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{math-winner-elo}", String.valueOf(Math.abs(oldEloWinner - newEloWinner)))
                .replace("{math-loser-elo}", String.valueOf(Math.abs(oldEloLoser - newEloLoser)))
        );

        list.forEach(this::notifyParticipants);
    }

    /**
     * Sends the progress of the winner to the player using the ProgressService.
     * The method no longer needs Division or Tier passed in.
     *
     * @param winner The winning player.
     */
    public void sendProgressToWinner(Player winner) {
        Profile winnerProfile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(winner.getUniqueId());

        PlayerProgress progress = AlleyPlugin.getInstance().getService(ProgressService.class).calculateProgress(winnerProfile, this.getKit().getName());

        String progressLine;

        if (progress.isMaxRank() && progress.getCurrentWins() >= progress.getWinsForNextTier()) {
            progressLine = " &c&l● &fCONGRATULATIONS! You have reached the maximum rank!";
        } else {
            progressLine = String.format(" &c&l● &fUnlock &c%s &fwith %d more %s!",
                    progress.getNextRankName(),
                    progress.getWinsRequired(),
                    progress.getWinOrWins()
            );
        }

        Arrays.asList(
                "&c&lProgress",
                progressLine,
                "  &7(" + progress.getProgressBar(12, "■") + "&7) " + progress.getProgressPercentage(),
                " &c&l● &fDaily Streak: &c" + "N/A" + " &f(Best: " + "N/A" + ")",
                " &c&l● &fWin Streak: &c" + "N/A" + " &f(Best: " + "N/A" + ")",
                ""
        ).forEach(line -> winner.sendMessage(CC.translate(line)));
    }

    /**
     * Method to get the old elo result.
     *
     * @return The old elo result.
     */
    public @NotNull OldEloResult getOldEloResult(GameParticipant<MatchGamePlayer> winner, GameParticipant<MatchGamePlayer> loser) {
        int oldWinnerElo = winner.getLeader().getElo();
        int oldLoserElo = loser.getLeader().getElo();
        return new OldEloResult(oldWinnerElo, oldLoserElo);
    }

    /**
     * Method to get the elo result.
     *
     * @param oldWinnerElo The old elo of the winner.
     * @param oldLoserElo  The old elo of the loser.
     * @return The elo result.
     */
    public @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        EloCalculator eloCalculator = AlleyPlugin.getInstance().getService(EloCalculator.class);
        int newWinnerElo = eloCalculator.determineNewElo(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = eloCalculator.determineNewElo(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    /**
     * Method to handle the winner.
     *
     * @param elo    The new elo of the winner.
     * @param winner The winner of the match.
     */
    public void handleWinner(int elo, GameParticipant<MatchGamePlayer> winner) {
        Profile winnerProfile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(winner.getLeader().getUuid());
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementRankedWins();
        winnerProfile.getProfileData().updateElo(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo   The new elo of the loser.
     * @param loser The loser of the match.
     */
    public void handleLoser(int elo, GameParticipant<MatchGamePlayer> loser) {
        Profile loserProfile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(loser.getLeader().getUuid());
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementRankedLosses();
        loserProfile.getProfileData().updateElo(loserProfile);
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return (this.getParticipantA().isAllDead() || this.getParticipantB().isAllDead())
                || (this.getParticipantA().getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected)
                || this.getParticipantB().getAllPlayers().stream().allMatch(MatchGamePlayer::isDisconnected));
    }

    @Override
    public boolean canEndMatch() {
        return true;
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, false, true);
    }

    @Override
    public void handleDeathItemDrop(Player player, PlayerDeathEvent event) {
        if (this.getKit().isSettingEnabled(KitSettingDropItemsImpl.class)) {
            ListenerUtil.clearDroppedItemsOnDeath(event, player);
        } else {
            event.getDrops().clear();
        }
    }

    @Override
    public void handleDisconnect(Player player) {
        if (!(this.getState() == MatchState.STARTING || this.getState() == MatchState.RUNNING)) {
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        this.sendMessage(profile.getFancyName() + " &fdisconnected.");

        MatchGamePlayer gamePlayer = this.getFromAllGamePlayers(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            gamePlayer.setEliminated(true);
            if (!gamePlayer.isDead()) {
                this.handleDeath(player, EntityDamageEvent.DamageCause.CUSTOM);
            }
        }

        if (player.isOnline()) {
            this.finalizePlayer(player);
        }
    }

    /**
     * Gives the base raiding kit to the player based on their team.
     *
     * @param player The player to give the kit to.
     */
    public void determineRolesAndGiveKit(Player player) {
        if (this.getParticipantA() == null || this.getParticipantB() == null) {
            return;
        }

        Kit parentKit = this.getKit();
        if (parentKit == null) {
            Logger.error("&cCould not determine the parent kit for the raiding match.");
            return;
        }

        BaseRaiderRole role = getParticipantA().containsPlayer(player.getUniqueId())
                ? BaseRaiderRole.TRAPPER
                : BaseRaiderRole.RAIDER;

        Kit kitToGive = AlleyPlugin.getInstance().getService(BaseRaidingService.class).getRaidingKitByRole(parentKit, role);
        if (kitToGive == null) {
            Logger.info("&cNo kit found for role: " + role.name() + " linked to parent kit.");
            return;
        }

        MatchGamePlayerData data = this.getGamePlayer(player).getData();
        data.setRole(role);

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        LayoutData layout = profile.getProfileData().getLayoutData().getLayouts().get(kitToGive.getName()).get(0);

        //TODO: after implementing multiple layouts, we need to give the books here, if multiple layouts are present in profile.

        if (layout != null) {
            player.getInventory().setContents(layout.getItems());
        } else {
            player.getInventory().setContents(kitToGive.getItems());
        }

        player.getInventory().setArmorContents(kitToGive.getArmor());
        player.updateInventory();
    }
}