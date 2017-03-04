package xyz.raphaelscheinkoenig.pretty.managers.clan;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import xyz.raphaelscheinkoenig.pretty.managers.clan.commands.ClanCommand;
import xyz.raphaelscheinkoenig.pretty.managers.clan.listeners.ClanListener;
import xyz.raphaelscheinkoenig.pretty.util.Tuple;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import org.bukkit.event.Listener;



public class ClanManager extends Manager {

	private static final String NAME = "Clan";
	private static ClanManager instance;
	private Set<Clan> clans;
//	private Set<ClanUser> users;
	
	@Override
	public void onEnable() {
	    instance = this;
	    initConfig();
		initClans();
		initCommands();
		registerListeners();
	}

    private void initCommands() {
        Command[] cmds = {
                new ClanCommand()
        };
        for(Command cmd : cmds)
            registerCommand(cmd);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ConfigManager.getConfig("ClanData").set("clans", clans);
        ConfigManager.saveConfig("ClanData");
    }

    private void registerListeners() {
        Listener[] listeners = {
                new ClanListener()
        };
        for(Listener l : listeners)
            registerListener(l);
    }

    private void initConfig() {
        FileConfiguration conf = ConfigManager.getConfig("Clan");
        conf.addDefault("messages.clan_create_usage", "Wrong usage! Correct: /clan create <name> <abbreviation>");
        conf.addDefault("messages.clan_name_already_taken", "This name is already taken!");
        conf.addDefault("messages.clan_name_abbreviation_already_taken", "This abbreviation is already taken!");
        conf.addDefault("messages.clan_created_successfully", "You have successfully created a clan called %name% with the abbreviation %abbr%");
        conf.addDefault("messages.you_are_already_in_clan", "You are already in a clan!");
        conf.addDefault("messages.you_cannot_hit_clan_members", "You cannot hit clan members if friendly fire is disabled!");
        conf.addDefault("messages.clan_commands", new String[] {"header", "examplecommand", "anothercommand", "yes I am lazy"});
        conf.addDefault("messages.cannot_leave_as_owner", "You cannot use /clan leave as the owner! You need to disband the clan first.");
        conf.addDefault("messages.member_left_clan", "%name% left the clan!");
        conf.addDefault("messages.you_closed_the_clan", "You have closed your clan!");
        conf.addDefault("messages.only_owner_may_close_clan", "You may not close the clan!");
        conf.addDefault("messages.clan_was_closed", "Your clan was closed!");
        conf.addDefault("clanchat.design", " [%sender_name%] %message%");
        conf.addDefault("messages.you_have_no_clan", "Currently, you are in no clan!");
        conf.addDefault("messages.clan_invite_usage", "Correct usage: /clan invite <name>");
        conf.addDefault("messages.only_assistant_can_invite", "You must be promoted to invite others!");
        conf.addDefault("messages.player_not_online", "This player is not online!");
        conf.addDefault("messages.player_has_already_clan", "The player %name% is in a clan already!");
        conf.addDefault("messages.player_already_in_clan", "%name% is already in a clan!");
        conf.addDefault("invite_maximum_time_to_accept_in_milliseconds", 1000*20);
        conf.addDefault("messages.player_already_invited", "%name% is already invited!");
        conf.addDefault("messages.someone_invited_someone", "%inviter% has invited %invited%");
        conf.addDefault("messages.someone_invited_you", "%name% invited you to the clan %clanname%");
        conf.addDefault("messages.accept_part", "[Accept]");
        conf.addDefault("messages.deny_part", "[Deny]");
        conf.addDefault("messages.clan_not_existing", "The clan %clanname% does not exist");
        conf.addDefault("messages.no_invitation", "You had no invitation for this clan!");
        conf.addDefault("messages.someone_joined", "%name% has joined the clan!");
        conf.addDefault("messages.you_joined_clan", "You have joined the clan %clanname%");
        conf.addDefault("messages.invitation_expired", "Your invitation expired! You need to get another one.");
        conf.addDefault("max_clan_name_length", 12);
        conf.addDefault("max_clan_abbreviation_length", 4);
        conf.addDefault("allowed_characters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz0123456789");
        conf.addDefault("messages.clan_name_not_valid", "This name is not valid. You may only use characters from a-z, A-Z, any number and underscores (_)");
        conf.addDefault("messages.clan_abbr_not_valid", "This abbreviation is not valid. You may only use characters from a-z, A-Z, any number and underscores (_)");
        conf.addDefault("forbidden_names", new String[] {"shit", "fuck", "hure", "nutte", "acon", "cunt", "fotze", "arsch", "admiN", "dev", "team", "mojang", "mitarbeiter"});
        conf.addDefault("messages.clan_name_contains_forbidden_name", "The clan name has forbidden words!");
        conf.addDefault("messages.clan_name_too_long", "Name too long");
        conf.addDefault("messages.clan_abbr_too_long", "abbreviation too long");
        conf.addDefault("messages.clan_abbr_contains_bad_words", "abbreviation contains bad words fy");
        conf.options().copyDefaults(true);
        ConfigManager.saveConfig("Clan");
    }

