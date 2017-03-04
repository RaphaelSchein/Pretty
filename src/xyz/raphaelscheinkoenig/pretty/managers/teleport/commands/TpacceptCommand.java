package xyz.raphaelscheinkoenig.pretty.managers.teleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;

public class TpacceptCommand extends Command {

	public TpacceptCommand() {
		super("tpaccept");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player from = (Player) sender;
		ArgumentParser ap = new ArgumentParser(args);
		if(ap.hasExactly(1)){
			Player to = Bukkit.getPlayer(ap.get(1));
			TeleportManager.instance().tpaccept(to, from);
		}
		return true;
	}

}
