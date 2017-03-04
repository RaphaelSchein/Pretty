package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class DeleteHomeCommand extends Command {

	public DeleteHomeCommand() {
		super("delhome", "homedel", "dh", "hd");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		ArgumentParser ap = new ArgumentParser(args);
		if(ap.hasExactly(1)){
			String homeName = ap.get(1);
			HomeManager.instance().getHome(p, homeName, loc -> {
				if(loc == null)
					p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.no_home_named").replace("%name%", homeName)));
				else {
					HomeManager.instance().deleteHome(p, homeName);
					p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.home_deleted_successfully").replace("%name%", homeName)));
				}
			});
		} else {
			p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.correct_usage_delhome")));
		}
		return true;
	}

}
