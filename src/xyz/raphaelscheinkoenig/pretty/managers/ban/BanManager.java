package xyz.raphaelscheinkoenig.pretty.managers.ban;

import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class BanManager extends Manager {

    @Override
    public void onEnable() {
        super.onEnable();
        initListeners();
    }

    private void initListeners() {
        Listener[] listeners = {
                new LoginListener()
        };
        for(Listener l : listeners)
            registerListener(l);
    }



    public void banPlayer(Player p){

    }

//    public boolean isBanned(Player p){
//
//    }
//
//    public boolean isBanned(String uuid){
//
//    }

    @Override
    public String getName() {
        return "Ban";
    }

}
