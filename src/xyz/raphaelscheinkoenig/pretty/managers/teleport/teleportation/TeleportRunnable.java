package xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import xyz.raphaelscheinkoenig.pretty.util.CancellableRunnable;

public abstract class TeleportRunnable extends CancellableRunnable {
	
	protected Player player;
	private Vector firstPosition;
	private double health;
	
	public TeleportRunnable(Player player){
		this.player = player;
		firstPosition = player.getLocation().toVector();
		health = player.getHealth();
	}
	

	public boolean hasMoved(){
		return hasMoved(0.5);
	}
	

	public boolean hasMoved(double distanceSquared){
		return player.getLocation().toVector().distanceSquared(firstPosition) > distanceSquared;
	}
	

	public boolean hasLostHealth(){
		return player.getHealth() < health;
	}
	

	public boolean lostHealthOrHasMoved(){
		return hasMoved() || hasLostHealth();
	}
	
}
