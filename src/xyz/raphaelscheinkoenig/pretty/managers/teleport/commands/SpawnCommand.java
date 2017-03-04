package xyz.raphaelscheinkoenig.pretty.managers.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation.CountedTeleportation;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class SpawnCommand extends Command {

	public SpawnCommand() {
		super("spawn", "s");
	}

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		Location spawn = (Location) ConfigManager.getConfig("config").get("locations.spawn");
		if(spawn == null){
			p.sendMessage(Util.translate(ConfigManager.getConfig("Commands").getString("spawn.no_spawn_was_set")));
		} else {
			String spawnName = Util.translate(ConfigManager.getConfig("Commands").getString("spawn.name"));
			new CountedTeleportation(p, spawn, spawnName == null ? "Spawn" : spawnName, 5).proceed();;
		}
		return true;
	}

	
	
}
