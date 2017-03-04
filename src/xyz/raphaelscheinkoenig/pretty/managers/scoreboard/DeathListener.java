package xyz.raphaelscheinkoenig.pretty.managers.scoreboard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player killed = e.getEntity();
        MyScoreboardManager.instance().increaseDeathsOf(killed);
        EntityDamageEvent lastDamage = killed.getLastDamageCause();
        if(lastDamage.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            if(lastDamage instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) lastDamage;
                Entity damager = e2.getDamager();
                if(damager instanceof Player){
                    Player killer = (Player) damager;
                    if(!killer.getName().equals(killed.getName()))
                        MyScoreboardManager.instance().increaseKillsOf(killer);
                }
            }
        }
    }

}
