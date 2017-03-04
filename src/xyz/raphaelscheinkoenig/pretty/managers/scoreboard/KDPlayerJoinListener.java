package xyz.raphaelscheinkoenig.pretty.managers.scoreboard;

import com.apple.concurrent.Dispatch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class KDPlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e){
        MyScoreboardManager.instance().createAndApplyScoreboardFor(e.getPlayer());
    }

}
