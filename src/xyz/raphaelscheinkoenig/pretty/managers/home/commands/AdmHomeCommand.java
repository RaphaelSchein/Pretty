package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class AdmHomeCommand extends Command {

    public AdmHomeCommand() {
        super("admhome");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player p = (Player) sender;

        ArgumentParser ap = new ArgumentParser(args);
        if(ap.hasNoArguments()){
            p.sendMessage("Usages:");
            p.sendMessage("/admhome <player_name>: lists homes of player");
            p.sendMessage("/admhome <player_name> <home_name>: immediately deletes the home");
        } else if(ap.hasExactly(1)){
            Player o = Bukkit.getPlayer(ap.get(1));
            if(o == null){
                p.sendMessage(ChatColor.RED + "This player is offline. Can only look for Online players.");
                return true;
            }
            FileConfiguration conf = ConfigManager.getConfig("Home");
            String showHomes = Util.translate(conf.getString("messages.show_homes"));
            String separator = Util.translate(conf.getString("messages.home_separator"));
            FancyMessage message = new FancyMessage(showHomes);
            HomeManager.instance().getHomeNames(o, homeNames -> {
//			StringBuilder sb = new StringBuilder();
                if (homeNames.isEmpty()) {
                    message.then(Util.translate(conf.getString("messages.no_homes")));
                } else {
                    boolean first = true;
                    for (String homeName : homeNames) {
                        if (first)
                            first = false;
                        else
                            message.then(separator);
                        message.then(homeName).command("/admhometp " + o.getName() + " " + homeName).tooltip("Click to teleport");
                    }
                }
//			p.sendMessage(Util.translate(showHomes.replace("%all_homes%", sb.toString())));
                message.send(p);
            });
        } else if(ap.hasExactly(2)){
            String playerName = ap.get(1);
            String homeName = ap.get(2);
            Player o = Bukkit.getPlayer(playerName);
            if(o != null){
                HomeManager.instance().deleteHome(o, homeName);
                p.sendMessage(ChatColor.RED + "Deleted.");
            }
        }
        return true;
    }

}
