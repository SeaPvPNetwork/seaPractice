package dev.revere.alley.feature.party.internal;

import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.cooldown.Cooldown;
import dev.revere.alley.feature.cooldown.CooldownService;
import dev.revere.alley.feature.cooldown.CooldownType;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.HotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyRequest;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueProfile;
import dev.revere.alley.feature.visibility.VisibilityService;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.impl.PartyLocale;
import dev.revere.alley.feature.match.MatchService;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.TeamGameParticipant;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.common.SoundUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.ClickableUtil;
import dev.revere.alley.common.text.Symbol;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 22:57
 */
@Getter
@Service(provides = PartyService.class, priority = 230)
public class PartyServiceImpl implements PartyService {
    private final ConfigService configService;
    private final ProfileService profileService;
    private final HotbarService hotbarService;
    private final ReflectionService reflectionService;
    private final CooldownService cooldownService;
    private final VisibilityService visibilityService;
    private final MatchService matchService;
    private final ArenaService arenaService;

    private final List<Party> parties = new ArrayList<>();
    private final List<PartyRequest> partyRequests = new ArrayList<>();
    private String chatFormat;

    public PartyServiceImpl(ConfigService configService, ProfileService profileService, HotbarService hotbarService, ReflectionService reflectionService, CooldownService cooldownService, VisibilityService visibilityService, MatchService matchService, ArenaService arenaService) {
        this.configService = configService;
        this.profileService = profileService;
        this.hotbarService = hotbarService;
        this.reflectionService = reflectionService;
        this.cooldownService = cooldownService;
        this.visibilityService = visibilityService;
        this.matchService = matchService;
        this.arenaService = arenaService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.chatFormat = configService.getMessagesConfig().getString("party.chat-format");
    }

    @Override
    public List<Party> getParties() {
        return Collections.unmodifiableList(this.parties);
    }

    @Override
    public void startMatch(Kit kit, Arena arena, Party party) {
        List<Player> allPartyPlayers = party.getMembers().stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());

        Collections.shuffle(allPartyPlayers);

        Player leaderA = allPartyPlayers.get(0);
        Player leaderB = allPartyPlayers.get(1);

        MatchGamePlayer gameLeaderA = new MatchGamePlayer(leaderA.getUniqueId(), leaderA.getName());
        MatchGamePlayer gameLeaderB = new MatchGamePlayer(leaderB.getUniqueId(), leaderB.getName());

        GameParticipant<MatchGamePlayer> participantA = new TeamGameParticipant<>(gameLeaderA);
        GameParticipant<MatchGamePlayer> participantB = new TeamGameParticipant<>(gameLeaderB);

        int totalPlayers = allPartyPlayers.size();
        int teamATargetSize = totalPlayers / 2;
        int currentTeamACount = 1;

        for (int i = 2; i < allPartyPlayers.size(); i++) {
            Player currentPlayer = allPartyPlayers.get(i);
            MatchGamePlayer gamePlayer = new MatchGamePlayer(currentPlayer.getUniqueId(), currentPlayer.getName());

            if (currentTeamACount < teamATargetSize) {
                participantA.addPlayer(gamePlayer);
                currentTeamACount++;
            } else {
                participantB.addPlayer(gamePlayer);
            }
        }

