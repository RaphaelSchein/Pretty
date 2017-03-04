package xyz.raphaelscheinkoenig.pretty.managers.clan;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.Tuple;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import mkremins.fanciful.FancyMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan implements ConfigurationSerializable {

    private boolean pvpEnabled;
    private String ownerUuid;
    private String name, abbreviation;
    private Set<String> playersUuid;
    private List<Player> onlinePlayers;
    private List<String> assistantUuids;
    private List<Tuple<UUID, Long>> invites;

    public String getName() {
        return name;
    }

    public Clan(Player owner, String name, String abbreviation) {
        this.ownerUuid = Util.uuid(owner);
        this.name = name;
        this.abbreviation = abbreviation;
        pvpEnabled = false;
        playersUuid = new HashSet<>();
        onlinePlayers = new ArrayList<>();
        assistantUuids = new ArrayList<>();
        invites = new ArrayList<>();
        playersUuid.add(ownerUuid);
        onlinePlayers.add(owner);
    }

    public Clan(Map<String, Object> map){
        this.pvpEnabled = (boolean) map.get("pvpEnabled");
        this.ownerUuid = (String) map.get("ownerUuid");
        this.name = (String) map.get("name");
        this.abbreviation = (String) map.get("abbr");
        this.playersUuid = (Set<String>) map.get("players");
        this.assistantUuids = (List<String>) map.get("assistants");
        invites = new ArrayList<>();
        onlinePlayers = new ArrayList<>();
    }

    public void addPlayer(Player p) {
        List<Player> sync = Collections.synchronizedList(onlinePlayers);
        synchronized (sync) {
            onlinePlayers.add(p);
        }
        Set<String> syncUuids = Collections.synchronizedSet(playersUuid);
        synchronized (syncUuids) {
            playersUuid.add(p.getUniqueId().toString());
        }
        removeInvitationOf(p); //redundant I believe
    }

    public boolean canInvite(Player p){
        if(isMember(p))
            if(isOwner(p) || isAssistant(p))
                return true;
        return false;
    }

    private boolean isMember(Player p) {
        return playersUuid.contains(Util.uuid(p));
    }

    private boolean isAssistant(Player p){
        return assistantUuids.contains(Util.uuid(p));
    }

    public void onPlayerOnline(Player p) {
        onlinePlayers.add(p);
    }

    public void onPlayerNotOnlineAnymore(Player p) {
        onlinePlayers.remove(p);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public boolean isOwner(Player p) {
        return Util.uuid(p).equals(ownerUuid);
    }

    public boolean isFriendlyFire() {
        return pvpEnabled;
    }

    public void sendMessage(Player from, String message) {
        final String finalMessage = Util.translate(ConfigManager.getConfig("Clan").getString("clanchat.design").replace("%sender_name%", from.getDisplayName())).replace("%message%", message);
        sendMessage(finalMessage);
    }

    public void sendMessage(String message) {
        onlinePlayers.forEach(p -> p.sendMessage(message));
    }

    public void setFriendlyFire(boolean pvp) {
        this.pvpEnabled = pvp;
    }

    public boolean hasUser(String uuid) {
        return playersUuid.contains(uuid);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("pvpEnabled", pvpEnabled);
        map.put("ownerUuid", ownerUuid);
        map.put("name", name);
        map.put("abbr", abbreviation);
        map.put("players", playersUuid);
        map.put("assistants", assistantUuids);
        return map;
    }

    public void onUserLeave(Player p) {
        if(isMember(p)) {
            onlinePlayers.remove(p);
            playersUuid.remove(Util.uuid(p));
            String leftMessage = Util.translate(ConfigManager.getConfig("Clan").getString("messages.member_left_clan").replace("%name%", p.getDisplayName()));
            onlinePlayers.forEach(player -> {
                player.sendMessage(leftMessage);
            });
        }
    }

    public void closeClan() {
        String message = ConfigManager.getConfig("Clan").getString("messages.clan_was_closed");
        onlinePlayers.forEach(p -> p.sendMessage(message));
        ClanManager.instance().removeClan(this);
    }

    public void invite(Player inviter, Player toInvite) {
        FileConfiguration conf = ConfigManager.getConfig("Clan");
        Tuple<UUID, Long> invitation = getInviteOf(toInvite);
        if(invitation == null) {
            addInvitation(toInvite);
            sendInviteMessage(inviter, toInvite);
        } else {
            if(ClanManager.instance().isInvitationValid(invitation)) {
                inviter.sendMessage(Util.translate(conf.getString("messages.player_already_invited").replace("%name%", toInvite.getDisplayName())));
            } else {
                removeInvitationOf(toInvite);
                invite(inviter, toInvite);
            }
        }
    }

    private void sendInviteMessage(Player inviter, Player toInvite) {
        FileConfiguration conf = ConfigManager.getConfig("Clan");
        sendMessage(Util.translate(conf.getString("messages.someone_invited_someone").replace("%inviter%", inviter.getDisplayName()).replace("%invited%", toInvite.getDisplayName())));
        toInvite.sendMessage(Util.translate(conf.getString("messages.someone_invited_you").replace("%name%", inviter.getDisplayName()).replace("%clanname%", getName())));
        FancyMessage message = new FancyMessage();
        message.text(Util.translate(conf.getString("messages.accept_part"))).command("/clan accept " + getName()).tooltip("Click to accept")
                .text(" ").text(Util.translate(conf.getString("messages.deny_part"))).command("/clan deny " + getName()).tooltip("Click to deny");
        message.send(toInvite);
    }

    public void addInvitation(Player toInvite) {
        Tuple<UUID, Long> invitation = new Tuple<>(toInvite.getUniqueId(), System.currentTimeMillis());
        invites.add(invitation);
    }

    public void removeInvitationOf(Player toInvite) {
        UUID uuid = toInvite.getUniqueId();
        Iterator<Tuple<UUID, Long>> iterator = invites.iterator();
        while(iterator.hasNext()) {
            Tuple<UUID, Long> invite = iterator.next();
            if(invite.getFirst().equals(uuid)) {
                iterator.remove();
                break;
            }
        }
    }

    public Tuple<UUID, Long> getInviteOf(Player p) {
        UUID uuid = p.getUniqueId();
        return invites.stream().filter(tuple -> tuple.getFirst().equals(uuid)).findFirst().orElse(null);
    }

    public void accept(Player p) {
        FileConfiguration conf = ConfigManager.getConfig("Clan");
        Tuple<UUID, Long> invitation = getInviteOf(p);
        if(invitation == null) {
            p.sendMessage(Util.translate(conf.getString("messages.no_invitation")));
        } else {
            boolean valid = ClanManager.instance().isInvitationValid(invitation);
            if(valid) {
                sendAcceptMessage(p);
                removeInvitationOf(p);
                addPlayer(p);
            } else {
                p.sendMessage(Util.translate(conf.getString("messages.invitation_expired")));
            }
        }
    }

    private void sendAcceptMessage(Player p) {
        FileConfiguration conf = ConfigManager.getConfig("Clan");
        sendMessage(Util.translate(conf.getString("messages.someone_joined").replace("%name%", p.getDisplayName())));
        p.sendMessage(Util.translate(conf.getString("messages.you_joined_clan").replace("%clanname%", getName())));
    }

}
