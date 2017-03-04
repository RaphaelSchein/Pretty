package xyz.raphaelscheinkoenig.pretty.managers.home.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import mkremins.fanciful.FancyMessage;

public class ListHomeCommand extends Command {

	public ListHomeCommand() {
		super("homes", "listhome", "homelist", "lh", "hl");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		FileConfiguration conf = ConfigManager.getConfig("Home");
		String showHomes = Util.translate(conf.getString("messages.show_homes"));
		String separator = Util.translate(conf.getString("messages.home_separator"));
		String perHomeDesign = Util.translate(conf.getString("messages.per_home_design"));
		FancyMessage message = new FancyMessage(showHomes);
		HomeManager.instance().getHomeNames(p, homeNames -> {
//			StringBuilder sb = new StringBuilder();
			if (homeNames.isEmpty()) {
				message.then(Util.translate(conf.getString("messages.no_homes")));
			} else {
				boolean first = true;
				for (String homeName : homeNames) {
					if (first)
						first = false;
					else
						message.then(separator);
					message.then(perHomeDesign.replace("%home%", homeName)).command("/home " + homeName).tooltip("Click to teleport");
				}
			}
//			p.sendMessage(Util.translate(showHomes.replace("%all_homes%", sb.toString())));
			message.send(p);
		});
		return false;
	}

}
