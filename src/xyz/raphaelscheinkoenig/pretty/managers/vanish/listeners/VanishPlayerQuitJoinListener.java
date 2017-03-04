package xyz.raphaelscheinkoenig.pretty.managers.vanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.raphaelscheinkoenig.pretty.managers.vanish.VanishManager;

public class VanishPlayerQuitJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		boolean isVanished = VanishManager.instance().isVanished(p);
		if(isVanished){
			VanishManager.instance().vanishPlayer(p);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		boolean isVanished = VanishManager.instance().isVanished(p);
		if(isVanished){
			VanishManager.instance().removePlayer(p);
		}
	}
	
}
