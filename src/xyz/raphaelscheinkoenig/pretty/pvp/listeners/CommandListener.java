package xyz.raphaelscheinkoenig.pretty.pvp.listeners;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.pvp.PvPManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if(PvPManager.instance().isInFight(p)) {
            p.sendMessage(Util.translate(ConfigManager.getConfig("pvp").getString("no_commands_allowed")));
            e.setCancelled(true);
        }
    }

}
