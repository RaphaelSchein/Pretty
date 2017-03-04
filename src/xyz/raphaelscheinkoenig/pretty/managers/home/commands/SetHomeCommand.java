package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class SetHomeCommand extends Command {

	public SetHomeCommand() {
		super("sethome", "homeset", "sh", "hs");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		ArgumentParser ap = new ArgumentParser(args);
		if(ap.hasNoArguments()){
			sender.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.set_home_usage")));
		} else {
			if(ap.hasExactly(1)){
				String homeName = ap.get(1);
				if(homeName.length() > 20){
					p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.home_name_too_long").replace("%name%", homeName)));
					return true;
				} else {
					HomeManager.instance().getHomeNames(p, list -> {
						int homes = list.size();
						int allowedHomes = HomeManager.instance().getMaxHomes(p); //TODO
						if(homes >= allowedHomes){
							p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.too_many_homes")));
							return;
						}
						HomeManager.instance().getHome(p, homeName, loc -> {
							if(loc == null){
								Location newLoc = p.getLocation();
								HomeManager.instance().addHome(p, homeName, newLoc);
								sender.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.home_created").replace("%name%", homeName)));

							} else {
								p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.home_already_exists").replace("%name%", homeName)));
							}
						});
					});
					return true;
				}
			} else {
				sender.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.set_home_usage")));
			}
		}
		return true;
	}
	
}
