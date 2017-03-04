package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AdmTpHomeCommand extends Command {

    public AdmTpHomeCommand() {
        super("admhometp");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        if(args.length != 2)
            return true;

        String playerName = args[0];
        String homeName = args[1];

        Player o = Bukkit.getPlayer(playerName);
        if(o != null){
            HomeManager.instance().getHome(o, homeName, loc -> {
                ((Player)sender).teleport(loc);
            });
        }
        return true;
    }
}
