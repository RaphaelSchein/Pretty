package xyz.raphaelscheinkoenig.pretty.managers.teleport.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;

public class TPPlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		TeleportManager.instance().removeTPRequests(p);
	}
	
}
