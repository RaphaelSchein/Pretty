package xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.LamaCore;

public class InstantTeleportation extends Teleportation {

	public InstantTeleportation(Player p, Location destination, String destinationName) {
		super(p, destination, destinationName);
	}

	@Override
	public TeleportRunnable initRunnable() {
		return new TeleportRunnable(player){
			public void onRun(){
				cancel();
				player.teleport(destination);
			}
		};
	}

	@Override
	protected void onProceed() {
		runnable.runTask(LamaCore.instance());
	}
	
}
