package xyz.raphaelscheinkoenig.pretty.managers.user;

import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.managers.User;

public class SimpleUser extends User {

	private int userId;
	
	public SimpleUser(Player p, int userId) {
		super(p);
		this.userId = userId;
	}
	
	public int getUserId(){
		return userId;
	}

}
