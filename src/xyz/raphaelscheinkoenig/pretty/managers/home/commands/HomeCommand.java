package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation.CountedTeleportation;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class HomeCommand extends Command {

	public HomeCommand() {
		super("home", "h");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		ArgumentParser ap = new ArgumentParser(args);
		if(ap.hasExactly(1)){
			String name = ap.get(1);
			HomeManager.instance().getHome(p, name, loc -> {
				if(loc == null){
					p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.no_home_named").replace("%name%", name)));
				} else
					new CountedTeleportation(p, loc, name, 5).proceed(); //5 seconds may not be hardcoded
			});
		} else {
			p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.correct_usage_home")));
		}
		return true;
	}

}
