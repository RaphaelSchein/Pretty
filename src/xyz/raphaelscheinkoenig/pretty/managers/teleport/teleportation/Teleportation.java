package xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;



public abstract class Teleportation {

	protected final Player player;
	protected final Location destination;
	protected final String destinationName;
	protected boolean started;
	protected TeleportRunnable runnable;


	public Teleportation(Player p, Location destination, String destinationName) {
		this.player = p;
		this.destination = destination;
		this.destinationName = destinationName;
		started = false;
	}


	public abstract TeleportRunnable initRunnable();
	

	public final void proceed() {
		TeleportManager.instance().proceed(this);
		started = true;
		runnable = initRunnable();
		onProceed();
	}


	protected abstract void onProceed();
	

	public final void cancelTeleport() {
		if (!started)
			throw new IllegalStateException(
					"Teleportation " + getClass().getSimpleName() + " could not be proceeded because it didn't start");
		Objects.requireNonNull(runnable);
		runnable.cancel();
		onCancel();
	}

	public final void cancel(){
		TeleportManager.instance().removeTeleportation(this);
		onCancel();
	}
	

	protected void onCancel() {
		player.sendMessage(Util.translate(ConfigManager.getConfig("Teleportation")
				.getString("messages.on_teleport_cancelled").replace("%destination%", destinationName)));
	}


	public Player getPlayer() {
		return player;
	}
	

	public Location getDestination(){
		return destination;
	}

}