    public long getMaxInviteTime() {
        return ConfigManager.getConfig("Clan").getLong("invite_maximum_time_to_accept_in_milliseconds");
    }

    public boolean isInvitationValid(Tuple<UUID, Long> invitation) {
        return (System.currentTimeMillis() - invitation.getSecond()) <= getMaxInviteTime();
    }

    private void initClans() {
        ConfigurationSerialization.registerClass(Clan.class);
        File f = new File(LamaCore.instance().getDataFolder(), "ClanData.yml");
        if(f.exists()){
            try {
                clans = new HashSet<>((Set<Clan>)ConfigManager.getConfig("ClanData").get("clans"));
            } catch(Exception e){
                //Sorry for doing something like this! :(
                e.printStackTrace();
                clans = new HashSet<>();
            }
        } else
            clans = new HashSet<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            Clan c = getClanOf(p);
            if(c != null)
                c.onPlayerOnline(p);
        }
    }

    public Clan getClanOf(Player p){
        return getClanOf(Util.uuid(p));
    }

    public Clan getClanOf(String uuid){
        Set<Clan> copy = Collections.synchronizedSet(clans);
        synchronized (copy){
            for(Clan c : copy){
                if(c.hasUser(uuid))
                    return c;
            }
        }
        return null;
//        return clans.stream().filter(clan -> clan.hasUser(uuid)).findFirst().orElse(null);
    }

    public Clan getClan(String name){
        Set<Clan> copy = Collections.synchronizedSet(clans);
        synchronized (copy){
            for(Clan c : copy){
                if(c.getName().equalsIgnoreCase(name))
                    return c;
            }
        }
        return null;
    }

    public Clan getClanByAbbreviation(String abbr){
        Set<Clan> copy = Collections.synchronizedSet(clans);
        synchronized (copy){
            for(Clan c : copy){
                if(c.getAbbreviation().equalsIgnoreCase(abbr))
                    return c;
            }
        }
        return null;
    }

    public void createClan(Player owner, String name, String abbreviation){
        Clan clan = new Clan(owner, name, abbreviation);
        Set<Clan> copy = Collections.synchronizedSet(clans);
        synchronized (copy){
            copy.add(clan);
        }
    }

    public void removeClan(Clan clan){
        Set<Clan> copy = Collections.synchronizedSet(clans);
        synchronized (copy){
            copy.remove(clan);
        }
    }

    @Override
	public String getName() {
		return NAME;
	}

    public static ClanManager instance() {
        return instance;
    }

    private static final Pattern NAME_PATTERN = Pattern.compile("([a-zA-Z0-9_])+");

    public boolean isNameValid(String name) {
        String allowed = ConfigManager.getConfig("Clan").getString("allowed_characters");
        for(char c : name.toCharArray()) {
            if(allowed.indexOf(c) < 0)
                return false;
        }
        return true;
    }

    public boolean containsBadWords(String name) {
        name = name.toLowerCase();
        for(String s : ConfigManager.getConfig("Clan").getStringList("forbidden_names"))
            if(name.contains(s.toLowerCase()))
                return true;
        return false;
    }

}
