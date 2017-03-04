package xyz.raphaelscheinkoenig.pretty.managers;

import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.util.Util;

public abstract class User {

	protected final Player player;
	protected final String uuid;
	
	public User(Player p){
		this.player = p;
		uuid = p.getUniqueId().toString();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public boolean isUser(Player o){
		return uuid.equals(Util.uuid(o));
	}
	
}
