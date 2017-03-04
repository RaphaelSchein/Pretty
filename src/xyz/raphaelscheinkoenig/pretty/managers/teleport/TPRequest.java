package xyz.raphaelscheinkoenig.pretty.managers.teleport;

import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation.TpaTeleportation;

public class TPRequest {

	private Player from;
	private Player to;
	private long requestTime;
	
	public TPRequest(Player from, Player to){
		this.from = from;
		this.to = to;
		requestTime = System.currentTimeMillis();
	}
	
	public boolean isFrom(Player p){
		System.out.println("Checking if " + p.getName() + " = " + from.getName());
		return p.getName().equals(from.getName());
	}
	
	public boolean isTo(Player p){
		return p.getName().equals(to.getName());
	}
	
	public Player getFrom(){
		return from;
	}
	
	public Player getTo(){
		return to;
	}
	
	public boolean isExpired(int seconds){
		long current = System.currentTimeMillis();
		double elapsed = ((double)current-requestTime);
		return elapsed > seconds;
	}
	
	public void startTeleportation(){
		new TpaTeleportation(from, to, 5).proceed();
	}
	
}
