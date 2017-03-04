package xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TpaTeleportation extends CountedTeleportation {

	private Player to;
	
	public TpaTeleportation(Player p, Player to, int seconds) {
		super(p, to.getLocation(), to.getDisplayName(), seconds);
		this.to = to;
	}
	
	@Override
	public Location getDestination() {
		return to.getLocation();
	}

}
