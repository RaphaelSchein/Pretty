package xyz.raphaelscheinkoenig.pretty.managers.teleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class TpaCommand extends Command {

	public TpaCommand() {
		super("tpa", "tpr");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		ArgumentParser ap = new ArgumentParser(args);
		FileConfiguration conf = ConfigManager.getConfig("Teleportation");
		if(ap.hasExactly(1)){
			String toName = ap.get(1);
			Player to = Bukkit.getPlayer(toName);
			if(to == null){
				p.sendMessage(Util.translate(conf.getString("messages.user_not_online").replace("%name%", toName)));
				return true;
			} else {
				TeleportManager.instance().requestTpa(p, to);
			}
		} else {
			p.sendMessage(Util.translate(conf.getString("messages.tpa_usage")));
		}
		return true;
	}

}
