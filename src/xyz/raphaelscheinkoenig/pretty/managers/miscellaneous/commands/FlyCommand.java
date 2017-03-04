package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class FlyCommand extends Command {

	public FlyCommand() {
		super("fly", "f");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if(Util.getPower(p) >= 512){
			boolean canFly = p.getAllowFlight();
			if(canFly){
				p.setAllowFlight(false);
				p.sendMessage(Util.translate(ConfigManager.getConfig("Misc").getString("messages.flying_disabled")));
			} else {
				p.setAllowFlight(true);
				p.sendMessage(Util.translate(ConfigManager.getConfig("Misc").getString("messages.flying_enabled")));
			}
		} else
			noPermissions(p);
		return true;
	}

}
