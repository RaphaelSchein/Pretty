package xyz.raphaelscheinkoenig.pretty.managers.clan.listeners;

import xyz.raphaelscheinkoenig.pretty.managers.clan.Clan;
import xyz.raphaelscheinkoenig.pretty.managers.clan.ClanManager;
import xyz.raphaelscheinkoenig.pretty.pvp.PvPManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class ClanListener implements Listener {


//    @EventHandler
    public static void onEntityDamage(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        if(damager instanceof Player) {
            Entity damaged = e.getEntity();
            if(damaged instanceof Player){
                Player pDamager = (Player) damager;
                Player pDamaged = (Player) damaged;
                Clan cDamager = ClanManager.instance().getClanOf(pDamager);
                if(cDamager != null){
                    Clan cDamaged = ClanManager.instance().getClanOf(pDamaged);
                    if(cDamaged != null){
                        if(cDamaged.getName().equals(cDamager.getName())){
                            if(!cDamager.isFriendlyFire()){
//                                pDamager.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.you_cannot_hit_clan_members")));
                                e.setCancelled(true);
                            }
                        }
                    }
                }
                if (!e.isCancelled()) {
                    PvPManager.instance().updateLastHit(pDamager);
                    PvPManager.instance().updateLastHit(pDamaged);
                }
            }
        }
    }

//    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
//    public void onChat(AsyncPlayerChatEvent e){
//        Player p = e.getPlayer();
//        String message = e.getMessage();
//    }

}
