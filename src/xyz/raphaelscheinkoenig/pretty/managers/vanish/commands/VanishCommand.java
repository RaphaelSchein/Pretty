package xyz.raphaelscheinkoenig.pretty.managers.vanish.commands;

import net.sirminer.main.SystemAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.vanish.VanishManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class VanishCommand extends Command {

	public VanishCommand() {
		super("vanish", "v");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		boolean canVanish = VanishManager.instance().canVanish(p);
		if(canVanish){
			boolean isVanished = VanishManager.instance().isVanished(p);
			String message;
			if(isVanished){
				VanishManager.instance().unvanishPlayer(p);
				message = Util.translate(ConfigManager.getConfig("Vanish").getString("vanish.you_were_unvanished"));
			} else {
				VanishManager.instance().vanishPlayer(p);
				message = Util.translate(ConfigManager.getConfig("Vanish").getString("vanish.you_were_vanished"));
			}
			p.sendMessage(message);
		}
		return true;
	}

}
