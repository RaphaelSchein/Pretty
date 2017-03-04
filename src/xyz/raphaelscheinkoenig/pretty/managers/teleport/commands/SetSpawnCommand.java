package xyz.raphaelscheinkoenig.pretty.managers.teleport.commands;

import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;

public class SetSpawnCommand extends Command {

	public SetSpawnCommand() {
		super("setspawn");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
        if(Util.getPower(p) < 640) {
            noPermissions(p);
            return true;
        }
        Location loc = p.getLocation();
		ConfigManager.getConfig("config").set("locations.spawn", loc);
		ConfigManager.saveConfig("config");
		p.sendMessage("The spawn was set.");
		return true;
	}

}
