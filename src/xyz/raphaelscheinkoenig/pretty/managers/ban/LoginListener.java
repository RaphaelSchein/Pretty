package xyz.raphaelscheinkoenig.pretty.managers.ban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        Player p = e.getPlayer();
    }

}
