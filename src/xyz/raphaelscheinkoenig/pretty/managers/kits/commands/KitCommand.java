package xyz.raphaelscheinkoenig.pretty.managers.kits.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.managers.kits.KitSelectGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class KitCommand extends Command {

    public KitCommand() {
        super("kit", "kits");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            new KitSelectGUI(p);
        }
        return true;
    }

}
