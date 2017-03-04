package xyz.raphaelscheinkoenig.pretty.managers.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		UserManager.instance().unregisterUser(p);
	}
	
}