        this.matchService.createAndStartMatch(
                kit, this.arenaService.selectArenaWithPotentialTemporaryCopy(arena), participantA, participantB, true, false, false
        );
    }

    @Override
    public void createParty(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot create a party in this state."));
            return;
        }

        Party party = new Party(player);

        this.parties.add(party);

        profile.setParty(party);

        this.hotbarService.applyHotbarItems(player);

        this.reflectionService.getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&a&l" + Symbol.CROSSED_SWORDS + " Party Created",
                "&7Type /p for help."
        );

        SoundUtil.playCustomSound(player, Sound.FIREWORK_TWINKLE, 1.0F, 1.0F);

        Arrays.asList(
                "",
                "&c&lParty Created &a" + Symbol.TICK,
                " &7Type /p for help.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    @Override
    public void disbandParty(Player leader) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        party.notifyParty("&c&lParty &7&l" + Symbol.ARROW_R + " &c" + leader.getName() + " &cdisbanded the party.");

        for (UUID memberId : new ArrayList<>(party.getMembers())) {
            Player member = Bukkit.getPlayer(memberId);

            Profile profile = this.profileService.getProfile(memberId);
            if (profile != null && profile.getQueueProfile() != null) {
                Queue queue = profile.getQueueProfile().getQueue();
                if (queue != null) {
                    queue.removePlayer(profile.getQueueProfile());
                }
            }

            if (member != null && member.isOnline()) {
                this.setupProfile(member, false);
            }
        }

        this.parties.remove(party);

        Cooldown cooldown = this.cooldownService.getCooldown(leader.getUniqueId(), CooldownType.PARTY_ANNOUNCE_COOLDOWN);
        if (cooldown != null && cooldown.isActive()) {
            cooldown.resetCooldown();
        }

        this.reflectionService.getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                leader,
                "&c&l✖ Party Disbanded",
                "&7You've removed your party."
        );

        SoundUtil.playBanHammer(leader);
    }

    @Override
    public void leaveParty(Player player) {
        Party party = this.getPartyByMember(player.getUniqueId());
        if (party == null) {
            if (player.isOnline()) {
                player.sendMessage(CC.translate("&cYou are not in a party."));
            }
            return;
        }

        Profile profile = this.profileService.getProfile(player.getUniqueId());
        QueueProfile queueProfile = profile.getQueueProfile();

        if (queueProfile != null) {
            this.handlePartyMemberLeave(player);
        }

        party.getMembers().remove(player.getUniqueId());
        party.notifyParty("&a" + player.getName() + " has left the party.");

        this.setupProfile(player, false);
    }

    @Override
    public void kickMember(Player leader, Player member) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (party.getLeader().equals(member)) {
            leader.sendMessage(CC.translate("&cYou cannot kick the party leader."));
            return;
        }

        if (!party.getMembers().contains(member.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not in your party."));
            return;
        }

        Profile profile = this.profileService.getProfile(member.getUniqueId());
        QueueProfile queueProfile = profile.getQueueProfile();

        party.getMembers().remove(member.getUniqueId());
        party.notifyParty("&c" + member.getName() + " has been kicked from the party.");

        if (queueProfile != null) {
            this.handlePartyMemberLeave(member);
        }

        this.setupProfile(member, false);
    }

    @Override
    public void banMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (party.getLeader().equals(target)) {
            leader.sendMessage(CC.translate("&cYou cannot ban the party leader."));
            return;
        }

        if (!party.getMembers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not in your party."));
            return;
        }

        Profile profile = this.profileService.getProfile(target.getUniqueId());
        QueueProfile queueProfile = profile.getQueueProfile();

        party.getBannedPlayers().add(target.getUniqueId());
        party.getMembers().remove(target.getUniqueId());

        if (queueProfile != null) {
            this.handlePartyMemberLeave(target);
        }

        this.setupProfile(target, false);

        party.notifyParty(CC.translate("&c" + target.getName() + " has been banned from the party."));
        target.sendMessage(CC.translate("&cYou have been banned from the party."));
    }

    @Override
    public void unbanMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (!party.getBannedPlayers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not banned from your party."));
            return;
        }

        party.getBannedPlayers().remove(target.getUniqueId());
        party.notifyParty(CC.translate("&c" + target.getName() + " &ahas been unbanned from the party and is now able to join again."));
        target.sendMessage(CC.translate("&aYou have been unbanned from &c" + party.getLeader().getName() + "'s &aparty."));
    }

    @Override
    public void joinParty(Player player, Player leader) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in lobby to join a party."));
            return;
        }
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis party does not exist."));
            return;
        }

        Party yourParty = this.getPartyByLeader(player);
        if (yourParty != null) {
            player.sendMessage(CC.translate("&cYou are already in a party."));
            return;
        }

        if (party.getLeader() == player) {
            player.sendMessage(CC.translate("&cYou cannot join your own party."));
            return;
        }

        if (party.getMembers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are already in this party."));
            return;
        }

        if (party.getBannedPlayers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are banned from &c" + leader.getName() + "&c's party."));
            return;
        }

        Profile leaderProfile = this.profileService.getProfile(leader.getUniqueId());
        QueueProfile queueProfile = leaderProfile.getQueueProfile();

        if (queueProfile != null) {
            player.sendMessage(CC.translate("&cYou can't join the party as they are already in a queue."));
            return;
        }

        party.getMembers().add(player.getUniqueId());
        party.notifyParty("&a" + player.getName() + " has joined the party.");

        player.sendMessage(CC.translate(PartyLocale.JOINED_PARTY.getMessage().replace("{player}", leader.getName())));

        this.setupProfile(player, true);
    }

    @Override
    public PartyRequest getRequest(Player player) {
        return this.partyRequests.stream()
                .filter(request -> request.getTarget().equals(player))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeRequest(PartyRequest request) {
        this.partyRequests.remove(request);
    }

    @Override
    public String getChatFormat() {
        return chatFormat;
    }

    /**
     * Sets up the profile of a player.
     *
     * @param player The player to set up the profile for.
     * @param join   Whether the player is joining a party.
     */
    private void setupProfile(Player player, boolean join) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        profile.setParty(join ? this.getPartyByMember(player.getUniqueId()) : null);

        if (profile.getMatch() != null) {
            return;
        }

        profile.setState(ProfileState.LOBBY);
        this.hotbarService.applyHotbarItems(player, join ? HotbarType.PARTY : HotbarType.LOBBY);
        this.visibilityService.updateVisibility(player);
    }

    @Override
    public void sendInvite(Party party, Player sender, Player target) {
        if (party == null) return;

        PartyRequest request = new PartyRequest(sender, target);
        this.partyRequests.add(request);

        target.sendMessage("");
        target.sendMessage(CC.translate("&c&lParty Invitation"));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &c" + sender.getName()));
        target.sendMessage(CC.translate("&f&l ● &fPlayers: &c" + party.getMembers().size() + "&f/&c30")); //TODO: Implement party size limit with permissions ect...
        target.spigot().sendMessage(this.getClickable(sender));
        target.sendMessage("");
    }

    @Override
    public Party getPartyByLeader(Player player) {
        return this.parties.stream()
                .filter(party -> party.getLeader().equals(player))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Party getPartyByMember(UUID uuid) {
        return this.parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Party getParty(Player player) {
        Party party = getPartyByLeader(player);
        if (party != null) {
            return party;
        }
        return getPartyByMember(player.getUniqueId());
    }

    @Override
    public void announceParty(Party party) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("");
            player.sendMessage(CC.translate("&c&l" + party.getLeader().getName() + " &a&lis inviting you to join &c&ltheir &a&lparty!"));
            player.spigot().sendMessage(this.getClickable(party.getLeader()));
            player.sendMessage("");
        });
    }

    /**
     * Handles a player leaving a party, specifically notifying the QueueService if they were queuing.
     * This method should be called whenever a party member disconnects or leaves their party.
     *
     * @param player The player who left the party (or disconnected).
     */
    public void handlePartyMemberLeave(Player player) {
        if (player == null) return;

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getQueueProfile() == null || profile.getMatch() != null) {
            return;
        }

        QueueProfile associatedQueueProfile = profile.getQueueProfile();
        Queue queue = associatedQueueProfile.getQueue();
        if (queue == null) {
            return;
        }

        if (queue.isDuos()) {
            Player leader = Bukkit.getPlayer(associatedQueueProfile.getUuid());
            Party party = getPartyByLeader(leader);
            if (party.getMembers().size() < 2) {
                leader.sendMessage(CC.translate("&eA party member has left/disconnected. You are now queuing solo for duos."));
            }
        }

        profile.setQueueProfile(null);
    }

    /**
     * Gets the clickable text component for accepting a party invitation.
     *
     * @param sender The player who sent the invitation.
     * @return The clickable text component.
     */
    private TextComponent getClickable(Player sender) {
        return ClickableUtil.createComponent(
                " &a(Click to accept)",
                "/party accept " + sender.getName(),
                "&aClick to accept &c" + sender.getName() + "&a's party invitation."
        );
    }
}