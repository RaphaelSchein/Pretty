package xyz.raphaelscheinkoenig.pretty.pvp;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.CancellableRunnable;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.entity.Player;


public class PvPRunnable extends CancellableRunnable {

    private Player player;

    public PvPRunnable(Player player) {
        this.player = player;
    }

    @Override
    public void onRun() {
        if(!player.isOnline())
            cancelMe();
        else
            if(!PvPManager.instance().isInFight(player)) {
                player.sendMessage(Util.translate(ConfigManager.getConfig("pvp").getString("no_longer_in_fight")));
                PvPManager.instance().onNoPvpAnymore(player);
                cancelMe();
            }
    }

}
