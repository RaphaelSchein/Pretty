package xyz.raphaelscheinkoenig.pretty.managers.invsee.commands;

import xyz.raphaelscheinkoenig.pretty.managers.invsee.InvseeGUI;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;

public class InvseeCommand extends Command {

	public InvseeCommand() {
		super("invsee");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		int power = Util.getPower(p);
		if(power < 192) {
		    noPermissions(p);
		    return true;
        }
        ArgumentParser ap = new ArgumentParser(args);
		if(ap.hasExactly(1)){
			String playerName = ap.get(1);
			Player o = Bukkit.getPlayer(playerName);
			if(o != null){
			    new InvseeGUI(p, o);
			}
		} else {
			p.sendMessage("Correct usage: /invsee <playername>");
		}
		return true;
	}

}
