package xyz.raphaelscheinkoenig.pretty.managers.clan.commands;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.clan.Clan;
import xyz.raphaelscheinkoenig.pretty.managers.clan.ClanCloseGUI;
import xyz.raphaelscheinkoenig.pretty.managers.clan.ClanManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;


public class ClanCommand extends Command {

    public ClanCommand() {
        super("clan", "clans", "c");
        addSubCommand(new CreateComamnd());
        addSubCommand(new LeaveCommand());
        addSubCommand(new CloseClan());
        addSubCommand(new InviteCommand());
        addSubCommand(new AcceptCommand());
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        List<String> commandList = ConfigManager.getConfig("Clan").getStringList("messages.clan_commands");
        commandList.forEach(sender::sendMessage);
        return true;
    }

    private static class LeaveCommand extends Command {

        public LeaveCommand() {
            super("leave");
        }

        @Override
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
            if(sender instanceof Player){
                FileConfiguration conf = ConfigManager.getConfig("Clan");
                Player p = (Player) sender;
                Clan clan = ClanManager.instance().getClanOf(p);
                if(clan != null){
                    if(clan.isOwner(p)){
                        p.sendMessage(Util.translate(conf.getString("messages.cannot_leave_as_owner")));
                    } else {
                        clan.onUserLeave(p);
                    }
                } else
                    sendNoClan(p);
            }
            return true;
        }

    }

    private static class CreateComamnd extends Command {

        public CreateComamnd() {
            super("create", "c");
        }

        @Override
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                ArgumentParser ap = new ArgumentParser(args);
                FileConfiguration conf = ConfigManager.getConfig("Clan");
                if(ap.hasExactly(2)){
                    String name = ap.get(1);
                    String abbr = ap.get(2);
                    Clan clan = ClanManager.instance().getClan(name);
                    if(clan != null){
                        p.sendMessage(Util.translate(conf.getString("messages.clan_name_already_taken")));
                        return true;
                    }
                    clan = ClanManager.instance().getClanByAbbreviation(abbr);
                    if(clan != null){
                        p.sendMessage(Util.translate(conf.getString("messages.clan_name_abbreviation_already_taken")));
                        return true;
                    }
                    clan = ClanManager.instance().getClanOf(p);
                    if(clan != null){
                        p.sendMessage(Util.translate(conf.getString("messages.you_are_already_in_clan")));
                        return true;
                    }
                    if(!ClanManager.instance().isNameValid(name)) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_name_not_valid")));
                        return true;
                    }
                    if(!ClanManager.instance().isNameValid(abbr)) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_abbr_not_valid")));
                        return true;
                    }
                    if(ClanManager.instance().containsBadWords(name)) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_name_contains_forbidden_name")));
                        return true;
                    }
                    if(ClanManager.instance().containsBadWords(abbr)) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_abbr_contains_bad_words")));
                        return true;
                    }
                    int maxName = conf.getInt("max_clan_name_length");
                    if(name.length() > maxName) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_name_too_long")));
                        return true;
                    }
                    int maxAbbr = conf.getInt("max_clan_abbreviation_length");
                    if(abbr.length() > maxAbbr) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_abbr_too_long")));
                        return true;
                    }
                    ClanManager.instance().createClan(p, name, abbr);
                    p.sendMessage(Util.translate(conf.getString("messages.clan_created_successfully").replace("%name%", name).replace("%abbr%", abbr)));
                } else {
                    p.sendMessage(Util.translate(conf.getString("messages.clan_create_usage")));
                }
                return true;
            }
            return false;
        }
    }

    private static class CloseClan extends Command {

        public CloseClan() {
            super("close", "disband");
        }

        @Override
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
            if(sender instanceof Player){
                Player p = (Player) sender;
                Clan clan = ClanManager.instance().getClanOf(p);
                if(clan != null){
                    FileConfiguration conf = ConfigManager.getConfig("Clan");
                    if(clan.isOwner(p)){
                        new ClanCloseGUI(p, clan);
                    } else {
                        p.sendMessage(Util.translate(conf.getString("messages.only_owner_may_close_clan")));
                    }
                } else
                    sendNoClan(p);
            }
            return true;
        }
    }

    private static class InviteCommand extends Command {

        public InviteCommand() {
            super("invite", "add");
        }

        @Override
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
            if(sender instanceof Player){
                Player p = (Player) sender;
                Clan clan = ClanManager.instance().getClanOf(p);
                if(clan != null){
                    ArgumentParser ap = new ArgumentParser(args);
                    if(ap.hasExactly(1)){
                        if(clan.canInvite(p)){
                            String oName = ap.get(1);
                            Player o = Bukkit.getPlayer(oName);
                            if(o != null){
                                Clan oClan = ClanManager.instance().getClanOf(o);
                                if(oClan == null){
                                    clan.invite(p, o);
                                } else {
                                    p.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.player_already_in_clan").replace("%name%", o.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.player_not_online")));
                            }
                        } else {
                            p.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.only_assistant_can_invite")));
                        }
                    } else {
                        p.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.clan_invite_usage")));
                    }
                } else
                    sendNoClan(p);
            }
            return true;
        }
    }

    private static class AcceptCommand extends Command {

        public AcceptCommand() {
            super("accept");
        }

        @Override
        public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                ArgumentParser ap = new ArgumentParser(args);
                if(ap.hasExactly(1)) {
                    String clanName = ap.get(1);
                    Clan clan = ClanManager.instance().getClan(clanName);
                    FileConfiguration conf = ConfigManager.getConfig("Clan");
                    if(clan == null) {
                        p.sendMessage(Util.translate(conf.getString("messages.clan_not_existing").replace("%clanname%", clanName)));
                    } else {
                        clan.accept(p);
                    }
                }
            }
            return false;
        }
    }

    private static final String NO_CLAN = Util.translate(ConfigManager.getConfig("Clan").getString("messages.you_have_no_clan"));
    private static void sendNoClan(Player p){
        p.sendMessage(NO_CLAN);
    }

}
