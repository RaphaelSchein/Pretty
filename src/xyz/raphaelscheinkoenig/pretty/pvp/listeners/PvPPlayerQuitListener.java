package xyz.raphaelscheinkoenig.pretty.pvp.listeners;

import xyz.raphaelscheinkoenig.pretty.pvp.PvPManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PvPPlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PvPManager.instance().onLogout(p);
    }

}
