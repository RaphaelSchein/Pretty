package xyz.raphaelscheinkoenig.pretty.managers.regions.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.raphaelscheinkoenig.pretty.managers.regions.RegionManager;
import xyz.raphaelscheinkoenig.pretty.managers.regions.components.FlatRegion;
import xyz.raphaelscheinkoenig.pretty.managers.regions.types.Region;

public class RegionListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Region current = RegionManager.instance().getCachedRegion(p);
		Region now = RegionManager.instance().getRegion(p);
		
		if(now == current){
			current.onPlayerMove(e);
		} else {
//			System.out.println("Changed from " + now.getClass().getName() + " to " + current.getClass().getName());
			now.onPlayerEnter(e, current);
			if(current != null)
				current.onPlayerLeave(e, now);
			RegionManager.instance().setRegion(p, now);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		RegionManager.instance().setRegion(p, Region.ANY);
//		onPlayerMove(new PlayerMoveEvent(p, p.getLocation(), p.getLocation()));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(p.isSneaking()){
			Region r = new Region();
			FlatRegion fr = new FlatRegion(p.getLocation().getWorld().getUID().toString(), -10, -10, 10, 10);
			r.addRegion(fr);
			RegionManager.instance().addRegion(r);
		}
	}

	@EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
	    Block bl = e.getBlock();
	    Region reg = RegionManager.instance().getRegion(bl.getLocation());
	    reg.onBlockPlace(e);
    }

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
	    Block bl = e.getBlock();
	    Region r = RegionManager.instance().getRegion(bl.getLocation());
	    r.onBlockBreak(e);
    }
}
