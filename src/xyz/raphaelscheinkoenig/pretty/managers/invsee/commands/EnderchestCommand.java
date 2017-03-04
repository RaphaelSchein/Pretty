package xyz.raphaelscheinkoenig.pretty.managers.invsee.commands;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class EnderchestCommand extends Command {

    public EnderchestCommand() {
        super("enderchest", "echest");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            int power = Util.getPower(p);
            if(power < 192) {
                noPermissions(p);
                return true;
            }
            ArgumentParser ap = new ArgumentParser(args);
            if(ap.hasNoArguments()){
                p.openInventory(p.getEnderChest());
            } else if(ap.hasExactly(1)){
                String oName = ap.get(1);
                Player o = Bukkit.getPlayer(oName);
                if(o == null){

                }
            }
        }
        return true;
    }
}
