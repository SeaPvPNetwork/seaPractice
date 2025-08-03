package dev.revere.alley.feature.party;

import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface PartyService extends Service {
    /**
     * Gets a list of all currently active parties.
     *
     * @return An unmodifiable list of active parties.
     */
    List<Party> getParties();

    /**
     * Creates a new party with the given model as the leader.
     *
     * @param player The model creating the party.
     */
    void createParty(Player player);

    /**
     * Disbands a party, kicking all members. This can only be done by the leader.
     *
     * @param leader The leader of the party to disband.
     */
    void disbandParty(Player leader);

    /**
     * Allows a model to leave the party they are currently in.
     * If they are the leader, the party is disbanded.
     *
     * @param player The model leaving the party.
     */
    void leaveParty(Player player);

    /**
     * Kicks a member from the party. Can only be initiated by the party leader.
     *
     * @param leader The party leader.
     * @param member The model to kick.
     */
    void kickMember(Player leader, Player member);

    /**
     * Bans a model from the party, preventing them from rejoining.
     *
     * @param leader The party leader.
     * @param target The model to ban.
     */
    void banMember(Player leader, Player target);

    /**
     * Unbans a model from the party.
     *
     * @param leader The party leader.
     * @param target The model to unban.
     */
    void unbanMember(Player leader, Player target);

    /**
     * Allows a model to join an existing party.
     *
     * @param player The model joining.
     * @param leader The leader of the party to join.
     */
    void joinParty(Player player, Player leader);

    /**
     * Sends a party invitation from a sender to a target model.
     *
     * @param party  The party instance.
     * @param sender The model sending the invite.
     * @param target The model receiving the invite.
     */
    void sendInvite(Party party, Player sender, Player target);

    /**
     * Gets the party that a model is the leader of.
     *
     * @param player The potential leader.
     * @return The Party object, or null if they are not a leader.
     */
    Party getPartyByLeader(Player player);

    /**
     * Gets the party that a model is a member of.
     *
     * @param uuid The UUID of the potential member.
     * @return The Party object, or null if they are not in a party.
     */
    Party getPartyByMember(UUID uuid);

    /**
     * Gets the party a model is in, regardless of their role (leader or member).
     *
     * @param player The model.
     * @return The Party object, or null if they are not in a party.
     */
    Party getParty(Player player);

    /**
     * Starts a 2v2 party match.
     *
     * @param kit   The kit for the match.
     * @param arena The arena for the match.
     * @param party The party starting the match.
     */
    void startMatch(Kit kit, Arena arena, Party party);

    /**
     * Announces a party to the entire server, inviting players to join.
     *
     * @param party The party to announce.
     */
    void announceParty(Party party);

    List<PartyRequest> getPartyRequests();

    PartyRequest getRequest(Player player);

    void removeRequest(PartyRequest request);

    /**
     * Gets the chat format
     *
     * @return The chat format
     */
    String getChatFormat();
}