package xyz.raphaelscheinkoenig.pretty.managers.regions.listeners;

import xyz.raphaelscheinkoenig.pretty.managers.clan.listeners.ClanListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import xyz.raphaelscheinkoenig.pretty.managers.regions.RegionFlag;
import xyz.raphaelscheinkoenig.pretty.managers.regions.RegionManager;
import xyz.raphaelscheinkoenig.pretty.managers.regions.types.FlagRegion;
import xyz.raphaelscheinkoenig.pretty.managers.regions.types.Region;

public class FlagListener implements Listener {

	@EventHandler(ignoreCancelled=true)
	public void onEntityDamage(EntityDamageEvent e){
		Entity ent = e.getEntity();
		Location loc = ent.getLocation();
		Region affected = RegionManager.instance().getRegion(loc);
		if(affected instanceof FlagRegion){
			FlagRegion flagged = (FlagRegion) affected;
			if(flagged.hasFlag(RegionFlag.NO_DAMAGE_AT_ALL)){
				e.setCancelled(true);
			} else {
				boolean isHostile = ent instanceof Monster;
				boolean isPlayer = ent instanceof Player;
				if(flagged.hasFlag(RegionFlag.NO_DAMAGE_ENTITY)){
					if(!isHostile && !isPlayer)
						e.setCancelled(true);
				}
				if(flagged.hasFlag(RegionFlag.NO_DAMAGE_ENTITY_HOSTILE))
					if(isHostile)
						e.setCancelled(true);
				if(flagged.hasFlag(RegionFlag.NO_DAMAGE_PLAYER)){
					if(isPlayer)
						e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		Entity damager = e.getDamager();
		Entity to = e.getEntity();
		boolean isDamagerPlayer = damager instanceof Player;
		boolean isToPlayer = to instanceof Player;
		if(isDamagerPlayer && isToPlayer){
			Player damagerP = (Player) damager;
			Player toP = (Player) to;
			Region damagerRegion = RegionManager.instance().getRegion(damagerP);
			if(damagerRegion instanceof FlagRegion) {
				FlagRegion flagged = (FlagRegion) damagerRegion;
				if(flagged.hasFlag(RegionFlag.NO_PVP)){
//					damager.sendMessage(Util.translate(ConfigManager.getConfig("Region").getString("no_pvp_allowed_where_you_stand")));
					e.setCancelled(true);
				}
			}
			if(!e.isCancelled()){
				Region toRegion = RegionManager.instance().getRegion(toP);
				if(toRegion instanceof FlagRegion) {
					FlagRegion flagged = (FlagRegion) toRegion;
					if(flagged.hasFlag(RegionFlag.NO_PVP)) {
//						damager.sendMessage(Util.translate(ConfigManager.getConfig("Region").getString("no_pvp_allowed_where_opponent_stands")));
						e.setCancelled(true);
					}
				}
			}
			if (!e.isCancelled()) {
			    ClanListener.onEntityDamage(e);
            }
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onFoodLevelChance(FoodLevelChangeEvent e){
		HumanEntity he = e.getEntity();
		Location loc = he.getLocation();
		Region affected = RegionManager.instance().getRegion(loc);
		if(affected instanceof FlagRegion) {
			FlagRegion flagged = (FlagRegion) affected;
			if(flagged.hasFlag(RegionFlag.NO_HUNGER)){
				e.setCancelled(true);
			}
		}
	}
	
}
